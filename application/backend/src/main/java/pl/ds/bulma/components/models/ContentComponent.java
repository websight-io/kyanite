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
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.bulma.components.helpers.ColorHelper;

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
  private String shadeBw;

  @Inject
  @Default(values = StringUtils.EMPTY)
  private String shadeGrey;

  @Inject
  @Default(values = StringUtils.EMPTY)
  private String shadeRest;

  @SlingObject
  private Resource resource;

  @Getter
  private String textColorVariant;

  @PostConstruct
  private void init() {
    List<String> colorsList = getColorsListFromResource();
    ColorHelper colorHelper
            = new ColorHelper(colorsList, this.color, this.shadeBw, this.shadeGrey, this.shadeRest);

    this.textColorVariant = colorHelper.getTextColorVariant();
  }

  private List<String> getColorsListFromResource() {
    if (resource != null) {
      ResourceResolver resourceResolver = resource.getResourceResolver();
      Resource textColorsResource = resourceResolver
              .getResource("/apps/bulma/components/common/text/color");
      Spliterator<Resource> spliterator = Spliterators
              .spliteratorUnknownSize(textColorsResource.listChildren(), Spliterator.ORDERED);

      return StreamSupport.stream(spliterator, false)
              .map(Resource::getValueMap)
              .map(valueMap -> valueMap.get("value", String.class))
              .collect(Collectors.toList());
    }

    return new ArrayList<>();
  }
}
