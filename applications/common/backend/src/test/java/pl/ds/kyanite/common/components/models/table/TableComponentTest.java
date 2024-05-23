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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.IterableUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SlingContextExtension.class)
public class TableComponentTest {

  private static final String PATH = "/content/table";

  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(TableComponent.class);
  }

  @Test
  void defaultTableComponentModelTest() {
    // given
    importTestTableResource();

    // when
    TableComponent model = toTableModel(PATH + "/default");

    // then
    assertThat(model).isNotNull();
    assertThat(model.getTableClasses()).isEmpty();
    assertThat(model.isScrollable()).isFalse();
  }

  @Test
  void tableComponentTest() {
    // given
    importTestTableResource();

    // when
    TableComponent model = toTableModel(PATH + "/complex");

    // then
    assertThat(model.isScrollable()).isTrue();
    assertThat(model.getTableClasses()).containsExactlyInAnyOrder("is-bordered", "is-striped",
        "is-narrow", "is-hoverable", "is-fullwidth", "is-scrollable");
  }

  @Test
  void shouldCalculateInitialColspansForTableTemplate() throws IOException {
    // given
    String jcrPath = "/content/table/template";
    importMainTableTemplateResource(jcrPath);

    // when
    toTableModel(jcrPath);

    // then
    Resource tableResource = context.resourceResolver().getResource(jcrPath);
    List<Resource> tableCellResources = new ArrayList<>();
    for (Resource tableHeadOrBody : IterableUtils.toList(tableResource.getChildren())) {
      for (Resource tableRow : IterableUtils.toList(tableHeadOrBody.getChildren())) {
        tableCellResources.addAll(IterableUtils.toList(tableRow.getChildren()));
      }
    }

    // currently the template for table is 3 rows x 3 columns
    assertThat(tableCellResources)
            .hasSize(9)
            .extracting(cell -> cell.adaptTo(TableCellComponent.class))
            .extracting(TableCellComponent::getColspan)
            .allMatch(colspan -> colspan.equals(4)); // 4, because currently default summary colspan of each row is 12
  }

  private void importTestTableResource() {
    context.load().json(requireNonNull(
            Thread.currentThread().getContextClassLoader().getResourceAsStream("table.json")), PATH);
  }

  private void importMainTableTemplateResource(String jcrPath) throws IOException {
    File tableTemplateFile = new File("src/main/resources/libs/kyanite/common/components/table/template/.content.json");
    assertThat(tableTemplateFile).exists();

    try (InputStream fileStream = new FileInputStream(tableTemplateFile)) {
      context.load().json(fileStream, jcrPath);
    }
  }

  private TableComponent toTableModel(String tableResourcePath) {
    return context.resourceResolver()
            .getResource(tableResourcePath)
            .adaptTo(TableComponent.class);
  }
}
