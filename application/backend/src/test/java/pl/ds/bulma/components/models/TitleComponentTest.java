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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.ds.bulma.components.services.ColorService;

@ExtendWith({SlingContextExtension.class, MockitoExtension.class})
class TitleComponentTest {

  private static final String PATH = "/content/title";
  private static final String SHADE_PATH = "/libs/bulma/components/common/text/shade";
  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @Spy
  private ColorService colorService;

  @BeforeEach
  public void init() {

    context.addModelsForClasses(TitleComponent.class);
    context.registerService(ColorService.class, colorService);
    context.load().json(requireNonNull(
        Thread.currentThread().getContextClassLoader().getResourceAsStream("title.json")), PATH);
    context.load().json(requireNonNull(
        Thread.currentThread().getContextClassLoader().getResourceAsStream("titleShade.json")), SHADE_PATH);
  }

  @Test
  void titleComponentModelTest() {
    TitleComponent model = context.resourceResolver().getResource(PATH + "/complex")
        .adaptTo(TitleComponent.class);
    assertThat(model).isNotNull();
    assertThat(model.getText()).isEqualTo("Title");
    assertThat(model.getSubtitle()).isEqualTo("Subtitle");
    assertThat(model.getElement()).isEqualTo("h2");
    assertThat(model.isSpaced()).isFalse();
    assertThat(model.isAddSubtitle()).isFalse();
    assertThat(model.getSize()).isEqualTo("is-2");

    assertThat(model.getAnchorId()).isEqualTo("");

    assertThat(model.getTitleClasses()).containsExactlyInAnyOrder("title", "is-2", "is-spaced");
    assertThat(model.getSubtitleClasses()).containsExactlyInAnyOrder("subtitle", "is-4");
  }

  @Test
  void titleComponentModelWithoutShadeTest() {
    TitleComponent model = context.resourceResolver().getResource(PATH + "/complex1")
        .adaptTo(TitleComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getText()).isEqualTo("Nice title");
    assertThat(model.getSubtitle()).isEqualTo("Nice subtitle");
    assertThat(model.getElement()).isEqualTo("p");
    assertThat(model.getAnchorId()).isEqualTo("anchorId");
    assertThat(model.getTitleClasses()).containsExactlyInAnyOrder("title", "is-4", "has-text-dark");
    assertThat(model.getSubtitleClasses()).containsExactlyInAnyOrder("subtitle", "is-6", "has-text-light");
    assertThat(model.getSize()).isEqualTo("is-4");
  }

  @Test
  void titleComponentModelNewShadeTest() {
    TitleComponent model = context.resourceResolver().getResource(PATH + "/newShade")
        .adaptTo(TitleComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getText()).isEqualTo("Nice title");
    assertThat(model.getSubtitle()).isEqualTo("Nice subtitle");
    assertThat(model.getTitleClasses()).containsExactlyInAnyOrder("title", "has-text-black-test");
    assertThat(model.getSubtitleClasses()).containsExactlyInAnyOrder("subtitle", "has-text-grey-test1");
  }
}
