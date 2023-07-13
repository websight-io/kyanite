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

package pl.ds.kyanite.components.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;

@Component(service = {ColorService.class})
public class ColorService {


  public String getShadeClass(Resource resource, String color, String propertyName) {
    if (Objects.isNull(color)) {
      return StringUtils.EMPTY;
    }
    String shade = this.getShadeByColor(resource, color, propertyName);
    Map<String, List<String>> colorShadeMapping = this.getColorShadeMapping(resource);

    PrefixToSuffix mappingForSpecificShade = this.getMappingForSpecificShade(shade, color,
        colorShadeMapping);
    return this.getClass(mappingForSpecificShade, color);
  }

  private String getClass(PrefixToSuffix mapping, String color) {
    return Optional.ofNullable(mapping)
        .map(elem -> {
          String prefix = elem.getPrefix();
          String suffix = elem.getSuffix();
          String colorClass = StringUtils.substringAfter(color, prefix + "_");
          return !elem.getSuffix().isBlank()
              && !suffix.isBlank() ? colorClass + "-" + suffix : colorClass;
        })
        .orElse(color);
  }

  private String getShadeByColor(Resource resource, String color, String propertyName) {
    return StreamSupport
        .stream(resource.getChildren().spliterator(), false)
        .filter(it -> color.startsWith(it.getName()))
        .map(Resource::getValueMap)
        .map(vm -> StringUtils.defaultString(vm.get(propertyName, String.class),
            StringUtils.EMPTY))
        .findFirst()
        .orElse(StringUtils.EMPTY);
  }

  private PrefixToSuffix getMappingForSpecificShade(String shade, String color,
      Map<String, List<String>> colorShadeMapping) {
    return colorShadeMapping.entrySet().stream()
        .filter(it -> color.startsWith(it.getKey()))
        .findFirst()
        .map(it -> PrefixToSuffix.builder()
            .prefix(it.getKey())
            .suffix(it.getValue().stream()
                .filter(shade::equals)
                .findFirst()
                .orElse(StringUtils.EMPTY))
            .build())
        .orElse(null);
  }

  private Map<String, List<String>> getColorShadeMapping(Resource resource) {
    Map<String, List<String>> map = new HashMap<>();
    if (Objects.isNull(resource)) {
      return map;
    }
    ResourceResolver resourceResolver = resource.getResourceResolver();
    Resource textColorsResource = resourceResolver
        .getResource("kyanite/components/common/text/shade");
    if (textColorsResource != null) {
      List<Resource> resources = StreamSupport.stream(Spliterators
              .spliteratorUnknownSize(textColorsResource.listChildren(),
                  Spliterator.ORDERED), false)
          .toList();
      for (Resource res : resources) {
        map.put(res.getName(), StreamSupport.stream(Spliterators
                .spliteratorUnknownSize(res.listChildren(),
                    Spliterator.ORDERED), false)
            .map(Resource::getValueMap)
            .map(vm -> StringUtils.defaultString(vm.get("value", String.class),
                StringUtils.EMPTY))
            .toList());
      }
    }
    return map;
  }

  @AllArgsConstructor
  @Builder
  @Getter
  public static class PrefixToSuffix {

    private String prefix = "";
    private String suffix = "";
  }

}