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

package pl.ds.kyanite.common.components.models.navbar;

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
public class NavbarMegaDropdownComponent {

  @Inject
  @Getter
  @Default(values = "Label")
  private String dropdownLabel;

  @Inject
  @Getter
  private boolean isDropup;

  @Inject
  @Getter
  @Default(booleanValues = true)
  private boolean hasArrow;


  @Inject
  @Getter
  private boolean isBoxed;

  @Inject
  @Getter
  private List<MegaDropdownColumnComponent> dropdownColumns = new ArrayList<>();

  @PostConstruct
  private void init() {
    int consecutiveHiddenCount = 0;
    for (int i = 1; i < dropdownColumns.size(); i++) {
      MegaDropdownColumnComponent currentColumn = dropdownColumns.get(i);
      if (currentColumn.isHideColumn()) {
        consecutiveHiddenCount++;

        MegaDropdownColumnComponent lastNotHidden = dropdownColumns.get(i - consecutiveHiddenCount);
        lastNotHidden.setColSizeClass("column-" + (consecutiveHiddenCount + 1));
      } else {
        consecutiveHiddenCount = 0;
      }
    }
  }
}
