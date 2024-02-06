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
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContainerComponent {

  @Inject
  @Getter
  @Default(values = StringUtils.EMPTY)
  private String containerStyle;

  @Inject
  @Getter
  @Default(values = StringUtils.EMPTY)
  private String alignmentHorizontal;

  @Inject
  @Getter
  private String[] containerClasses;

  @PostConstruct
  private void init() {
    List<String> classes = new ArrayList<>();

    if (StringUtils.isNotEmpty(containerStyle)) {
      classes.add(containerStyle);
    }

    List<String> classesV1 = getHorizontalAlignmentClasses();
    if (!classesV1.isEmpty()) {
      classes.addAll(classesV1);
    }

    containerClasses = classes.toArray(new String[]{});
  }

  /**
   * Returns a list of Bulma classes for a container to align items in it horizontally.
   * The current solution is:
   *  - items are kept one per line by 'flex-direction: column' css property
   *  - horizontal axis then becomes Y axis for flexbox and therefore is handled by 'align-items'
   */
  public List<String> getHorizontalAlignmentClasses() {
    List<String> cssClasses = new ArrayList<>();
    String flexAlignmentClass = switch (alignmentHorizontal) {
      case "is-left"      -> "is-align-items-flex-start";
      case "is-right"     -> "is-align-items-flex-end";
      case "is-centered"  -> "is-align-items-center";
      default -> "";
    };
    if (StringUtils.isNotEmpty(flexAlignmentClass)) {
      cssClasses.add("is-flex");
      cssClasses.add("is-flex-direction-column");
      cssClasses.add(flexAlignmentClass);
    }
    return cssClasses;
  }
}
