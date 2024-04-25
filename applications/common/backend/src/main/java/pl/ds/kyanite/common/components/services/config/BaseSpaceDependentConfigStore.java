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

package pl.ds.kyanite.common.components.services.config;

import org.apache.sling.api.resource.Resource;
import pl.ds.kyanite.common.components.utils.PagesSpaceUtil;

/**
 * Config store retrieving space dependent configurations by space name.
 *
 * @param <T> class implementing SpaceDependentConfiguration.
 *           Space name from the configuration serves as ID in this case.
 *           Space name or a resource consuming the configuration is resolved by that resource path
 */

public abstract class BaseSpaceDependentConfigStore<T extends SpaceDependentConfiguration>
    extends     BaseConfigStore<T>
    implements  SpaceDependentConfigStore<T> {

  @Override
  public T get(Resource resource) {
    String spaceName = PagesSpaceUtil.getWsPagesSpaceName(resource.getPath(),
        resource.getResourceResolver());
    return get(spaceName);
  }
}
