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
      List<Resource> titles = findAllValidTitlesInContainer(contentsContainer);

      hierarchyErrorMessage = "";
      ListIterator<Resource> titlesIter = titles.listIterator();
      BlogPostTableOfContentsItemComponent stubRoot = new BlogPostTableOfContentsItemComponent(
          "stub", "", "");
      BlogPostTableOfContentsItemComponent currentNode = stubRoot;
      int currentLevel = maxHeadingLevel - 1;

      while (titlesIter.hasNext()) {
        Resource titleResource = titlesIter.next();
        TitleComponent titleComponent = titleResource.adaptTo(TitleComponent.class);
        BlogPostTableOfContentsItemComponent newNode = prepareNewContentItem(titleComponent);
        if (newNode != null) {
          int headingLevel = getHeadingLevel(titleComponent);
          if (headingLevel == NO_HEADING_LEVEL) {
            //  skip this node as it's incorrectly configured
          } else if (headingLevel > currentLevel + 1) {
            if (StringUtils.isBlank(hierarchyErrorMessage)) {
              hierarchyErrorMessage = String.format(
                  "Wrong header level for title #%s: expected h%s or higher, got h%s. "
                      + "This element will be skipped",
                  titlesIter.nextIndex(), currentLevel + 1, headingLevel);
            }
          } else if (headingLevel == currentLevel + 1) {
            //  we found a child: add it to current node
            currentNode.getSubTitles().add(newNode);
            newNode.setParent(currentNode);
            //  now the new node is potential parent for the upcoming nodes
            currentNode = newNode;
            currentLevel++;
          } else if (headingLevel == currentLevel) {
            //  we found a sibling: add it to parent
            BlogPostTableOfContentsItemComponent parent = currentNode.getParent();
            if (parent != null) {
              parent.getSubTitles().add(newNode);
              newNode.setParent(parent);
              //  now the new node is potential parent for the upcoming nodes
              currentNode = newNode;
            }
          } else if (headingLevel < currentLevel) {
            //  we found a sibling to one of a parent levels, so go up the hierarchy
            while (currentLevel > headingLevel - 1) {
              //  parent != null check is omitted, because only root stub has no parent,
              //  and we will never reach the root by design
              currentNode = currentNode.getParent();
              currentLevel--;
            }
            currentNode.getSubTitles().add(newNode);
            newNode.setParent(currentNode);
          }
        }
      }

      this.subTitles = stubRoot.getSubTitles();
    }
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

  int getHeadingLevel(Resource resource) {
    return getHeadingLevel(resource.adaptTo(TitleComponent.class));
  }

  int getHeadingLevel(TitleComponent titleComponent) {
    if (titleComponent != null) {
      String headingLevelValue = titleComponent.getElement();
      if (headingLevelValue != null && !"p".equals(headingLevelValue)) {
        return Integer.parseInt(headingLevelValue.substring(1));
      }
    }
    return NO_HEADING_LEVEL;
  }

  private List<Resource> findAllValidTitlesInContainer(Resource container) {
    List<Resource> result = new ArrayList<>();

    if (container != null) {
      for (Resource titleResource : container.getChildren()) {
        if (titleResource.isResourceType(TITLE_RESOURCE_PATH)) {
          int headingLevel = getHeadingLevel(titleResource);
          if (headingLevel <= minHeadingLevel && headingLevel >= maxHeadingLevel) {
            result.add(titleResource);
          }
        }
      }
    }

    return result;
  }
}
