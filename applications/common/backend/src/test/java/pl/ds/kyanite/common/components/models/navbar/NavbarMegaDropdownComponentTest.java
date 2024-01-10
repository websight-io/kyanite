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

package pl.ds.kyanite.common.components.models.navbar;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SlingContextExtension.class)
class NavbarMegaDropdownComponentTest {

  private static final String PATH = "/content/megadropdown";

  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(NavbarMegaDropdownComponent.class,
        MegaDropdownColumnComponent.class, TextSectionComponent.class, NavbarItemComponent.class);
    context.load().json(requireNonNull(
            Thread.currentThread().getContextClassLoader().getResourceAsStream("megadropdown.json")),
        PATH);
  }

  @Test
  void hiddenColumnsTest() {
    NavbarMegaDropdownComponent model = context.resourceResolver().getResource(PATH + "/hidden")
        .adaptTo(NavbarMegaDropdownComponent.class);

    assertThat(model).isNotNull();
    assertEquals("column-2", model.getDropdownColumns().get(0).getColSizeClass());
    assertEquals("column-3", model.getDropdownColumns().get(2).getColSizeClass());
  }

  @Test
  void notHiddenColumnsTest() {
    NavbarMegaDropdownComponent model = context.resourceResolver().getResource(PATH + "/not-hidden")
        .adaptTo(NavbarMegaDropdownComponent.class);

    assertThat(model).isNotNull();
    model.getDropdownColumns().forEach(column -> {
          assertFalse(column.isHideColumn());
          assertNull(column.getColSizeClass());
        }
    );
  }
}
