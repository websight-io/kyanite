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

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pl.ds.kyanite.common.components.models.CardComponent;

@ExtendWith(SlingContextExtension.class)
class CardComponentTest {

  private static final String PATH = "/content/card";
  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(CardComponent.class);
    context.load().json(requireNonNull(
        Thread.currentThread().getContextClassLoader().getResourceAsStream("card.json")), PATH);
  }

  @Test
  void defaultCardComponentModelTest() {
    CardComponent model = context.resourceResolver().getResource(PATH + "/default")
        .adaptTo(CardComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getHeader()).isEqualTo(StringUtils.EMPTY);

  }

  @Test
  void cardComponentModelTest() {
    CardComponent model = context.resourceResolver().getResource(PATH + "/complex")
        .adaptTo(CardComponent.class);

    assertThat(model).isNotNull();
    assertThat(model).isNotNull();
    assertThat(model.getHeader()).isEqualTo("Card header");

  }
}
