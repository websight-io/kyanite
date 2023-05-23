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

import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.bulma.components.helpers.IconContainerService;
import pl.ds.bulma.components.utils.LinkUtil;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class ButtonComponent {

  @Inject
  @Getter
  @Default(values = "Label")
  private String label;

  @Inject
  @Getter
  private boolean showIconLeft;

  @Inject
  @Getter
  @Default(values = "mdi")
  private String iconLibTypeLeft;

  @Inject
  @Getter
  private String iconLeft;

  @Inject
  @Getter
  @Default(values = "mdi-36px")
  private String iconSizeLeft;

  @Getter
  private String iconContainerSizeLeft;

  @Inject
  @Getter
  private boolean showIconRight;

  @Inject
  @Getter
  @Default(values = "mdi")
  private String iconLibTypeRight;

  @Inject
  @Getter
  private String iconRight;

  @Inject
  @Getter
  @Default(values = "mdi-36px")
  private String iconSizeRight;

  @Getter
  private String iconContainerSizeRight;

  @Inject
  @Getter
  @Default(values = "button")
  private String type;

  @Inject
  @Getter
  @Default(values = "is-primary")
  private String variant;

  @Inject
  @Getter
  private String size;

  @Inject
  private boolean isLight;

  @Inject
  private boolean isOutlined;

  @Inject
  private boolean isRounded;

  @Inject
  private boolean isInverted;

  @Inject
  private boolean isFullWidth;

  @Inject
  @Getter
  private boolean isDisabled;

  @Inject
  @Default(values = StringUtils.EMPTY)
  private String url;

  @Inject
  @Getter
  @Default(values = "false")
  private String openInNewTab;

  @Inject
  @Getter
  @Default(values = StringUtils.EMPTY)
  private String actionType;

  @Inject
  @Getter
  @Default(values = StringUtils.EMPTY)
  private String modalId;

  @Inject
  @Getter
  private String[] buttonClasses;

  @SlingObject
  private Resource resource;

  @PostConstruct
  private void init() {
    List<String> classes = new ArrayList<>();
    classes.add(size);
    classes.add(variant);
    if (isLight) {
      classes.add("is-light");
    }
    if (isOutlined) {
      classes.add("is-outlined");
    }
    if (isRounded) {
      classes.add("is-rounded");
    }
    if (isInverted) {
      classes.add("is-inverted");
    }
    if (isFullWidth) {
      classes.add("is-fullwidth");
    }
    buttonClasses = classes.toArray(new String[]{});

    IconContainerService iconContainerService = new IconContainerService(resource);
    String mappingPath
            = "bulma/components/common/icon/containersize/defaultsizemappings";

    this.iconContainerSizeLeft
            = iconContainerService.calculateContainerSize(this.iconLibTypeLeft,
            mappingPath, this.iconSizeLeft);
    this.iconContainerSizeRight
            = iconContainerService.calculateContainerSize(this.iconLibTypeRight,
            mappingPath, this.iconSizeRight);
  }

  public String getUrl() {
    return LinkUtil.handleLink(url, resource.getResourceResolver());
  }
}
