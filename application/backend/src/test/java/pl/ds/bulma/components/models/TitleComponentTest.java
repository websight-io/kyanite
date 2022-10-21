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

package pl.ds.bulma.components.models;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SlingContextExtension.class)
class TitleComponentTest {

  private static final String PATH = "/content/title";
  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(TitleComponent.class);
    context.load().json(requireNonNull(
        Thread.currentThread().getContextClassLoader().getResourceAsStream("title.json")), PATH);
  }

  @Test
  void defaultTitleComponentModelTest() {
    TitleComponent model = context.resourceResolver().getResource(PATH + "/default")
        .adaptTo(TitleComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getText()).isEqualTo("Title");
    assertThat(model.getType()).isEqualTo("title");
    assertThat(model.getElement()).isEqualTo("h1");
    assertThat(model.getTitleClasses()).containsExactlyInAnyOrder("title");
    assertThat(model.getSize()).isEmpty();
    assertThat(model.isSpaced()).isFalse();
  }

  @Test
  void titleComponentModelTest() {
    TitleComponent model = context.resourceResolver().getResource(PATH + "/complex")
        .adaptTo(TitleComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getText()).isEqualTo("Nice subtitle");
    assertThat(model.getType()).isEqualTo("subtitle");
    assertThat(model.getElement()).isEqualTo("p");
    assertThat(model.getTitleClasses()).containsExactlyInAnyOrder("subtitle", "is-2", "is-spaced");
    assertThat(model.getSize()).isEqualTo("is-2");
    assertThat(model.isSpaced()).isTrue();
  }
}
