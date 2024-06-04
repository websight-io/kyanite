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

package pl.ds.kyanite.common.components.models.columns;

import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;
import static pl.ds.kyanite.common.components.utils.SlingUtils.SLING_RESOURCE_TYPE;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class ColumnsComponent {

  private static final String COLUMN_COMPONENT_RESOURCE_TYPE
      = "kyanite/common/components/columns/column";

  @SlingObject
  private Resource resource;

  @Getter
  @Inject
  @Default(values = "is-tablet")
  private String columnsActivationLevel;

  @Getter
  @Inject
  private boolean isCustomGapLevel;

  @Getter
  @Inject
  @Default(values = "is-3")
  private String mobileGapLevel;

  @Getter
  @Inject
  @Default(values = "is-3")
  private String tabletGapLevel;

  @Getter
  @Inject
  @Default(values = "is-3")
  private String desktopGapLevel;

  @Getter
  @Inject
  private boolean isMultiline;

  @Getter
  @Inject
  private boolean isVerticallyCentered;

  @Getter
  @Inject
  private boolean isCentered;

  @Getter
  @Inject
  private String[] classes;

  @PostConstruct
  private void init() {
    List<String> classList = new ArrayList<>();
    if (isCustomGapLevel) {
      classList.add("is-variable");
      classList.add(mobileGapLevel + "-mobile");
      classList.add(tabletGapLevel + "-tablet");
      classList.add(desktopGapLevel + "-desktop");
    }

    if (isMultiline) {
      classList.add("is-multiline");
    }

    if (isVerticallyCentered) {
      classList.add("is-vcentered");
    }

    if (isCentered) {
      classList.add("is-centered");
    }

    classList.add(columnsActivationLevel);
    classes = classList.toArray(new String[]{});
  }

  public boolean hasOnlyColumnsWhichHasNoChildren() {
    return StreamSupport
        .stream(resource.getChildren().spliterator(), false)
        .allMatch(it -> isColumn(it) && (!it.hasChildren() || allOfItsChildrenAreColumnStyles(it)));
  }

  private boolean allOfItsChildrenAreColumnStyles(Resource resource) {
    return StreamSupport
        .stream(resource.getChildren().spliterator(), false)
        .allMatch(it -> it.getValueMap().get(SLING_RESOURCE_TYPE) == null);
  }

  private boolean isColumn(Resource resource) {
    return COLUMN_COMPONENT_RESOURCE_TYPE.equals(resource.getResourceType());
  }
}
