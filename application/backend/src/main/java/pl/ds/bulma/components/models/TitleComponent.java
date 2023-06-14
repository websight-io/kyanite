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
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.bulma.components.helpers.ColorService;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TitleComponent {

  @Inject
  @Getter
  private String text;

  @Inject
  @Getter
  private String subtitle;

  @Inject
  @Getter
  private String element;

  @Inject
  @Getter
  private boolean isSpaced;

  @Inject
  @Getter
  private boolean addSubtitle;

  @Inject
  @Getter
  private String size;

  @Inject
  @Getter
  private String[] titleClasses;

  @Inject
  @Getter
  private String[] subtitleClasses;

  @Inject
  private String color;

  @Inject
  private String subtitleColor;

  @Inject
  private String shadeBw;

  @Inject
  private String shadeGrey;

  @Inject
  private String shadeRest;

  @Inject
  private String subtitleShadeBw;

  @Inject
  private String subtitleShadeGrey;

  @Inject
  private String subtitleShadeRest;

  @Inject
  @Getter
  private String anchorId;

  @SlingObject
  private Resource resource;

  @Getter
  private String textColorVariant;

  @Getter
  private String subtitleColorVariant;

  @PostConstruct
  private void init() {
    List<String> titleClassList = new ArrayList<>();
    List<String> subtitleClassList = new ArrayList<>();
    titleClassList.add("title");
    subtitleClassList.add("subtitle");

    if (StringUtils.isNotBlank(size)) {
      titleClassList.add(size);
      int sizeNumber = Integer.parseInt(size.split("-")[1]);
      if (sizeNumber > 4) {
        addSubtitle = false;
      }
      subtitleClassList.add("is-" + (sizeNumber + 2));
    }
    if (isSpaced) {
      titleClassList.add("is-spaced");
    }
    titleClasses = titleClassList.toArray(new String[]{});
    subtitleClasses = subtitleClassList.toArray(new String[]{});

    ColorService colorService
        = new ColorService(resource, "bulma/components/common/text/color",
        this.color, this.shadeBw, this.shadeGrey, this.shadeRest);

    this.textColorVariant = colorService.getTextColorVariant();

    colorService = new ColorService(resource, "bulma/components/common/text/color",
        this.subtitleColor, this.subtitleShadeBw, this.subtitleShadeGrey, this.subtitleShadeRest);

    this.subtitleColorVariant = colorService.getTextColorVariant();
  }
}
