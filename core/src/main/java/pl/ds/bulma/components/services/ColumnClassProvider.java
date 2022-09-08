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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Component;
import pl.ds.bulma.components.models.columns.ColumnSizes;
import pl.ds.bulma.components.models.columns.ResponsiveColumnStyle;

@Component(service = ColumnClassProvider.class)
public class ColumnClassProvider {

  public String[] getClasses(Map<String, ResponsiveColumnStyle> columnStyleMap) {
    List<String> classList = new ArrayList<>();
    columnStyleMap.forEach((key, columnStyle) -> {
      if (columnStyle.isNormalColumn()) {
        classList.add("is-narrow-" + key);
      } else {
        String sizeClass = createSizeClass(
            ColumnSizes.findByName(columnStyle.getSize()).getCssClass());
        addToClassListIfNotNull(classList, key, sizeClass);

        String offsetClass = createOffsetClass(
            ColumnSizes.findByName(columnStyle.getOffset()).getCssClass());
        addToClassListIfNotNull(classList, key, offsetClass);
      }
    });

    return classList.isEmpty() ? new String[]{} : classList.toArray(new String[]{});
  }

  private void addToClassListIfNotNull(List<String> classList, String key, String cssClass) {
    if (cssClass != null) {
      classList.add(cssClass + "-" + key);
    }
  }

  private String createSizeClass(String classPart) {
    return StringUtils.EMPTY.equals(classPart) ? null : "is-" + classPart;
  }

  private String createOffsetClass(String classPart) {
    return StringUtils.EMPTY.equals(classPart) ? null : "is-offset-" + classPart;
  }

}
