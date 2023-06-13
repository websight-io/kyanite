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
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.bulma.components.helpers.IconContainerService;
import pl.ds.bulma.components.helpers.IconService;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class IconComponent {

  @Inject
  @Getter
  @Default(values = "home")
  private String icon;

  @Getter
  private String mappedIcon;

  @Inject
  @Getter
  private boolean addText;

  @Inject
  @Getter
  private String label;

  @Inject
  @Getter
  @Default(values = "span")
  private String elementType;

  @Inject
  @Getter
  private String textVariant;

  @Inject
  @Getter
  private boolean colorOnText;

  @Inject
  @Getter
  @Default(values = "mdi")
  private String iconLibType;

  @Getter
  private String containerSize;

  @Inject
  @Getter
  @Default(values = "mdi-36px")
  private String iconSize;

  @SlingObject
  private Resource resource;

  @PostConstruct
  private void init() {
    IconContainerService iconContainerService = new IconContainerService(this.resource);
    String mappingPath = "bulma/components/common/icon/containersize/defaultsizemappings";

    this.containerSize
            = iconContainerService.calculateContainerSize(this.iconLibType,
            mappingPath, this.iconSize);

    IconService iconService = new IconService(resource);
    String iconMappingPath = "bulma/components/common/icon/icons/mappings";

    this.mappedIcon
            = iconService.getIconIdByIconLibType(this.iconLibType,
            iconMappingPath, this.icon);
  }

}
