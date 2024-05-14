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
import pl.ds.kyanite.blogs.components.exceptions.tableofcontents.BlogPostTableOfContentsHierarchyException;
import pl.ds.kyanite.common.components.models.TitleComponent;
import pl.ds.websight.pages.core.api.Page;
import pl.ds.websight.pages.core.api.PageManager;


@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class BlogPostTableOfContentsComponent {
  private static final String BLOG_CONTENT_CONTAINER_REL_PATH =
      "/jcr:content/pagecontainer/section/container/columns1/column4";
  private static final String TITLE_RESOURCE_PATH = "kyanite/common/components/title";

  @ValueMapValue
  @Getter
  private boolean isHidden;

  @Getter
  @Inject
  @Default(values = "Table of contents")
  private String title;

  @Getter
  @Inject
  @Default(intValues = 1)
  private Integer maxHeadingLevel;

  @Getter
  @Inject
  @Default(booleanValues = true)
  private boolean showBackToTopButton;

  @Getter
  @Inject
  @Default(values = "Back to top")
  private String backToTopButtonLabel;

  @Getter
  private BlogPostTableOfContentsItemComponent rootItem;

  @Getter
  private String hierarchyErrorMessage;

  @SlingObject
  private ResourceResolver resourceResolver;
  @SlingObject
  private Resource resource;

  @PostConstruct
  private void init() {
    Resource pageResource = getContainingPageAsResource(this.resource);

    if (pageResource != null) {
      Resource contentsContainer = resourceResolver.getResource(
          pageResource.getPath() + BLOG_CONTENT_CONTAINER_REL_PATH);
      List<Resource> titles = findAllResourcesInContainerByType(
          contentsContainer, TITLE_RESOURCE_PATH);

      //  construct items hierarchy with a stub root element
      hierarchyErrorMessage = "";
      ListIterator<Resource> titlesIter = titles.listIterator();
      this.rootItem = new BlogPostTableOfContentsItemComponent(
          "", "", "",
          getChildTitles(titlesIter, maxHeadingLevel - 1)
      );
    }

    System.out.print(this.rootItem);
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
      ListIterator<Resource> titlesIter, int requiredLevel) {
    if (titlesIter.hasNext()) {

      Resource titleResource = titlesIter.next();
      TitleComponent titleComponent = titleResource.adaptTo(TitleComponent.class);

      if (titleComponent != null) {
        String titleValue = titleComponent.getRawText();
        String anchorIdValue = titleComponent.getAnchorId();
        String headingLevelValue = titleComponent.getElement();

        if (headingLevelValue != null && !"p".equals(headingLevelValue)) {
          int headingLevel = Integer.parseInt(headingLevelValue.substring(1));
          if (headingLevel == requiredLevel) {
            List<BlogPostTableOfContentsItemComponent> children = getChildTitles(
                titlesIter, headingLevel);
            return new BlogPostTableOfContentsItemComponent(
                titleValue, anchorIdValue, headingLevelValue, children);
          } else {
            if (StringUtils.isEmpty(hierarchyErrorMessage)) {
              hierarchyErrorMessage = String.format(
                  "Wrong headers hierarchy: expected %s, got %s at header %s",
                  requiredLevel, headingLevel, titlesIter.nextIndex() - 1);
            }
            //  throw exception for getChildItems() to decide whether it is a child with wrong level
            //  or probably a top-level item (we should process this item, not skip it)
            throw new BlogPostTableOfContentsHierarchyException(requiredLevel, headingLevel);
          }
        }
      }
    }

    return null;
  }

  private List<BlogPostTableOfContentsItemComponent> getChildTitles(
      ListIterator<Resource> titlesIter, int parentLevel) {

    List<BlogPostTableOfContentsItemComponent> children = new ArrayList<>();

    while (titlesIter.hasNext()) {
      try {
        BlogPostTableOfContentsItemComponent item = prepareNewContentItem(
            titlesIter, parentLevel + 1);
        if (item != null) {
          children.add(item);
        }
      } catch (BlogPostTableOfContentsHierarchyException e) {
        if (e.getLevelActual() < e.getLevelExpected()) {
          //  return to the item as it may be a next item in parent level sequence
          titlesIter.previous();
          break;
        } else {
          //  just skip this element as it doesn't fit into hierarchy
        }
      }
    }

    return children;
  }

  private List<Resource> findAllResourcesInContainerByType(
      Resource container, String resourceType) {
    List<Resource> result = new ArrayList<>();

    if (container != null) {
      for (Resource content : container.getChildren()) {
        if (content.isResourceType(resourceType)) {
          result.add(content);
        }
      }
    }

    return result;
  }
}
