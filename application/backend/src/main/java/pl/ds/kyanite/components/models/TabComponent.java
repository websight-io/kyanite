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

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.kyanite.components.helpers.IconContainerService;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class TabComponent {

  @Inject
  @Getter
  @Default(values = StringUtils.EMPTY)
  private String label;

  @Inject
  @Getter
  private boolean addIcon;

  @Inject
  @Getter
  @Default(values = "mdi-home-outline")
  private String icon;

  @Inject
  @Getter
  @Default(values = "mdi")
  private String iconLibType;

  @Inject
  @Getter
  @Default(values = "mdi-36px")
  private String iconSize;

  @Getter
  private String containerSize;

  @SlingObject
  private Resource resource;


  @PostConstruct
  private void init() {
    IconContainerService iconContainerService = new IconContainerService(this.resource);
    String mappingPath = "kyanite/components/common/icon/containersize/defaultsizemappings";

    this.containerSize
            = iconContainerService.calculateContainerSize(this.iconLibType,
            mappingPath, this.iconSize);
  }
}
