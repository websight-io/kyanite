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

package pl.ds.bulma.components.models.table;

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

  private static final String PATH = "/content/tablecell";

  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(TableCellComponent.class);
    context.load().json(requireNonNull(
            Thread.currentThread().getContextClassLoader().getResourceAsStream("tablecell.json")),
        PATH);
  }

  @Test
  void defaultTableCellComponentModelTest() {
    TableCellComponent model = context.resourceResolver().getResource(PATH + "/default")
        .adaptTo(TableCellComponent.class);
    assertThat(model).isNotNull();
    assertThat(model.getText()).isEqualTo("Content");
    assertThat(model.getRowspan()).isEqualTo(1);
    assertThat(model.getColspan()).isEqualTo(1);
  }

  @Test
  void tableCellComponentModelTest() {
    TableCellComponent model = context.resourceResolver().getResource(PATH + "/complex")
        .adaptTo(TableCellComponent.class);
    assertThat(model.getText()).isEqualTo("Table cell text");
    assertThat(model.getRowspan()).isEqualTo(2);
    assertThat(model.getColspan()).isEqualTo(3);
  }
}
