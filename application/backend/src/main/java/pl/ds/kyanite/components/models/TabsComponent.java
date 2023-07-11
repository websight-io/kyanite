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

package pl.ds.kyanite.components.models;

import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)

public class TabsComponent {

  @Inject
  @Getter
  @Default(values = StringUtils.EMPTY)
  private String alignment;

  @Inject
  @Getter
  @Default(values = StringUtils.EMPTY)
  private String size;

  @Inject
  @Getter
  private boolean boxed;

  @Inject
  @Getter
  private boolean fullWidth;

  @Inject
  @Getter
  private boolean toggle;

  @Inject
  @Getter
  private boolean toggleRounded;

  @Inject
  @Getter
  private String contentId;

  @Inject
  private boolean autoSwitch;

  @Inject
  @Getter
  @Default(values = "5")
  private String switchingTime;

  @Inject
  @Getter
  private String[] tabClasses;

  @PostConstruct
  private void init() {
    List<String> styles = new ArrayList<>();
    if (StringUtils.isNotEmpty(size)) {
      styles.add(size);
    }
    if (StringUtils.isNotEmpty(alignment)) {
      styles.add(alignment);
    }
    if (boxed) {
      styles.add("is-boxed");
    }
    if (fullWidth) {
      styles.add("is-fullwidth");
    }
    if (toggle) {
      styles.add("is-toggle");
      if (toggleRounded) {
        styles.add("is-toggle-rounded");
      }
    }

    tabClasses = styles.toArray(new String[]{});
  }

  public String getAutoSwitch(){
    return String.valueOf(autoSwitch);
  }
}
