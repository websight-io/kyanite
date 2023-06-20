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

package pl.ds.bulma.components.models;

import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Required;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.bulma.components.helpers.IconService;
import pl.ds.bulma.components.utils.LinkUtil;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class BreadcrumbElementComponent {


  @Inject
  @Getter
  private String label;

  @Inject
  private String url;

  @Inject
  @Getter
  private boolean addIcon;

  @Inject
  @Getter
  private String icon;

  @Getter
  private String mappedIcon;

  @Inject
  @Getter
  private String iconLibType;

  @Inject
  @Getter
  private String iconSize;

  @Getter
  private String containerSize;

  @SlingObject
  private Resource resource;

  @OSGiService
  @Required
  private IconService iconService;

  public String getUrl() {
    return LinkUtil.handleLink(url, resource.getResourceResolver());
  }

  @PostConstruct
  private void init() {
    ResourceResolver resourceResolver = resource.getResourceResolver();
    this.containerSize
        = iconService.calculateContainerSize(this.iconLibType,
        this.iconSize, resourceResolver);
    this.mappedIcon
        = iconService.getIconIdByIconLibType(this.iconLibType,
        this.icon, resourceResolver);
  }
}
