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

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Required;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.bulma.components.services.ColorService;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContentComponent {

  private static final String DEFAULT_TEXT = "Content";
  public static final String TEXT_WEIGHT_DEFAULT = "has-text-weight-normal";

  @Inject
  @Getter
  @Default(values = DEFAULT_TEXT)
  private String text;

  @Inject
  @Getter
  @Default(values = StringUtils.EMPTY)
  private String size;

  @Inject
  @Getter
  @Default(values = TEXT_WEIGHT_DEFAULT)
  private String textWeight;

  @Inject
  @Default(values = "grey_has-text-grey")
  private String color;

  @SlingObject
  private Resource resource;

  @Getter
  private String textColorVariant;

  @OSGiService
  @Required
  private ColorService colorService;

  @PostConstruct
  private void init() {
    if (text.isEmpty()) {
      text = DEFAULT_TEXT;
    }
    this.textColorVariant = colorService.getShadeClass(resource, color, "shade");
  }

}
