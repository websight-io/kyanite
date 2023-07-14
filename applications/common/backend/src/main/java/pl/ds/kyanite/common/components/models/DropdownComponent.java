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

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DropdownComponent {

  @Inject
  @Getter
  @Default(values = "Label")
  private String label;

  @Inject
  private boolean isHoverable;

  @Inject
  private boolean isRight;

  @Inject
  @Getter
  private boolean isUp;

  @Inject
  @Getter
  private List<DropdownElementComponent> items;

  @Inject
  @Getter
  private String[] dropdownClasses;

  @PostConstruct
  private void init() {
    List<String> classes = new ArrayList<>();
    if (isHoverable) {
      classes.add("is-hoverable");
    }
    if (isRight) {
      classes.add("is-right");
    }
    if (isUp) {
      classes.add("is-up");
    }
    dropdownClasses = classes.toArray(new String[]{});
  }
}
