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
import pl.ds.kyanite.common.components.utils.LinkUtil;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class LinkComponent {

  @Inject
  @Getter
  @Default(values = "Label")
  private String label;

  @Inject
  @Default(values = StringUtils.EMPTY)
  private String link;

  @Inject
  @Getter
  @Default(values = "false")
  private String openInNewTab;

  @Inject
  @Getter
  private boolean showIconLeft;

  @Inject
  @Getter
  private String iconLeft;

  @Inject
  @Getter
  private boolean showIconRight;

  @Inject
  @Getter
  private String iconRight;

  @Inject
  @Getter
  @Default(values = "is-dark")
  private String variant;

  @Inject
  @Getter
  @Default(values = "is-medium")
  private String size;

  @Getter
  private String[] linkClasses;
  @Getter
  private String iconLibType;
  @Getter
  private String iconSize;
  @Getter
  private String iconContainerSize;

  @SlingObject
  private Resource resource;

  @ChildResource
  private Resource leftIcon;

  @ChildResource
  private Resource rightIcon;

  @PostConstruct
  private void init() {
    List<String> classes = new ArrayList<>();
    classes.add(size);
    classes.add(variant);

    this.linkClasses = classes.toArray(new String[]{});
  }

  public String getLink() {
    return LinkUtil.handleLink(link, resource.getResourceResolver());
  }

  public boolean hasLeftIcon() {
    return Objects.nonNull(leftIcon);
  }

  public boolean hasRightIcon() {
    return Objects.nonNull(rightIcon);
  }

}
