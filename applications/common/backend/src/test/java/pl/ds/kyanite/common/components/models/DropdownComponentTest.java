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

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pl.ds.kyanite.common.components.models.DropdownComponent;

@ExtendWith(SlingContextExtension.class)
class DropdownComponentTest {

  private static final String PATH = "/content/dropdown";

  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(DropdownComponent.class);
    context.load().json(requireNonNull(
        Thread.currentThread().getContextClassLoader().getResourceAsStream("dropdown.json")), PATH);
  }

  @Test
  void defaultDropdownComponentModelTest() {
    DropdownComponent model = context.resourceResolver().getResource(PATH + "/default")
        .adaptTo(DropdownComponent.class);
    assertThat(model).isNotNull();
    assertThat(model.getLabel()).isEqualTo("Label");
    assertThat(model.getDropdownClasses()).isEmpty();
    assertThat(model.getItems()).isNull();
  }

  @Test
  void dropdownComponentTest() {
    DropdownComponent model = context.resourceResolver().getResource(PATH + "/complex")
        .adaptTo(DropdownComponent.class);
    assertThat(model.getLabel()).isEqualTo("Dropdown");
    assertThat(model.getDropdownClasses()).containsExactlyInAnyOrder("is-hoverable", "is-up",
        "is-right");
    assertThat(model.getItems().stream()
        .filter(i -> i.getLabel().equals("Item 1"))
        .filter(i -> i.getUrl().equals("http://url1"))
        .filter(i -> i.isHasDivider())
        .count()).isEqualTo(1);
    assertThat(model.getItems().stream()
        .filter(i -> i.getLabel().equals("Item 2"))
        .filter(i -> i.getUrl().equals("http://url2"))
        .filter(i -> !i.isHasDivider())
        .count()).isEqualTo(1);

  }

}
