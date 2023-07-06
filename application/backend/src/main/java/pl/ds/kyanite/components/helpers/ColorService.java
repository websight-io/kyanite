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

package pl.ds.kyanite.components.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

public class ColorService {
  private final List<String> colorBw;
  private final List<String> colorGrey;
  private final List<String> colorRest;
  private final String color;
  private final String shadeBw;
  private final String shadeGrey;
  private final String shadeRest;

  public ColorService(Resource resource, String resourcePath, String color,
                      String shadeBw, String shadeGrey, String shadeRest) {
    List<String> colorsList = getColorsListFromResource(resource, resourcePath);

    this.colorBw = getElementsFromListByPrefix(colorsList, "bw_");
    this.colorGrey = getElementsFromListByPrefix(colorsList, "grey_");
    this.colorRest = getElementsFromListByPrefix(colorsList, "rest_");

    this.color = color.replace("bw_", "")
                .replace("grey_", "")
                .replace("rest_", "");

    this.shadeBw = shadeBw;
    this.shadeGrey = shadeGrey;
    this.shadeRest = shadeRest;
  }

  public String getTextColorVariant() {
    if (colorBw.contains(this.color)) {
      return prepareColorClass(this.color, this.shadeBw);
    }

    if (colorGrey.contains(this.color)) {
      return prepareColorClass(this.color, this.shadeGrey);
    }

    if (colorRest.contains(this.color)) {
      return prepareColorClass(this.color, this.shadeRest);
    }

    return this.color;
  }

  private List<String> getColorsListFromResource(Resource resource, String resourcePath) {
    if (resource != null) {
      ResourceResolver resourceResolver = resource.getResourceResolver();
      Resource textColorsResource = resourceResolver
              .getResource(resourcePath);

      if (textColorsResource != null) {
        Spliterator<Resource> spliterator = Spliterators
                .spliteratorUnknownSize(textColorsResource.listChildren(), Spliterator.ORDERED);

        return StreamSupport.stream(spliterator, false)
                .map(Resource::getValueMap)
                .map(valueMap -> valueMap.get("value", String.class))
                .collect(Collectors.toList());
      }
    }

    return new ArrayList<>();
  }

  private List<String> getElementsFromListByPrefix(List<String> list, String prefix) {
    return list.stream().filter(i -> i.startsWith(prefix))
                .map(i -> i.substring(prefix.length()))
                .collect(Collectors.toList());
  }

  private String prepareColorClass(String colorClass, String shadeSuffixClass) {
    return shadeSuffixClass != null
                && !shadeSuffixClass.isEmpty() ? colorClass + "-" + shadeSuffixClass : colorClass;
  }
}
