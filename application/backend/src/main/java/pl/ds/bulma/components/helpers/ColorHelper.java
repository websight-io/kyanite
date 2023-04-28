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

package pl.ds.bulma.components.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ColorHelper {
  private List<String> colorBw = new ArrayList<>();
  private List<String> colorGrey = new ArrayList<>();
  private List<String> colorRest = new ArrayList<>();
  private String color = "";
  private String shadeBw = "";
  private String shadeGrey = "";
  private String shadeRest = "";

  public ColorHelper(List<String> colorsList, String color,
                     String shadeBw, String shadeGrey, String shadeRest) {
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
