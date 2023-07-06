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
class ButtonComponentTest {

  private static final String PATH = "/content/button";
  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(ButtonComponent.class);
    context.load().json(requireNonNull(
        Thread.currentThread().getContextClassLoader().getResourceAsStream("button.json")), PATH);
  }

  @Test
  void defaultButtonComponentModelTest() {
    ButtonComponent model = context.resourceResolver().getResource(PATH + "/default")
        .adaptTo(ButtonComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getLabel()).isEqualTo("Label");
    assertThat(model.getType()).isEqualTo("button");
    assertThat(model.getVariant()).isEqualTo("is-primary");
    assertThat(model.getSize()).isNull();

  }

  @Test
  void buttonComponentModelTest() {
    ButtonComponent model = context.resourceResolver().getResource(PATH + "/complex")
        .adaptTo(ButtonComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getLabel()).isEqualTo("Button");
    assertThat(model.getType()).isEqualTo("anchor");
    assertThat(model.getVariant()).isEqualTo("is-white");
    assertThat(model.isShowIconLeft()).isTrue();
    assertThat(model.getSize()).isEqualTo("is-medium");
  }
}
