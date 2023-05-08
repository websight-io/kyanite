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

package pl.ds.bulma.components.models.level;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(SlingContextExtension.class)
class LevelItemComponentTest {


  private static final String PATH = "/content/level";
  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(LevelItemComponent.class, LevelComponent.class, PositionedLevelComponent.class);
    context.load().json(requireNonNull(
        Thread.currentThread().getContextClassLoader().getResourceAsStream("levelitem.json")), PATH);
  }

  @Test
  void levelItemOnLevel() {
    LevelItemComponent model = requireNonNull(
        getResource("/onLevel/firstItem")).adaptTo(LevelItemComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getElementType()).isEqualTo("div");
  }

  @Test
  void levelItemOnPositionedLevel() {
    LevelItemComponent model = requireNonNull(
        getResource("/onPositionedLevel/firstItem")).adaptTo(LevelItemComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getElementType()).isEqualTo("p");
  }

  @Nullable
  private Resource getResource(String variant) {
    return context.resourceResolver().getResource(PATH + variant);
  }

}