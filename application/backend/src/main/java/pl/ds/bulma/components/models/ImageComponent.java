/*
 * Copyright (C) 2022 Dynamic Solutions
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

package pl.ds.bulma.components.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.bulma.components.utils.LinkUtil;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ImageComponent {

  @Inject
  private String src;

  @Inject
  private String assetReference;

  @Inject
  @Getter
  private String type;

  @Inject
  @Getter
  private String style;

  @Inject
  @Getter
  private String isRounded;

  @Inject
  @Getter
  private String alt;

  @SlingObject
  private Resource resource;

  @PostConstruct
  private void init() {
    if (assetReference == null && src != null) {
      assetReference = src;
    }
  }

  public String getSrc() {
    return LinkUtil.handleLink(src, resource.getResourceResolver());
  }

  public String getAssetReference() {
    return LinkUtil.handleLink(assetReference, resource.getResourceResolver());
  }
}
