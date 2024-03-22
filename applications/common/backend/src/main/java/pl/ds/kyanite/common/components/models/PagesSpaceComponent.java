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
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.kyanite.common.components.utils.PagesSpaceUtil;

@Model(adaptables = Resource.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PagesSpaceComponent {

  @SlingObject
  private Resource resource;

  @Getter
  private String stylesPath;

  @PostConstruct
  private void init() {
    stylesPath = initStylesPath();
  }

  private String initStylesPath() {
    Resource pageSpace = PagesSpaceUtil.getSpace(resource.getPath(),
        resource.getResourceResolver());
    if (pageSpace != null) {
      String templatePath = pageSpace.getValueMap().get("ws:template", StringUtils.EMPTY);
      Resource templateResource = resource.getResourceResolver().getResource(templatePath);
      if (templateResource != null) {
        return templateResource.getValueMap().get("stylesPath", StringUtils.EMPTY);
      }
    }
    return StringUtils.EMPTY;
  }
}
