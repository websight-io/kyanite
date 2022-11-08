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

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NavbarComponent {

  @Inject
  @Getter
  private String variant;

  @Inject
  @Getter
  private boolean isTransparent;

  @Inject
  @Getter
  private String navbarStyle;

  @Inject
  @Getter
  private String fixedOption;

  @PostConstruct
  private void init() {
    if (isTransparent) {
      navbarStyle = "is-transparent";
    } else {
      navbarStyle = variant;
    }

    if (fixedOption != null && !fixedOption.isEmpty()) {
      navbarStyle = navbarStyle + " " + fixedOption;
    }
  }
}
