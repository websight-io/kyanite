/*
 * Copyright (C) 2022 Dynamic Solutions
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

package pl.ds.kyanite.common.components.models.panel;

import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.kyanite.common.components.services.ComponentIdService;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class PanelBlockComponent {

  public static final String ID_PREFIX = "panel_block";

  @OSGiService
  private ComponentIdService idService;

  @SlingObject
  private Resource resource;

  @Inject
  @Getter
  private boolean addIcon;

  @Inject
  @Getter
  @Default(values = "mdi-home-outline")
  private String icon;

  public String getId() {
    return idService.getTemporaryId(ID_PREFIX);
  }

}
