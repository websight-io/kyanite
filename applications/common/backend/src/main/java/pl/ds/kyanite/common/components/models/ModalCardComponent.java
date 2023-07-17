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
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ModalCardComponent extends ModalComponent {

  public static final String ID_PREFIX = "modalcard";

  public static final String MODEL_CARD_FOOTER_RESOURCE_TYPE
      = "kyanite/common/components/modelcard/modelcardfooter";

  @ValueMapValue
  @Getter
  @Default(values = StringUtils.EMPTY)
  private String header;

  @Getter
  private boolean hasFooter;

  @PostConstruct
  void init() {
    for (Resource child : resource.getChildren()) {
      String resourceType = child.getResourceType();
      if (MODEL_CARD_FOOTER_RESOURCE_TYPE.equals(resourceType) && child.hasChildren()) {
        hasFooter = true;
        break;
      }
    }
  }

  public String getId() {
    return StringUtils.isNotBlank(id) ? id : idService.getStoredId(resource, ID_PREFIX);
  }
}
