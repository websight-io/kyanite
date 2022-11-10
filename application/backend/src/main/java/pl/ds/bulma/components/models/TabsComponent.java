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

package pl.ds.bulma.components.models;

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
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)

public class TabsComponent {

  public static final String TAB_TYPE = "bulma/components/tabs/tab";

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

  @SlingObject
  private Resource resource;

  @Inject
  @Getter
  private List<TabComponent> tabs;

  @Inject
  @Getter
  private String[] tabClasses;

  @Inject
  @Getter
  private boolean content;

  @PostConstruct
  private void init() {
    for (Resource child : resource.getChildren()) {
      String resourceType = child.getResourceType();
      if (TAB_TYPE.equals(resourceType)) {
        content = true;
        break;
      }
    }

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
}
