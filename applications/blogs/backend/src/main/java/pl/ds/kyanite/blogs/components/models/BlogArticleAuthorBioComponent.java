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

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.kyanite.blogs.components.exceptions.AuthorInfoResolvingException;
import pl.ds.kyanite.blogs.components.services.AuthorInfoResolverService;

@Model(
    adaptables = {Resource.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class BlogArticleAuthorBioComponent {

  @ChildResource
  private Resource author;

  private final ResourceResolver resourceResolver;

  private final AuthorInfoResolverService authorInfoResolver;

  @Getter
  private AuthorInfoModel authorInfoModel;

  @Getter
  private String authorInfoErrMessage;

  @Inject
  public BlogArticleAuthorBioComponent(
      @SlingObject ResourceResolver resourceResolver,
      @OSGiService AuthorInfoResolverService authorInfoResolver
  ) {
    this.resourceResolver = resourceResolver;
    this.authorInfoResolver = authorInfoResolver;
  }

  @PostConstruct
  private void init() {
    try {
      authorInfoModel = authorInfoResolver.retrieveAuthorInfo(author, resourceResolver);
    } catch (AuthorInfoResolvingException e) {
      authorInfoErrMessage = e.getMessage();
    }
  }
}

