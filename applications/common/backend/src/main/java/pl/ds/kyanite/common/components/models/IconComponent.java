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

package pl.ds.kyanite.common.components.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class IconComponent {

  public static final String COMMON_ICON_CONTAINERSIZE_DEFAULTSIZEMAPPINGS
      = "kyanite/common/components/common/icon/containersize/defaultsizemappings";
  @Inject
  @Getter
  @Default(values = "mdi-home-outline")
  private String icon;

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

  @ValueMapValue
  @Getter
  @Default(values = "false")
  private String hidden;

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
    this.containerSize
        = calculateContainerSize(this.iconLibType, this.iconSize);
  }

  public String calculateContainerSize(String iconLibType, String iconSize) {
    if (iconLibType != null && !iconLibType.isEmpty()) {
      ValueMap containerSizeMapping = getContainerSizeMapping(
          COMMON_ICON_CONTAINERSIZE_DEFAULTSIZEMAPPINGS + "/" + iconLibType);

      Object mappedContainerSize = containerSizeMapping.get(iconSize);
      if (mappedContainerSize != null) {
        return mappedContainerSize.toString();
      }
    }
    return StringUtils.EMPTY;
  }

  private ValueMap getContainerSizeMapping(String resourcePath) {
    if (resource != null) {
      ResourceResolver resourceResolver = resource.getResourceResolver();
      Resource iconContainerDefaultSizeMapping = resourceResolver
          .getResource(resourcePath);

      if (iconContainerDefaultSizeMapping != null) {
        return iconContainerDefaultSizeMapping.getValueMap();
      }
    }
    return ValueMap.EMPTY;
  }

}
