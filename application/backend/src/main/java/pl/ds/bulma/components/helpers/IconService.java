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

package pl.ds.bulma.components.helpers;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import pl.ds.bulma.components.services.SvgImageService;

@Component (service = IconService.class)
public class IconService {

  public static final String ICON_MAPPING_PATH = "bulma/components/common/icon/icons/mappings";

  public static final String MAPPING_PATH =
      "bulma/components/common/icon/containersize/defaultsizemappings";

  public String getIconIdByIconLibType(String iconLibType, String icon,
      ResourceResolver resourceResolver) {

    if (iconLibType != null && !iconLibType.isEmpty()) {
      ValueMap iconMapping = getMapping(ICON_MAPPING_PATH + "/" + iconLibType, resourceResolver);

      Object mappedIcon = iconMapping.get(icon);
      if (mappedIcon != null) {
        return mappedIcon.toString();
      }
    }

    return icon;
  }

  private ValueMap getMapping(String resourcePath, ResourceResolver resourceResolver) {
    if (StringUtils.isNotBlank(resourcePath)) {
      Resource iconMapping = resourceResolver
          .getResource(resourcePath);

      if (iconMapping != null) {
        return iconMapping.getValueMap();
      }
    }
    return ValueMap.EMPTY;
  }

  public String calculateContainerSize(String iconLibType, String iconSize,
      ResourceResolver resourceResolver) {

    if (iconLibType != null && !iconLibType.isEmpty()) {
      ValueMap containerSizeMapping = getMapping(
          MAPPING_PATH + "/" + iconLibType, resourceResolver);

      Object mappedContainerSize = containerSizeMapping.get(iconSize);
      if (mappedContainerSize != null) {
        return mappedContainerSize.toString();
      }
    }

    return StringUtils.EMPTY;
  }

}
