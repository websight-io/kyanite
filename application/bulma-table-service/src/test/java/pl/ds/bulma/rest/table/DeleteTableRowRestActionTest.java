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

package pl.ds.bulma.rest.table;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.ds.websight.rest.framework.RestActionResult.Status.SUCCESS;

import java.util.Iterator;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pl.ds.websight.rest.framework.RestActionResult;

@ExtendWith(SlingContextExtension.class)
public class DeleteTableRowRestActionTest {

  private static final String ROWSPAN = "rowspan";
  private static final String PATH = "/content/table";
  private static final String ROW1 = PATH + "/tablerow";
  private static final String ROW2 = PATH + "/tablerow0";
  private static final String ROW3 = PATH + "/tablerow1";
  private static final String ROW4 = PATH + "/tablerow2";

  private final SlingContext context = new SlingContext(ResourceResolverType.JCR_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(DeleteTableRowRestModel.class);
    context.load().json(requireNonNull(
            Thread.currentThread().getContextClassLoader().getResourceAsStream("deletetablerow.json")),
        PATH);
  }

  @Test
  void testDeleteTableRowRestAction_SingleRow() {
    context.request().setResource(context.resourceResolver().getResource(ROW1 + "/tablecell0"));
    DeleteTableRowRestModel model = requireNonNull(
        context.request().adaptTo(DeleteTableRowRestModel.class));
    DeleteTableRowRestAction restAction = new DeleteTableRowRestAction();
    RestActionResult<String> result = restAction.perform(model);
    assertThat(result.getStatus()).isEqualTo(SUCCESS);

    Iterator<Resource> rows = model.getRowParent().listChildren();
    assertThat(rows.next().getPath()).isEqualTo(ROW2);
    assertThat(rows.next().getPath()).isEqualTo(ROW3);
    assertThat(rows.next().getPath()).isEqualTo(ROW4);
    assertThat(rows.hasNext()).isFalse();

  }

  @Test
  void testDeleteTableRowRestAction_MultipleRow() {
    context.request().setResource(context.resourceResolver().getResource(ROW2 + "/tablecell"));
    DeleteTableRowRestModel model = requireNonNull(
        context.request().adaptTo(DeleteTableRowRestModel.class));
    DeleteTableRowRestAction restAction = new DeleteTableRowRestAction();
    RestActionResult<String> result = restAction.perform(model);
    assertThat(result.getStatus()).isEqualTo(SUCCESS);

    Iterator<Resource> rows = model.getRowParent().listChildren();
    Resource row1 = rows.next();
    assertThat(row1.getPath()).isEqualTo(ROW1);
    //check if shadow cell rowspan is correct
    assertThat(getRowspan(row1.listChildren().next())).isEqualTo(2);

    Resource row4 = rows.next();
    assertThat(row4.getPath()).isEqualTo(ROW4);
    Iterator<Resource> row4Cells = row4.listChildren();
    row4Cells.next();
    //check if overflow cell rowspan is correct
    Resource overflowCell = row4Cells.next();
    assertThat(getRowspan(overflowCell)).isEqualTo(1);
    assertThat(overflowCell.getValueMap().get("text", String.class)).isEqualTo("overflow cell");

    assertThat(rows.hasNext()).isFalse();
  }

  private int getRowspan(Resource resource) {
    Integer rowspan = resource.getValueMap().get(ROWSPAN, Integer.class);
    if (rowspan == null) {
      rowspan = 1;
    }
    return rowspan.intValue();
  }

}
