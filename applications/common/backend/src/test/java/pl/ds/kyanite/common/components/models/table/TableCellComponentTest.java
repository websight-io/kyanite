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

package pl.ds.kyanite.common.components.models.table;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SlingContextExtension.class)
public class TableCellComponentTest {

  private static final String PATH = "/content/table";

  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(TableCellComponent.class);
    context.load().json(requireNonNull(
            Thread.currentThread().getContextClassLoader().getResourceAsStream("tablecell.json")),
        PATH);
  }

  @Test
  void shouldUseDefaultsWhenNoConfigurationSet() {
    final TableCellComponent model = createModel("tablebody/tablerow1/default");

    assertThat(model).isNotNull();
    assertThat(model.getText()).isEqualTo("Content");
    assertThat(model.getRowspan()).isEqualTo(1);
    assertThat(model.getColspan()).isEqualTo(12);
    assertThat(model.getLeftIcon()).isEmpty();
    assertThat(model.getRightIcon()).isEmpty();
  }

  @Test
  void shouldUseAuthorProvidedConfigurationWhenSet() {
    final TableCellComponent model = createModel("tablebody/tablerow2/complex");

    assertThat(model).isNotNull();
    assertThat(model.getText()).isEqualTo("Table cell text");
    assertThat(model.getRowspan()).isEqualTo(2);
    assertThat(model.getColspan()).isEqualTo(3);
  }

  @Test
  void shouldResolveLeftIconWhenEnabled() {
    final TableCellComponent model = createModel("tablebody/tablerow3/withLeftIcon");

    assertThat(model).isNotNull();
    assertThat(model.getLeftIcon()).isNotEmpty();
    assertThat(model.getRightIcon()).isEmpty();
  }

  @Test
  void shouldResolveRightIconWhenEnabled() {
    final TableCellComponent model = createModel("tablebody/tablerow3/withRightIcon");

    assertThat(model).isNotNull();
    assertThat(model.getLeftIcon()).isEmpty();
    assertThat(model.getRightIcon()).isNotEmpty();
  }

  private TableCellComponent createModel(final String componentResourceSubPath) {
    return context.resourceResolver().getResource(PATH + "/" + componentResourceSubPath)
        .adaptTo(TableCellComponent.class);
  }
}
