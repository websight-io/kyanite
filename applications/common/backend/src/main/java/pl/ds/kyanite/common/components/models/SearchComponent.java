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

package pl.ds.kyanite.common.components.models;

import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.kyanite.common.components.services.SearchConfigStore;
import pl.ds.kyanite.common.components.services.SearchConfiguration;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SearchComponent {

  @Inject
  @Default(intValues = 40)
  @Getter
  private int pageLimit;

  @OSGiService
  private SearchConfigStore searchConfigStore;

  @SlingObject
  private Resource resource;

  public String getSearchEndpoint() {
    SearchConfiguration configuration = searchConfigStore.get(resource);
    if (configuration != null) {
      return configuration.getSearchEndpoint();
    }
    return null;
  }

  public String getSpaceName() {
    SearchConfiguration configuration = searchConfigStore.get(resource);
    if (configuration != null) {
      return configuration.getSpaceName();
    }
    return null;
  }
}
