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

public class IconContainerService {

  private Resource resource;

  public IconContainerService(Resource resource) {
    this.resource = resource;
  }

  public ValueMap getContainerSizeMapping(String resourcePath) {
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
