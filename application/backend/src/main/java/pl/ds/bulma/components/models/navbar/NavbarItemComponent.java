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

package pl.ds.bulma.components.models.navbar;

import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import pl.ds.bulma.components.models.ImageComponent;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NavbarItemComponent {


  @Inject
  @Getter
  @Default(values = "link")
  private String type;

  @Inject
  @Getter
  private String label;

  @Inject
  @Getter
  private String url;

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
  private String icon;
}
