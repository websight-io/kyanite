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
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.factory.ModelFactory;

@Model(
    adaptables = {Resource.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ThumbnailModel {

  @SlingObject
  private Resource resource;

  @OSGiService
  private ModelFactory modelFactory;

  @Getter
  private BlogArticleHeaderModel article;

  @PostConstruct
  private void init() {
    final String pagePath = resource.getPath() + "/jcr:content";
    final Resource currentPage = resource.getResourceResolver().getResource(pagePath);
    if (Objects.nonNull(currentPage)) {
      article = modelFactory.createModel(currentPage, BlogArticleHeaderModel.class);
    }
  }

}
