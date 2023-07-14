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

import java.util.Objects;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.kyanite.common.components.utils.LinkUtil;
import pl.ds.websight.pages.core.api.Page;
import pl.ds.websight.pages.core.api.PageManager;

@Model(
    adaptables = {Resource.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class BlogPostBackButtonComponent {
  public static final String JCR_CONTENT = "/jcr:content";
  private final ResourceResolver resourceResolver;
  private final Resource resource;

  @Getter
  private String link;

  @Inject
  public BlogPostBackButtonComponent(@SlingObject ResourceResolver resourceResolver,
      @SlingObject Resource resource) {
    this.resourceResolver = resourceResolver;
    this.resource = resource;
  }

  @PostConstruct
  private void init() {
    PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
    if (Objects.nonNull(pageManager)) {
      Page page = pageManager.getPage(StringUtils.substringBefore(resource.getPath(), JCR_CONTENT));
      Page blogListingPage = this.findBlogListingPage(page);
      this.link = Optional.ofNullable(blogListingPage)
          .map(elem -> LinkUtil.handleLink(elem.getPath(), resourceResolver))
          .orElse(StringUtils.EMPTY);
    }
  }

  private Page findBlogListingPage(Page page) {
    if (Objects.isNull(page)) {
      return null;
    }
    Page parentPage = page.getParent();
    if (Objects.nonNull(parentPage)) {
      Resource contentResource = parentPage.getContentResource();
      String template = Optional.ofNullable(contentResource)
          .map(Resource::getValueMap)
          .map(valueMap -> valueMap.get("ws:template", String.class))
          .orElse(StringUtils.EMPTY);
      if ("/libs/kyanite/templates/bloglistingpage".equals(template)) {
        return parentPage;
      }
    }
    return findBlogListingPage(parentPage);
  }

}

