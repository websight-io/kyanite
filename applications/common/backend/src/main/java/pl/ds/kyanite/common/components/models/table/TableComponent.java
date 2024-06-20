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

package pl.ds.kyanite.common.components.models.table;

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

  static final int DEFAULT_SUMMARY_COLSPAN = 12;

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
  private boolean isScrollable;

  @Inject
  private boolean isFirstColumnLocked;

  @Inject
  @Getter
  private String[] tableClasses;

  @Inject
  @Getter
  private String[] tableContainerClasses;

  @PostConstruct
  private void init() {
    List<String> classes = new ArrayList<>();
    List<String> containerClasses = new ArrayList<>();
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
      containerClasses.add("is-table-fullwidth");
    }
    if (isScrollable) {
      containerClasses.add("is-table-scrollable");
    }
    if (isFirstColumnLocked) {
      containerClasses.add("is-table-locked-first-column");
    }
    tableClasses = classes.toArray(new String[]{});
    tableContainerClasses = containerClasses.toArray(new String[] {});
  }
}
