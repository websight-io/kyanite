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

package pl.ds.kyanite.fragments.components.models;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SlingContextExtension.class)
class ExperienceFragmentTest {

  private static final String PATH = "/content/fragments";
  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(ExperienceFragment.class);
    context.load().json(requireNonNull(
        Thread.currentThread().getContextClassLoader().getResourceAsStream("fragment.json")), PATH);
  }

  @Test
  void emptyFragment() {
    ExperienceFragment model =
        requireNonNull(requireNonNull(context.resourceResolver()).getResource(PATH + "/empty"))
            .adaptTo(ExperienceFragment.class);

    assertThat(model).isNotNull();
    assertThat(model.getResource()).isNull();
    assertThat(model.getValidPage()).isFalse();
    assertThat(model.getPagePath()).isNull();
  }

  @Test
  void validFragment() {
    ExperienceFragment model =
        requireNonNull(requireNonNull(context.resourceResolver()).getResource(PATH + "/with-valid-resource"))
            .adaptTo(ExperienceFragment.class);

    assertThat(model).isNotNull();
    assertThat(model.getResource()).isEqualTo("/content/fragments/pages/fragments/fragment");
    assertThat(model.getValidPage()).isTrue();
    assertThat(model.getPagePath()).isEqualTo("/content/fragments/pages/fragments/fragment.html");
  }

  @Test
  void validFragmentWithSlashAtResourcePath() {
    ExperienceFragment model =
        requireNonNull(requireNonNull(context.resourceResolver()).getResource(PATH + "/with-slash-at-the-end"))
            .adaptTo(ExperienceFragment.class);

    assertThat(model).isNotNull();
    assertThat(model.getResource()).isEqualTo("/content/fragments/pages/fragments/fragment/");
    assertThat(model.getValidPage()).isTrue();
    assertThat(model.getPagePath()).isEqualTo("/content/fragments/pages/fragments/fragment.html");
  }

  @Test
  void invalidResourceFragment() {
    ExperienceFragment model =
        requireNonNull(requireNonNull(context.resourceResolver()).getResource(PATH + "/with-invalid-resource"))
            .adaptTo(ExperienceFragment.class);

    assertThat(model).isNotNull();
    assertThat(model.getResource()).isEqualTo("/content/fragments/pages/fragments/invalid");
    assertThat(model.getValidPage()).isFalse();
    assertThat(model.getPagePath()).isEqualTo("/content/fragments/pages/fragments/invalid.html");
  }

  @Test
  void notExistingResourceFragment() {
    ExperienceFragment model =
        requireNonNull(requireNonNull(context.resourceResolver()).getResource(PATH + "/with-not-existing-resource"))
            .adaptTo(ExperienceFragment.class);

    assertThat(model).isNotNull();
    assertThat(model.getResource()).isEqualTo("/content/fragments/pages/fragments/not-existing");
    assertThat(model.getValidPage()).isFalse();
    assertThat(model.getPagePath()).isEqualTo("/content/fragments/pages/fragments/not-existing.html");
  }

}