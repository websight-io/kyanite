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

package pl.ds.bulma.components.models.columns;

import java.util.Arrays;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum ColumnSizes {
  EMPTY(StringUtils.EMPTY),
  ONE_FIFTH("one-fifth"),
  ONE_QUARTER("one-quarter"),
  ONE_THIRD("one-third"),
  TWO_FIFTHS("two-fifths"),
  HALF("half"),
  THREE_FIFTHS("three-fifths"),
  TWO_THIRDS("two-thirds"),
  THREE_QUARTERS("three-quarters"),
  FOUR_FIFTHS("four-fifths"),
  FULL("full"),
  ONE("1"),
  TWO("2"),
  THREE("3"),
  FOUR("4"),
  FIVE("5"),
  SIX("6"),
  SEVEN("7"),
  EIGHT("8"),
  NINE("9"),
  TEN("10"),
  ELEVEN("11"),
  TWELVE("12");

  private final String cssClass;

  ColumnSizes(String cssClass) {
    this.cssClass = cssClass;
  }

  public static ColumnSizes findByName(String name) {
    return Arrays.stream(ColumnSizes.values())
        .filter(it -> it.name().toLowerCase().equals(name))
        .findFirst()
        .orElse(EMPTY);
  }
}
