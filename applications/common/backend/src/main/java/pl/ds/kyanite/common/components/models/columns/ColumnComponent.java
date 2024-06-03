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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import pl.ds.kyanite.common.components.services.ColumnClassProvider;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class ColumnComponent extends DefaultResponsiveColumnComponent {

  @SlingObject
  private Resource resource;

  @OSGiService
  private ColumnClassProvider columnClassProvider;

  @Inject
  @Getter
  private String[] classes;

  @ValueMapValue
  @Default(values = StringUtils.EMPTY)
  private String customClass;

  @PostConstruct
  private void init() {
    Map<String, ResponsiveColumnStyle> columnStyleMap = new HashMap<>();
    addToColumnStyleMapIfColumnStyleIsNotNull(columnStyleMap, "mobile", getMobileColumnStyle());
    addToColumnStyleMapIfColumnStyleIsNotNull(columnStyleMap, "tablet", getTabletColumnStyle());
    addToColumnStyleMapIfColumnStyleIsNotNull(columnStyleMap, "desktop", getDesktopColumnStyle());

    classes = columnClassProvider.getClasses(columnStyleMap);
    classes = ArrayUtils.add(classes, customClass);
  }

  public List<Resource> getChildrenComponents() {
    return StreamSupport
        .stream(resource.getChildren().spliterator(), false)
        .filter(it -> it.getValueMap().get(SLING_RESOURCE_TYPE) != null)
        .collect(Collectors.toList());
  }

  private void addToColumnStyleMapIfColumnStyleIsNotNull(
      Map<String, ResponsiveColumnStyle> columnStyleMap,
      String screen,
      ResponsiveColumnStyle columnStyle) {
    if (columnStyle != null) {
      columnStyleMap.put(screen, columnStyle);
    }
  }

}
