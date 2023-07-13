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

package pl.ds.kyanite.components.models;

import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.websight.pages.core.api.Page;
import pl.ds.websight.pages.core.api.PageManager;


@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class BlogPostTableOfContentsComponent {
  private static final String BLOG_CONTENT_CONTAINER_REL_PATH =
      "/jcr:content/pagecontainer/section/container/columns1/column4";
  private static final String TITLE_RESOURCE_PATH = "kyanite/components/title";

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
  private List<BlogPostTableOfContentsItemComponent> items;

  @SlingObject
  private ResourceResolver resourceResolver;
  @SlingObject
  private Resource resource;

  @PostConstruct
  private void init() {
    this.items = new ArrayList<>();

    Resource pageResource = getContainingPageAsResource(this.resource);

    if (pageResource != null) {
      Resource contentsContainer = resourceResolver.getResource(
          pageResource.getPath() + BLOG_CONTENT_CONTAINER_REL_PATH);
      List<Resource> titles = findAllResourcesInContainerByType(
          contentsContainer, TITLE_RESOURCE_PATH);

      for (Resource contentTitle : titles) {
        TitleComponent titleComponent = contentTitle.adaptTo(TitleComponent.class);

        if (!titleComponent.getElement().equals("p")) {
          BlogPostTableOfContentsItemComponent contentItem =
              prepareNewContentItem(titleComponent);
          if (contentItem != null) {
            this.items.add(contentItem);
          }
        }
      }
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

      if (headingLevelValue != null
          && Integer.parseInt(headingLevelValue.substring(1)) >= this.maxHeadingLevel) {
        return new BlogPostTableOfContentsItemComponent(
            titleValue, anchorIdValue, headingLevelValue);
      }
    }

    return null;
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
