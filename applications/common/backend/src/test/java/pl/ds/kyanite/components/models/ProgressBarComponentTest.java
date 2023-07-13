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

package pl.ds.kyanite.components.models;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SlingContextExtension.class)
class ProgressBarComponentTest {

  private static final String PATH = "/content/progressbar";
  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(ProgressBarComponent.class);
    context.load().json(requireNonNull(
            Thread.currentThread().getContextClassLoader().getResourceAsStream("progressbar.json")),
        PATH);
  }

  @Test
  void defaultProgressBarComponentModelTest() {
    ProgressBarComponent model = context.resourceResolver().getResource(PATH + "/default")
        .adaptTo(ProgressBarComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getVariant()).isNull();
    assertThat(model.isIntermediate()).isFalse();
    assertThat(model.getValue()).isEqualTo(50);
    assertThat(model.getSize()).isNull();
    assertThat(model.getProgressBarClasses()).isEmpty();
  }

  @Test
  void isIntermediateModelTest() {
    ProgressBarComponent model = context.resourceResolver().getResource(PATH + "/isIntermediate")
        .adaptTo(ProgressBarComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getVariant()).isEqualTo("is-warning");
    assertThat(model.isIntermediate()).isTrue();
    assertThat(model.getValue()).isEqualTo(45);
    assertThat(model.getSize()).isEqualTo("is-medium");
    assertThat(model.getProgressBarClasses()).containsExactlyInAnyOrder("is-warning", "is-medium");

  }

  @Test
  void valueSetModelTest() {
    ProgressBarComponent model = context.resourceResolver().getResource(PATH + "/valueSet")
        .adaptTo(ProgressBarComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getVariant()).isEqualTo("is-warning");
    assertThat(model.isIntermediate()).isFalse();
    assertThat(model.getValue()).isEqualTo(45);
    assertThat(model.getSize()).isEqualTo("is-medium");
    assertThat(model.getProgressBarClasses()).containsExactlyInAnyOrder("is-warning", "is-medium");

  }

}
