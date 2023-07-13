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
import static pl.ds.websight.rest.framework.RestActionResult.Status.SUCCESS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pl.ds.websight.rest.framework.RestActionResult;

@ExtendWith(SlingContextExtension.class)
public class DeleteTableColumnRestActionTest {

  private static final String COLSPAN = "colspan";

  private static final String SELECTED_CELL = String.join("/", TableTestConstants.TABLE_CONTENT_ROOT, TableTestConstants.TABLEBODY,
      TableTestConstants.TABLEROW, TableTestConstants.TABLECELL0);

  private final SlingContext context = new SlingContext(ResourceResolverType.JCR_MOCK);

  RestActionResult<String> result;

  @BeforeEach
  public void init() {
    context.addModelsForClasses(DeleteTableColumnRestModel.class);
    context.load().json(requireNonNull(
            Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("deletetablecolumn.json")),
        TableTestConstants.TABLE_CONTENT_ROOT);
    context.request().setResource(context.resourceResolver().getResource(SELECTED_CELL));
    DeleteTableColumnRestModel model = requireNonNull(
        context.request().adaptTo(DeleteTableColumnRestModel.class));
    DeleteTableColumnRestAction restAction = new DeleteTableColumnRestAction();
    this.result = restAction.perform(model);
  }

  @Test
  void testRestActionResult() {
    assertThat(result.getStatus()).isEqualTo(SUCCESS);
  }

  @Test
  void testTableheadContent() {
    List<String> theadCellsPath = getCellsPath(
        context.resourceResolver().getResource(TableTestConstants.TABLE_CONTENT_ROOT + "/tablehead"));
    List<String> expected = Arrays.asList(
        String.join("/", TableTestConstants.TABLE_CONTENT_ROOT, TableTestConstants.TABLEHEAD, TableTestConstants.TABLEROW, TableTestConstants.TABLECELL),
        String.join("/", TableTestConstants.TABLE_CONTENT_ROOT, TableTestConstants.TABLEHEAD, TableTestConstants.TABLEROW, TableTestConstants.TABLECELL2)
    );
    assertThat(theadCellsPath).isEqualTo(expected);
  }

  @Test
  void testTablebodyContent() {
    List<String> tbodyCellsPath = getCellsPath(
        context.resourceResolver().getResource(TableTestConstants.TABLE_CONTENT_ROOT + "/tablebody"));
    List<String> expected = Arrays.asList(
        String.join("/", TableTestConstants.TABLE_CONTENT_ROOT, TableTestConstants.TABLEBODY, TableTestConstants.TABLEROW, TableTestConstants.TABLECELL),
        String.join("/", TableTestConstants.TABLE_CONTENT_ROOT, TableTestConstants.TABLEBODY, TableTestConstants.TABLEROW, TableTestConstants.TABLECELL1),
        String.join("/", TableTestConstants.TABLE_CONTENT_ROOT, TableTestConstants.TABLEBODY, TableTestConstants.TABLEROW0, TableTestConstants.TABLECELL),
        String.join("/", TableTestConstants.TABLE_CONTENT_ROOT, TableTestConstants.TABLEBODY, TableTestConstants.TABLEROW0, TableTestConstants.TABLECELL1)
    );
    assertThat(tbodyCellsPath).isEqualTo(expected);
    Resource overflowCell = context.resourceResolver()
        .getResource(TableTestConstants.TABLE_CONTENT_ROOT + "/tablebody/tablerow0/tablecell1");
    assertThat(getColspan(overflowCell)).isEqualTo(1);
  }

  @Test
  void testTablefootContent() {
    List<String> tfootCellsPath = getCellsPath(
        context.resourceResolver().getResource(TableTestConstants.TABLE_CONTENT_ROOT + "/tablefoot"));
    List<String> expected = Arrays.asList(
        String.join("/", TableTestConstants.TABLE_CONTENT_ROOT, TableTestConstants.TABLEFOOT, TableTestConstants.TABLEROW, TableTestConstants.TABLECELL)
    );
    assertThat(tfootCellsPath).isEqualTo(expected);
    Resource shadowCell = context.resourceResolver()
        .getResource(String.join("/", TableTestConstants.TABLE_CONTENT_ROOT, TableTestConstants.TABLEFOOT, TableTestConstants.TABLEROW, TableTestConstants.TABLECELL));
    assertThat(getColspan(shadowCell)).isEqualTo(2);
  }

  private List<String> getCellsPath(Resource rowsContainer) {
    List<String> cellsPath = new ArrayList<>();
    Iterator<Resource> rows = rowsContainer.listChildren();
    while (rows.hasNext()) {
      Iterator<Resource> cells = rows.next().listChildren();
      while (cells.hasNext()) {
        cellsPath.add(cells.next().getPath());
      }
    }
    return cellsPath;
  }

  private int getColspan(Resource resource) {
    Integer colspan = resource.getValueMap().get(COLSPAN, Integer.class);
    if (colspan == null) {
      colspan = 1;
    }
    return colspan.intValue();
  }

}
