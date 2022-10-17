/*
 * <!--
 *     Copyright (C) 2022 Dynamic Solutions
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 * -->
 */

package pl.ds.bulma.components.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import pl.ds.bulma.components.models.columns.ResponsiveColumnStyle;

class ColumnClassProviderTest {

  private final ColumnClassProvider columnClassProvider = new ColumnClassProvider();

  @Test
  void getClassesWhenColumnStyleMapContainsNarrowColumnTest() {
    Map<String, ResponsiveColumnStyle> columnStyleMap = new HashMap<>();

    ResponsiveColumnStyle mobileColumnStyle = new ResponsiveColumnStyle();
    ResponsiveColumnStyle tabletColumnStyle = new ResponsiveColumnStyle();
    tabletColumnStyle.setNormalColumn(true);
    tabletColumnStyle.setSizeType("fraction");
    tabletColumnStyle.setSize("half");
    tabletColumnStyle.setOffsetType("evenNumber");
    tabletColumnStyle.setOffset("two");

    columnStyleMap.put("mobile", mobileColumnStyle);
    columnStyleMap.put("tablet", tabletColumnStyle);

    String[] resultArray = columnClassProvider.getClasses(columnStyleMap);

    assertThat(resultArray).containsExactlyInAnyOrder("is-narrow-mobile", "is-half-tablet",
        "is-offset-2-tablet");
  }

  @Test
  void getClassesWhenColumnStyleMapContainsEmptySizeAndOffsetTest() {
    Map<String, ResponsiveColumnStyle> columnStyleMap = new HashMap<>();

    ResponsiveColumnStyle mobileColumnStyle = new ResponsiveColumnStyle();
    ResponsiveColumnStyle tabletColumnStyle = new ResponsiveColumnStyle();
    tabletColumnStyle.setNormalColumn(true);
    tabletColumnStyle.setSizeType("fraction");
    tabletColumnStyle.setSize("");
    tabletColumnStyle.setOffsetType("evenNumber");
    tabletColumnStyle.setOffset("");

    columnStyleMap.put("mobile", mobileColumnStyle);
    columnStyleMap.put("tablet", tabletColumnStyle);

    String[] resultArray = columnClassProvider.getClasses(columnStyleMap);

    assertThat(resultArray).containsExactly("is-narrow-mobile");
  }
}
