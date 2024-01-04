/*
 * Copyright (C) 2024 Dynamic Solutions
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

package pl.ds.kyanite.common.components.models.navbar;

import java.util.List;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NavbarMenuComponent {

  private static final String NAVBAR_START = "kyanite/common/components/navbar/navbarstart";
  private static final String NAVBAR_END = "kyanite/common/components/navbar/navbarend";

  @SlingObject
  private Resource resource;

  @Inject
  @Getter
  private List<NavbarItemComponent> metaNavigation;

  @Getter
  private String fixedOption;

  @Getter
  private List<Resource> items;

  public Boolean getHasChildren() {
    return items != null && !items.isEmpty();
  }

  @PostConstruct
  private void init() {
    NavbarComponent navbarComponent = resource.getParent().adaptTo(NavbarComponent.class);
    if (navbarComponent != null) {
      fixedOption = navbarComponent.getFixedOption();
    }
    items = findMenuItems();
  }

  private List<Resource> findMenuItems() {
    return StreamSupport.stream(resource.getChildren().spliterator(), false)
        .filter(this::isCorrectItem)
        .toList();
  }

  private boolean isCorrectItem(Resource resource) {
    return this.isComponentType(resource, NAVBAR_START)
        || this.isComponentType(resource, NAVBAR_END);
  }

  private boolean isComponentType(Resource resource, String componentName) {
    return componentName.equals(resource.getResourceType())
        || resource.isResourceType(componentName);
  }
}
