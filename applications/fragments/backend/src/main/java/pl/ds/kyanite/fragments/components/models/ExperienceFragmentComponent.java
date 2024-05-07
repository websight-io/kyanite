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

package pl.ds.kyanite.fragments.components.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.kyanite.fragments.api.models.ExperienceFragment;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ExperienceFragmentComponent implements ExperienceFragment {

  @Inject
  @Getter
  private String resource;

  @Getter
  private boolean validPage = false;

  @Getter
  private String pagePath;

  @SlingObject
  private ResourceResolver resourceResolver;

  @PostConstruct
  private void init() {
    validPage = checkIfValidPage();
    pagePath = preparePagePath(resource);
  }

  private boolean checkIfValidPage() {
    Resource pageResource = resourceResolver.getResource(resource);
    if (pageResource != null) {
      ValueMap valueMap = pageResource.getValueMap();
      String primaryType = valueMap.get("jcr:primaryType", String.class);
      return "ws:Page".equals(primaryType);
    }
    return false;
  }
}
