/*
 * Copyright (C) 2024 Dynamic Solutions
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

import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.kyanite.common.components.models.layouts.MultiTemplateComponent;
import pl.ds.kyanite.common.components.utils.LinkUtil;

@Model(
    adaptables = { Resource.class },
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class QuoteComponent implements Quote, MultiTemplateComponent {

  @Inject
  @Getter
  private String quoteText;

  @Inject
  @Getter
  private String authorName;

  @Inject
  @Getter
  private String authorCompany;

  @Inject
  private String authorPhotoPath;

  private final ResourceResolver resourceResolver;

  @Inject
  public QuoteComponent(
      @SlingObject ResourceResolver resourceResolver
  ) {
    this.resourceResolver = resourceResolver;
  }

  @Override
  public String getAuthorPhoto() {
    return LinkUtil.handleLink(authorPhotoPath, resourceResolver);
  }

}
