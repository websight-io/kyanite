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

package pl.ds.kyanite.common.components.models;

import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import pl.ds.kyanite.common.components.utils.LinkUtil;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class ButtonComponent {

  @Inject
  @Getter
  @Default(values = "Label")
  private String label;

  @ValueMapValue
  @Getter
  private boolean hideLabel;

  @Inject
  @Getter
  private boolean showIconLeft;

  @Inject
  @Getter
  private boolean showIconRight;

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

  @ChildResource
  private Resource leftIcon;

  @ChildResource
  private Resource rightIcon;

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
  private boolean isFullWidthOnMobile;

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

  @ValueMapValue
  @Default(values = StringUtils.EMPTY)
  private String mail;

  @ValueMapValue
  @Getter
  private String isMailto;

  @Getter
  private String mailPart1;
  @Getter
  private String mailPart2;
  @Getter
  private String mailPart3;

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
    if (!isFullWidth && isFullWidthOnMobile) {
      classes.add("is-fullwidth-on-mobile");
    }
    buttonClasses = classes.toArray(new String[]{});

    if ("true".equals(isMailto)) {
      List<String> parts = pl.ds.kyanite.common.components.utils.StringUtils.encryptEmail(mail);
      if (parts.size() == 3) {
        mailPart1 = parts.get(0);
        mailPart2 = parts.get(1);
        mailPart3 = parts.get(2);
      }
    } else {
      mailPart1 = "";
      mailPart2 = "";
      mailPart3 = "";
    }
  }

  public boolean hasLeftIcon() {
    return Objects.nonNull(leftIcon);
  }

  public boolean hasRightIcon() {
    return Objects.nonNull(rightIcon);
  }

  public String getUrl() {
    return LinkUtil.handleLink(url, resource.getResourceResolver());
  }
}
