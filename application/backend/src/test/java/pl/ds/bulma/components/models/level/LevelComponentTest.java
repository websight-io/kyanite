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
import static pl.ds.bulma.components.models.level.LevelComponent.LEVEL_ITEM_COMPONENT_RESOURCE_TYPE;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SlingContextExtension.class)
class LevelComponentTest {


  private static final String PATH = "/content/level";
  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(LevelComponent.class);
    context.load().json(requireNonNull(
        Thread.currentThread().getContextClassLoader().getResourceAsStream("level.json")), PATH);
  }


  @Test
  void defaultLevel() {
    LevelComponent model = requireNonNull(
        getResource("/default")).adaptTo(LevelComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.isVertical()).isTrue();
  }

  @Test
  void centeredLevel() {
    LevelComponent model = requireNonNull(
        getResource("/centered")).adaptTo(LevelComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.isVertical()).isFalse();
    getResource("/centered").getChildren().forEach(child -> {
      assertThat(child.isResourceType(LEVEL_ITEM_COMPONENT_RESOURCE_TYPE)).isTrue();
    });
  }

  @Nullable
  private Resource getResource(String variant) {
    return context.resourceResolver().getResource(PATH + variant);
  }


}