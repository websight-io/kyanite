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
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LevelItemComponent {

  @SlingObject
  private Resource resource;

  @Inject
  @Getter
  @Default(values = "div")
  private String elementType;

  @Inject
  @Default(values = "is-left")
  private String alignment;

  public String getAlignment() {
    if (alignment.equals("is-left")) {
      return "is-justify-content-flex-start has-text-left";
    }
    if (alignment.equals("is-right")) {
      return "is-justify-content-flex-end has-text-right";
    }
    return "has-text-centered";
  }
}
