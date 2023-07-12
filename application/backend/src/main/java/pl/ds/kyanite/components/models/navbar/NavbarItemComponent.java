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

package pl.ds.kyanite.components.models.navbar;

import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.kyanite.components.models.ImageComponent;
import pl.ds.kyanite.components.utils.LinkUtil;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NavbarItemComponent {

  @SlingObject
  private Resource resource;

  @Inject
  @Getter
  @Default(values = "link")
  private String type;

  @Inject
  @Getter
  private String label;

  @Inject
  private String url;

  @Inject
  @Getter
  private boolean isLink;

  @Inject
  private String imageUrl;

  @Inject
  @Getter
  private boolean openInNewTab;

  @Inject
  @Getter
  private ImageComponent image;

  @Inject
  @Getter
  private String imageWidth;

  @Inject
  @Getter
  private String imageHeight;

  @Inject
  @Getter
  private boolean hasDivider;

  @Inject
  @Getter
  private boolean addIcon;

  @Inject
  @Getter
  @Default(values = "mdi-home-outline")
  private String icon;

  public String getUrl() {
    return LinkUtil.handleLink(url, resource.getResourceResolver());
  }

  public String getImageUrl() {
    return LinkUtil.handleLink(imageUrl, resource.getResourceResolver());
  }
}
