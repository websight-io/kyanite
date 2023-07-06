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

package pl.ds.kyanite.rest.table;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.ds.kyanite.rest.table.TableTestConstants.TABLEBODY;
import static pl.ds.kyanite.rest.table.TableTestConstants.TABLECELL;
import static pl.ds.kyanite.rest.table.TableTestConstants.TABLECELL0;
import static pl.ds.kyanite.rest.table.TableTestConstants.TABLECELL1;
import static pl.ds.kyanite.rest.table.TableTestConstants.TABLECELL2;
import static pl.ds.kyanite.rest.table.TableTestConstants.TABLEFOOT;
import static pl.ds.kyanite.rest.table.TableTestConstants.TABLEHEAD;
import static pl.ds.kyanite.rest.table.TableTestConstants.TABLEROW;
import static pl.ds.kyanite.rest.table.TableTestConstants.TABLEROW0;
import static pl.ds.kyanite.rest.table.TableTestConstants.TABLE_CONTENT_ROOT;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SlingContextExtension.class)
public class DeleteTableColumnRestModelTest {

  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  private static final String SELECTED_CELL = String.join("/", TABLE_CONTENT_ROOT, TABLEBODY,
      TABLEROW, TABLECELL0);

  private DeleteTableColumnRestModel model;

  @BeforeEach
  public void init() {
    context.addModelsForClasses(DeleteTableColumnRestModel.class);
    context.load().json(requireNonNull(
            Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("deletetablecolumn.json")),
        TABLE_CONTENT_ROOT);
    context.request().setResource(context.resourceResolver().getResource(SELECTED_CELL));
    this.model = context.request().adaptTo(DeleteTableColumnRestModel.class);
  }

  @Test
  void testDeletePositions() {
    assertThat(model.getColDeleteStart()).isEqualTo(2);
    assertThat(model.getColDeleteEnd()).isEqualTo(3);
  }

  @Test
  void testTableRows() {
    List<String> expected = Arrays.asList(
        String.join("/", TABLE_CONTENT_ROOT, TABLEHEAD, TABLEROW),
        String.join("/", TABLE_CONTENT_ROOT, TABLEBODY, TABLEROW),
        String.join("/", TABLE_CONTENT_ROOT, TABLEBODY, TABLEROW0),
        String.join("/", TABLE_CONTENT_ROOT, TABLEFOOT, TABLEROW));
    assertThat(
        model.getRows().stream().map(r -> r.getPath()).collect(Collectors.toList())).isEqualTo(
        expected);
  }

  @Test
  void testCellsToDelete() {
    List<String> expected = Arrays.asList(
        String.join("/", TABLE_CONTENT_ROOT, TABLEHEAD, TABLEROW, TABLECELL0),
        String.join("/", TABLE_CONTENT_ROOT, TABLEHEAD, TABLEROW, TABLECELL1),
        String.join("/", TABLE_CONTENT_ROOT, TABLEBODY, TABLEROW, TABLECELL0),
        String.join("/", TABLE_CONTENT_ROOT, TABLEBODY, TABLEROW0, TABLECELL0),
        String.join("/", TABLE_CONTENT_ROOT, TABLEBODY, TABLEROW0, TABLECELL1));
    assertThat(model.getCellsToDelete().stream().map(c -> c.getPath())
        .collect(Collectors.toList())).isEqualTo(expected);
  }

  @Test
  void testTableMap() {
    Map<Integer, Map<Integer, Resource>> tableMap = model.getTableMap();

    List<String> tableMapString = tableMap.entrySet().stream()
        .flatMap(entry -> entry.getValue().entrySet()
            .stream()
            .map(innerEntry -> entry.getKey() + "," + innerEntry.getKey() + "," + (
                innerEntry.getValue() != null ? innerEntry.getValue().getPath() : "null")))
        .collect(Collectors.toList());

    List<String> expected = Arrays.asList(
        "1,1," + String.join("/", TABLE_CONTENT_ROOT, TABLEHEAD, TABLEROW, TABLECELL),
        "1,2," + String.join("/", TABLE_CONTENT_ROOT, TABLEHEAD, TABLEROW, TABLECELL0),
        "1,3," + String.join("/", TABLE_CONTENT_ROOT, TABLEHEAD, TABLEROW, TABLECELL1),
        "1,4," + String.join("/", TABLE_CONTENT_ROOT, TABLEHEAD, TABLEROW, TABLECELL2),
        "2,1," + String.join("/", TABLE_CONTENT_ROOT, TABLEBODY, TABLEROW, TABLECELL),
        "2,2," + String.join("/", TABLE_CONTENT_ROOT, TABLEBODY, TABLEROW, TABLECELL0),
        "2,3,null",
        "2,4," + String.join("/", TABLE_CONTENT_ROOT, TABLEBODY, TABLEROW, TABLECELL1),
        "3,1," + String.join("/", TABLE_CONTENT_ROOT, TABLEBODY, TABLEROW0, TABLECELL),
        "3,2," + String.join("/", TABLE_CONTENT_ROOT, TABLEBODY, TABLEROW0, TABLECELL0),
        "3,3," + String.join("/", TABLE_CONTENT_ROOT, TABLEBODY, TABLEROW0, TABLECELL1),
        "3,4,null",
        "4,1," + String.join("/", TABLE_CONTENT_ROOT, TABLEFOOT, TABLEROW, TABLECELL),
        "4,2,null",
        "4,3,null",
        "4,4,null");

    assertThat(tableMapString).isEqualTo(expected);
  }

}
