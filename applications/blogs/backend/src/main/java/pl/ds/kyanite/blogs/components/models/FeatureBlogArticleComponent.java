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

import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.factory.ModelFactory;
import pl.ds.kyanite.blogs.components.services.BlogArticleService;
import pl.ds.kyanite.common.components.utils.LinkUtil;
import pl.ds.kyanite.common.components.utils.PageUtil;
import pl.ds.websight.pages.core.api.Page;
import pl.ds.websight.pages.core.api.PageManager;

@Model(
    adaptables = {Resource.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FeatureBlogArticleComponent {

  public static final String CONTENT = "/content";
  public static final String JCR_CONTENT = "/jcr:content";
  private final ResourceResolver resourceResolver;

  private final BlogArticleService blogArticleService;

  @ValueMapValue
  @Getter
  @Default(values = CONTENT)
  private String link;

  @ValueMapValue
  @Getter
  private boolean isReversed;

  @Getter
  private BlogArticleHeaderModel blogArticleHeader;

  @Getter
  private String blogArticleHeaderLink;

  private final ModelFactory modelFactory;
  private final Resource resource;
  private final PageManager pageManager;

  @Inject
  public FeatureBlogArticleComponent(@SlingObject ResourceResolver resourceResolver,
      @SlingObject Resource resource,
      @OSGiService BlogArticleService blogArticleService, @OSGiService ModelFactory modelFactory) {
    this.resourceResolver = resourceResolver;
    this.blogArticleService = blogArticleService;
    this.modelFactory = modelFactory;
    this.pageManager = resourceResolver.adaptTo(PageManager.class);
    this.resource = resource;
  }

  @PostConstruct
  private void init() {
    final String pagePath =
        StringUtils.substringBefore(resource.getPath(), JCR_CONTENT);
    final String linkToFind = StringUtils.isNotBlank(link) && !link.equals(CONTENT) ? link
        : PageUtil.getCurrentPageSpace(pagePath);
    final Resource blogPage = this.getArticlePage(linkToFind);
    if (Objects.nonNull(blogPage)) {
      this.blogArticleHeader = modelFactory.createModel(blogPage, BlogArticleHeaderModel.class);
      this.blogArticleHeaderLink = LinkUtil.handleLink(
          StringUtils.substringBefore(blogPage.getPath(), JCR_CONTENT), resourceResolver);
    }
  }


  private Resource getArticlePage(String linkToFind) {
    final Page page = pageManager.getPage(linkToFind);
    final List<Resource> blogArticlePages;
    if (Objects.nonNull(page)) {
      Resource pageResource = page.getContentResource();
      if (this.blogArticleService.isBlogArticlePage(pageResource)) {
        return pageResource;
      }
      blogArticlePages = this.blogArticleService.getListBlogArticlePages(page);
    } else {
      blogArticlePages = this.blogArticleService.getListBlogArticlePages(linkToFind,
          resourceResolver);
    }
    return blogArticlePages
        .stream()
        .findFirst()
        .orElse(null);
  }

}

