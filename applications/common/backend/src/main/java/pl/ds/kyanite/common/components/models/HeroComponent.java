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

import java.util.Objects;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.kyanite.common.components.utils.LinkUtil;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeroComponent {

  @Inject
  @Getter
  private String variant;

  @Inject
  @Getter
  private String size;

  @Inject
  @Getter
  private String background;

  @Inject
  @Getter
  private String[] heroClasses;

  @Inject
  private String desktopBackgroundImage;

  @Inject
  private String tabletBackgroundImage;

  @Inject
  private String mobileBackgroundImage;

  @SlingObject
  private ResourceResolver resourceResolver;

  @PostConstruct
  private void init() {
    heroClasses = Stream.of(size, variant, background)
        .filter(Objects::nonNull)
        .toList()
        .toArray(new String[]{});
  }

  public String getDesktopBackgroundImage() {
    return getBackgroundImage(desktopBackgroundImage);
  }

  public String getTabletBackgroundImage() {
    return getBackgroundImage(tabletBackgroundImage);
  }

  public String getMobileBackgroundImage() {
    return getBackgroundImage(mobileBackgroundImage);
  }

  private String getBackgroundImage(String src) {
    if (StringUtils.isEmpty(src)) {
      return null;
    }

    return LinkUtil.handleLink(src, resourceResolver);
  }
}
