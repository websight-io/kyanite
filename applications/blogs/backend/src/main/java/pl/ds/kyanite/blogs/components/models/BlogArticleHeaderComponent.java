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
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.factory.ModelFactory;
import pl.ds.kyanite.blogs.components.exceptions.AuthorInfoResolvingException;
import pl.ds.kyanite.blogs.components.services.AuthorInfoResolverService;
import pl.ds.kyanite.blogs.components.utils.ResourceUtil;

@Model(
    adaptables = {Resource.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class BlogArticleHeaderComponent {

  private final Resource resource;

  private final ResourceResolver resourceResolver;

  private final ModelFactory modelFactory;

  @Getter
  private BlogArticleHeaderModel blogArticleHeaderModel;

  @Getter
  private AuthorInfoModel authorInfoModel;

  @Getter
  private String authorInfoErrMessage;

  private final AuthorInfoResolverService authorInfoResolver;

  public static final String JCR_CONTENT = "/jcr:content";

  @Inject
  public BlogArticleHeaderComponent(
      @SlingObject Resource resource,
      @SlingObject ResourceResolver resourceResolver,
      @OSGiService ModelFactory modelFactory,
      @OSGiService AuthorInfoResolverService authorInfoResolver
  ) {
    this.resource           = resource;
    this.resourceResolver   = resourceResolver;
    this.modelFactory       = modelFactory;
    this.authorInfoResolver = authorInfoResolver;
  }

  @PostConstruct
  private void init() {
    resolveHeaderModel();
    resolveAuthorInfo();
  }

  private void resolveHeaderModel() {
    final Resource currentPage = ResourceUtil.getParentPageResource(resource, resourceResolver);
    if (Objects.nonNull(currentPage)) {
      final Resource pageContent = ResourceUtil.getContentNode(currentPage);
      blogArticleHeaderModel = modelFactory.createModel(pageContent, BlogArticleHeaderModel.class);
    }
  }

  private void resolveAuthorInfo() {
    try {
      authorInfoModel = authorInfoResolver.retrieveAuthorInfo(resource, resourceResolver);
    } catch (AuthorInfoResolvingException e) {
      authorInfoErrMessage = e.getMessage();
    }
  }

}

