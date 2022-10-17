/*
 * <!--
 *     Copyright (C) 2022 Dynamic Solutions
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 * -->
 */

package pl.ds.bulma.components.models.menu;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SlingContextExtension.class)
class MenuListComponentTest {

  private static final String PATH = "/content/menu";
  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(MenuListComponent.class, MenuComponent.class,
        MenuListItemComponent.class);
    context.load().json(requireNonNull(
            Thread.currentThread().getContextClassLoader().getResourceAsStream("menu.json")),
        PATH);
  }

  @Test
  void defaultPricingPlanComponentModelTest() {
    MenuComponent model = context.resourceResolver().getResource(PATH + "/default")
        .adaptTo(MenuComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getMenuSections()).isNull();
  }

  @Test
  void pricingPlanComponentModelTest() {
    MenuComponent model = context.resourceResolver().getResource(PATH + "/complex")
        .adaptTo(MenuComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getMenuSections()).hasSize(2);
    assertThat(
        model.getMenuSections()
            .stream()
            .filter(i -> i.getLabel().equals("Label1"))
            .findFirst()
              .get().getMenuListItemComponentList()
              .get(1) //only second item has children
              .getSecondLevelItems()).hasSize(2);
    assertThat(
        model.getMenuSections()
            .stream()
            .filter(i -> i.getLabel().equals("Label2"))
            .findFirst()
            .get().getMenuListItemComponentList()
            .get(0)
            .getSecondLevelItems()).isNull();

  }

}