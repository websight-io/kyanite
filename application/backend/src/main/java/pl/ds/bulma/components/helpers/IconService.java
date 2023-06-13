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

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;

public class IconService {

  private Resource resource;

  public IconService(Resource resource) {
    this.resource = resource;
  }

  public String getIconIdByIconLibType(String iconLibType,
                                        String mappingPath, String icon) {

    if (iconLibType != null && !iconLibType.isEmpty()) {
      ValueMap iconMapping = getIconMapping(
              mappingPath + "/" + iconLibType);

      Object mappedIcon = iconMapping.get(icon);
      if (mappedIcon != null) {
        return mappedIcon.toString();
      }
    }

    return "";
  }

  private ValueMap getIconMapping(String resourcePath) {
    if (resource != null) {
      ResourceResolver resourceResolver = resource.getResourceResolver();
      Resource iconMapping = resourceResolver
              .getResource(resourcePath);

      if (iconMapping != null) {
        return iconMapping.getValueMap();
      }
    }

    return ValueMap.EMPTY;
  }

}
