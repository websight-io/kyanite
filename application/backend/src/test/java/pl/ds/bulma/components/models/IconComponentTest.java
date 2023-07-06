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

package pl.ds.bulma.components.models;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SlingContextExtension.class)
class IconComponentTest {

  private static final String PATH = "/content/icon";
  private final SlingContext context = new SlingContext();

  @BeforeEach
  public void init() {
    context.addModelsForClasses(IconComponent.class);
    context.load().json(requireNonNull(
        Thread.currentThread().getContextClassLoader().getResourceAsStream("icon.json")), PATH);
  }

  @Test
  void getIcon() {
    IconComponent model = requireNonNull(
        context.resourceResolver().getResource(PATH)).adaptTo(IconComponent.class);

    assertThat(model).isNotNull();
    assertEquals( "mdi-home-outline", model.getIcon());
    assertEquals( "Icon Text", model.getLabel());
    assertEquals( "mdi", model.getIconLibType());
    assertEquals( "span", model.getElementType());
    assertEquals( "has-text-primary", model.getTextVariant());
  }

}