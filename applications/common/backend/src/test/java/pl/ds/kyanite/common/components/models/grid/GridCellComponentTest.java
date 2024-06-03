/*
 * Copyright (C) 2024 Dynamic Solutions
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

package pl.ds.kyanite.common.components.models.grid;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SlingContextExtension.class)
public class GridCellComponentTest {

  private static final String PATH = "/content/grid/gridcell";

  private final SlingContext context = new SlingContext(ResourceResolverType.JCR_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(GridCellComponent.class);
    context.load().json(requireNonNull(
            Thread.currentThread().getContextClassLoader().getResourceAsStream("gridcells.json")),
        PATH);
  }

  @Test
  void shouldUseDefaultsWhenNoConfigurationSet() {
    final GridCellComponent gridCellComponentUnderTest = createModel("default");

    assertModel(gridCellComponentUnderTest, 1, 1, 1, 1);
  }

  @Test
  void shouldUseInitialPropertiesWhenNotReconfigured() {
    final GridCellComponent gridCellComponentUnderTest = createModel("withInitialProperties");

    assertModel(gridCellComponentUnderTest, 1, 1, 1, 1);
  }

  @Test
  void shouldResolveTabletAndMobileWidthsFromDesktopWhenNotConfigured() {
    final GridCellComponent gridCellComponentUnderTest = createModel("withDefaultsReconfigured");

    assertModel(gridCellComponentUnderTest, 2, 2, 2, 2);
  }

  @Test
  void shouldUseAuthorProvidedConfigurationForWidthsAndHeight() {
    final GridCellComponent gridCellComponentUnderTest = createModel("withAuthorProvidedWidths");

    assertModel(gridCellComponentUnderTest, 3, 2, 1, 4);
  }

  private GridCellComponent createModel(final String componentResourceName) {
    return context.resourceResolver().getResource(PATH + "/" + componentResourceName)
        .adaptTo(GridCellComponent.class);
  }

  private void assertModel(final GridCellComponent actualGridCellComponent,
      int expectedDesktopWidth, int expectedTabletWidth,
      int expectedMobileWidth, int expectedHeight) {

    assertThat(actualGridCellComponent).isNotNull();
    assertThat(actualGridCellComponent.getDesktopWidth()).isEqualTo(expectedDesktopWidth);
    assertThat(actualGridCellComponent.getTabletWidth()).isEqualTo(expectedTabletWidth);
    assertThat(actualGridCellComponent.getMobileWidth()).isEqualTo(expectedMobileWidth);
    assertThat(actualGridCellComponent.getHeight()).isEqualTo(expectedHeight);
  }
}
