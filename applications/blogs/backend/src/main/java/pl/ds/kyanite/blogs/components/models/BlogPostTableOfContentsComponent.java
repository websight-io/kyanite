/*
 * Copyright (C) 2023 Dynamic Solutions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.ds.kyanite.blogs.components.models;

import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import pl.ds.kyanite.common.components.models.TitleComponent;
import pl.ds.websight.pages.core.api.Page;
import pl.ds.websight.pages.core.api.PageManager;


@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class BlogPostTableOfContentsComponent {

  private static final String BLOG_CONTENT_CONTAINER_REL_PATH =
      "/jcr:content/pagecontainer/section/container/columns1/column4";
  private static final String TITLE_RESOURCE_PATH = "kyanite/common/components/title";
  private static final int NO_HEADING_LEVEL = -1;

  @ValueMapValue
  @Getter
  private boolean isHidden;

  @Getter
  @Inject
  @Default(values = "Table of contents")
  private String title;

  @Getter
  @Inject
  @Default(intValues = 2)
  private Integer maxHeadingLevel;

  @Getter
  @Inject
  @Default(intValues = 6)
  private Integer minHeadingLevel;

  @Getter
  @Inject
  @Default(booleanValues = true)
  private boolean showBackToTopButton;

  @Getter
  @Inject
  @Default(values = "Back to top")
  private String backToTopButtonLabel;

  @Getter
  private List<BlogPostTableOfContentsItemComponent> subTitles;

  @Getter
  private String hierarchyErrorMessage;

  @SlingObject
  private ResourceResolver resourceResolver;
  @SlingObject
  private Resource resource;

  @PostConstruct
  private void init() {
    prepareTitlesHierarchy();
  }

  private void prepareTitlesHierarchy() {
    this.subTitles = new ArrayList<>();
    Resource pageResource = getContainingPageAsResource(this.resource);

    if (pageResource != null) {
      Resource contentsContainer = resourceResolver.getResource(
          pageResource.getPath() + BLOG_CONTENT_CONTAINER_REL_PATH);
      List<TitleComponent> titles = findAllValidTitlesInContainer(contentsContainer);

      hierarchyErrorMessage = "";
      ListIterator<TitleComponent> titlesIter = titles.listIterator();
      BlogPostTableOfContentsItemComponent stubRoot = new BlogPostTableOfContentsItemComponent(
          "stub", "", "h1");
      BlogPostTableOfContentsItemComponent currentNode = stubRoot;

      while (titlesIter.hasNext()) {
        BlogPostTableOfContentsItemComponent newNode = prepareNewContentItem(titlesIter.next());
        currentNode = handleAddTitleNode(currentNode, newNode);
      }

      this.subTitles = stubRoot.getSubTitles();
    }
  }

  /**
   * Helper method for building title hierarchy.
   * Considering all titles processed are represented as a one-directional sequence,
   * compares the 'next' title element against currentNode aka 'lowest possible parent':
   *  - if it is a child/sibling/(grand)parent of currentNode:
   *    - (optionally) goes up the hierarchy to the proper parent
   *    - adds it to the parent's children
   *    - makes it a new lowest possible parent for the upcoming titles
   *  - if it has too low level comparing to the current element - skips it, fills the error message
   *
   * @param currentNode the lowest possible parent for newNode
   * @param newNode the next title in the sequence, needs to be placed in the hierarchy
   * @return the new lowest possible parent for the upcoming titles
   */
  BlogPostTableOfContentsItemComponent handleAddTitleNode(
      BlogPostTableOfContentsItemComponent currentNode,
      BlogPostTableOfContentsItemComponent newNode
  ) {
    if (newNode != null) {
      if (isGrandchildLevelTitle(currentNode, newNode)) {
        if (StringUtils.isBlank(hierarchyErrorMessage)) {
          hierarchyErrorMessage = String.format(
              "Wrong header level for %s with title %s: expected h%s or higher, got h%s. "
                  + "This element will be skipped",
              newNode.getHeadingLevel(),
              newNode.getTitle(),
              getHeadingLevel(currentNode) + 1,
              getHeadingLevel(newNode));
        }
      } else if (isChildLevelTitle(currentNode, newNode)) {
        currentNode = addSubTitle(currentNode, newNode);
      } else if (isSiblingLevelTitle(currentNode, newNode)) {
        currentNode = addSubTitle(currentNode.getParent(), newNode);
      } else {
        //  we found a sibling to one of a parent levels, so go up the hierarchy
        while (!isChildLevelTitle(currentNode, newNode)) {
          currentNode = currentNode.getParent();
        }
        currentNode = addSubTitle(currentNode, newNode);
      }
    }
    return currentNode;
  }

  /**
   *  Helper method for building title hierarchy.
   *    - adds subTitle to current title subtitles
   *    - sets title as subTitle's parent
   *    - returns subTitle as the new lowest possible parent for upcoming titles
   */
  BlogPostTableOfContentsItemComponent addSubTitle(
      BlogPostTableOfContentsItemComponent title,
      BlogPostTableOfContentsItemComponent subTitle
  ) {
    title.getSubTitles().add(subTitle);
    subTitle.setParent(title);
    return subTitle;
  }


  boolean isGrandchildLevelTitle(
      BlogPostTableOfContentsItemComponent baseTitle,
      BlogPostTableOfContentsItemComponent comparedTitle
  ) {
    return getHeadingLevel(comparedTitle) > getHeadingLevel(baseTitle) + 1;
  }

  boolean isChildLevelTitle(
      BlogPostTableOfContentsItemComponent baseTitle,
      BlogPostTableOfContentsItemComponent comparedTitle
  ) {
    return getHeadingLevel(comparedTitle) == getHeadingLevel(baseTitle) + 1;
  }

  boolean isSiblingLevelTitle(
      BlogPostTableOfContentsItemComponent baseTitle,
      BlogPostTableOfContentsItemComponent comparedTitle
  ) {
    return getHeadingLevel(comparedTitle) == getHeadingLevel(baseTitle);
  }

  private Resource getContainingPageAsResource(Resource currentResource) {
    if (currentResource != null) {
      PageManager pageManager = resource.getResourceResolver().adaptTo(PageManager.class);

      if (pageManager != null) {
        Page containingPage = pageManager.getContainingPage(currentResource.getPath());
        if (containingPage != null) {
          return containingPage.getResource();
        }
      }
    }

    return null;
  }

  private BlogPostTableOfContentsItemComponent prepareNewContentItem(
      TitleComponent titleComponent) {

    if (titleComponent != null) {
      String titleValue = titleComponent.getRawText();
      String anchorIdValue = titleComponent.getAnchorId();
      String headingLevelValue = titleComponent.getElement();

      return new BlogPostTableOfContentsItemComponent(
          titleValue, anchorIdValue, headingLevelValue);
    }

    return null;
  }

  int getHeadingLevel(String headingElement) {
    if (headingElement != null && !"p".equals(headingElement)) {
      return Integer.parseInt(headingElement.substring(1));
    }
    return NO_HEADING_LEVEL;
  }

  int getHeadingLevel(TitleComponent titleComponent) {
    return titleComponent == null
        ? NO_HEADING_LEVEL
        : getHeadingLevel(titleComponent.getElement());
  }

  int getHeadingLevel(BlogPostTableOfContentsItemComponent tocItem) {
    return getHeadingLevel(tocItem.getHeadingLevel());
  }


  private List<TitleComponent> findAllValidTitlesInContainer(Resource container) {
    List<TitleComponent> result = new ArrayList<>();

    if (container != null) {
      for (Resource titleResource : container.getChildren()) {
        if (titleResource.isResourceType(TITLE_RESOURCE_PATH)) {
          TitleComponent titleComponent = titleResource.adaptTo(TitleComponent.class);
          int headingLevel = getHeadingLevel(titleComponent);
          if (headingLevel <= minHeadingLevel && headingLevel >= maxHeadingLevel) {
            result.add(titleComponent);
          }
        }
      }
    }

    return result;
  }
}
