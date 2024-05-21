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

package pl.ds.kyanite.common.components.models.grid;

import static pl.ds.kyanite.common.components.models.grid.GridCellComponent.GRID_CELL_RESOURCE_TYPE;
import static pl.ds.kyanite.common.components.utils.SlingUtils.SLING_RESOURCE_TYPE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.kyanite.common.components.models.layouts.MultiTemplateComponent;

@Model(
    adaptables = { Resource.class },
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class GridComponent implements MultiTemplateComponent {

  @Inject
  private ResponsiveGridStyle desktopGridStyle;

  @Inject
  private ResponsiveGridStyle tabletGridStyle;

  @Inject
  private ResponsiveGridStyle mobileGridStyle;

  private Map<String, ResponsiveGridStyle> responsiveGridStyles;

  @Inject
  @Getter
  @Default(floatValues = 1.0f)
  private float columnGap;

  @Inject
  @Getter
  @Default(floatValues = 1.0f)
  private float rowGap;

  @Getter
  private String[] commonGridClasses = new String[0];

  @Getter
  private String[] fixedGridClasses = new String[0];

  @SlingObject
  private Resource resource;

  @PostConstruct
  private void init() {
    responsiveGridStyles = new HashMap<>();
    responsiveGridStyles.put("desktop", desktopGridStyle);
    responsiveGridStyles.put("tablet",  tabletGridStyle);
    responsiveGridStyles.put("mobile",  mobileGridStyle);

    calcCommonGridClasses();
    calcFixedGridClasses();
  }

  private void calcCommonGridClasses() {
    List<String> commonGridClassList = new ArrayList<>();

    commonGridClassList.add(String.format("is-column-gap-%s", formatFloat(columnGap)));
    commonGridClassList.add(String.format("is-row-gap-%s", formatFloat(rowGap)));

    commonGridClasses = commonGridClassList.toArray(new String[]{});
  }

  private void calcFixedGridClasses() {
    List<String> fixedGridClassList = new ArrayList<>();

    //  set default columns number that will be overwritten by viewport-specific classes
    final int defaultColumnsNumber = 4;
    fixedGridClassList.add(String.format("has-%s-cols", defaultColumnsNumber));
    responsiveGridStyles.forEach((viewportBulmaName, style) -> {
      if (style != null) {
        int columnsNumber = style.getColumnsNumber();
        if (columnsNumber > 0) {
          fixedGridClassList.add(String.format("has-%s-cols-%s", columnsNumber, viewportBulmaName));
        }
      }
    });

    fixedGridClasses = fixedGridClassList.toArray(new String[]{});
  }

  private String formatFloat(float f) {
    if (f == (long) f) {
      return String.format("%d", (long) f);
    } else {
      return String.format("%.1f", f);
    }
  }

  public List<Resource> getChildrenComponents() {
    return StreamSupport
        .stream(resource.getChildren().spliterator(), false)
        .filter(it -> GRID_CELL_RESOURCE_TYPE.equals(it.getValueMap().get(SLING_RESOURCE_TYPE)))
        .collect(Collectors.toList());
  }

}
