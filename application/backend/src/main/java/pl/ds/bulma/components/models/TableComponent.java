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

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TableComponent {

  @Inject
  private boolean isBordered;

  @Inject
  private boolean isStriped;

  @Inject
  private boolean isNarrow;

  @Inject
  private boolean isHoverable;

  @Inject
  private boolean isFullwidth;

  @Inject
  @Getter
  private String[] tableClasses;

  @PostConstruct
  private void init() {
    List<String> classes = new ArrayList<>();
    if (isBordered) {
      classes.add("is-bordered");
    }
    if (isStriped) {
      classes.add("is-striped");
    }
    if (isNarrow) {
      classes.add("is-narrow");
    }
    if (isHoverable) {
      classes.add("is-hoverable");
    }
    if (isFullwidth) {
      classes.add("is-fullwidth");
    }
    tableClasses = classes.toArray(new String[]{});
  }
}
