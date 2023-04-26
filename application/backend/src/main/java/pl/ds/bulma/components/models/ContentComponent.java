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

package pl.ds.bulma.components.models;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContentComponent {

  @Inject
  @Getter
  @Default(values = "Content")
  private String text;

  @Inject
  @Getter
  @Default(values = StringUtils.EMPTY)
  private String size;

  @Inject
  @Default(values = StringUtils.EMPTY)
  private String color;

  @Inject
  @Default(values = StringUtils.EMPTY)
  private String shade;

  public String getTextColorVariant() {
    List<String> blackAndWhiteColor = new ArrayList<>();
    blackAndWhiteColor.add("has-text-black");
    blackAndWhiteColor.add("has-text-white");
    List<String> blackAndWhiteShade = new ArrayList<>();
    blackAndWhiteShade.add("bis");
    blackAndWhiteShade.add("ter");

    List<String> greyColor = new ArrayList<>();
    greyColor.add("has-text-grey");
    List<String> greyShade = new ArrayList<>();
    greyShade.add("light");
    greyShade.add("lighter");
    greyShade.add("dark");
    greyShade.add("darker");

    List<String> restColor = new ArrayList<>();
    restColor.add("has-text-primary");
    restColor.add("has-text-link");
    restColor.add("has-text-info");
    restColor.add("has-text-success");
    restColor.add("has-text-success");
    restColor.add("has-text-warning");
    restColor.add("has-text-danger");
    List<String> restShade = new ArrayList<>();
    restShade.add("light");
    restShade.add("dark");

    return (blackAndWhiteColor.contains(color) && blackAndWhiteShade.contains(shade))
      || (greyColor.contains(color) && greyShade.contains(shade))
      || (restColor.contains(color) && restShade.contains(shade))
      ? color + "-" + shade : color;
  }
}
