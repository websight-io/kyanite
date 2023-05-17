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

package pl.ds.bulma.components.models.level;

import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LevelItemComponent {

  @SlingObject
  private Resource resource;

  @Inject
  @Default(values = "is-left")
  private String alignment;

  @Inject
  @Default(values = "is-centered")
  private String alignmentVertical;

  @ValueMapValue
  @Default(values = StringUtils.EMPTY)
  @Getter
  private String classes;

  public String getAlignment() {
    if (alignment.equals("is-left")) {
      return "is-justify-content-flex-start has-text-left";
    }
    if (alignment.equals("is-right")) {
      return "is-justify-content-flex-end has-text-right";
    }
    return "has-text-centered";
  }

  public String getAlignmentVertical() {
    if (alignmentVertical.equals("is-top")) {
      return "is-align-items-flex-start";
    }
    if (alignmentVertical.equals("is-bottom")) {
      return "is-align-items-flex-end";
    }
    return "is-align-items-center";
  }
}
