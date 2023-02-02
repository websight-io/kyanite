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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SlingContextExtension.class)
public class DeleteTableColumnRestModelTest {

  private static final String TABLE = "/content/table";
  private static final String THEAD = TABLE + "/tablehead";
  private static final String TBODY = TABLE + "/tablebody";
  private static final String TFOOT = TABLE + "/tablefoot";
  private static final String SHADOW_CELL = TFOOT + "/tablerow/tablecell";
  private static final String OVERFLOW_CELL = TBODY + "/tablerow0/tablecell1";

  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(DeleteTableColumnRestModel.class);
    context.load().json(requireNonNull(
            Thread.currentThread().getContextClassLoader().getResourceAsStream("deletetablecolumn.json")),
        TABLE);
  }

  @Test
  void testDeleteTableColumnRestModel() {
    String expected = TBODY + "/tablerow/tablecell0";
    context.request().setResource(context.resourceResolver().getResource(expected));
    DeleteTableColumnRestModel model = context.request().adaptTo(DeleteTableColumnRestModel.class);
    assertThat(model.getColDeleteStart()).isEqualTo(2);
    assertThat(model.getColDeleteEnd()).isEqualTo(3);
    List<String> expectedRows = Arrays.asList(
        THEAD + "/tablerow",
        TBODY + "/tablerow",
        TBODY + "/tablerow0",
        TFOOT + "/tablerow");

    List<String> expectedCellsToDelete = Arrays.asList(
        THEAD + "/tablerow/tablecell0",
        THEAD + "/tablerow/tablecell1",
        TBODY + "/tablerow/tablecell0",
        TBODY + "/tablerow0/tablecell0",
        TBODY + "/tablerow0/tablecell1");
    assertThat(model.getRows().stream().map(r -> r.getPath()).collect(Collectors.toList())).isEqualTo(expectedRows);
    assertThat(model.getCellsToDelete().stream().map(c -> c.getPath()).collect(Collectors.toList())).isEqualTo(expectedCellsToDelete);
    Map<Integer, Map<Integer, Resource>> tableMap = model.getTableMap();

    List<String> tableMapString = tableMap.entrySet().stream()
        .flatMap(entry -> entry.getValue().entrySet()
        .stream()
        .map(innerEntry -> entry.getKey() + "," + innerEntry.getKey() + "," + (innerEntry.getValue() != null ? innerEntry.getValue().getPath() : "null")))
        .collect(Collectors.toList());

    List<String> expectedTableMap = Arrays.asList(
        "1,1," + THEAD + "/tablerow/tablecell",
        "1,2," + THEAD + "/tablerow/tablecell0",
        "1,3," + THEAD + "/tablerow/tablecell1",
        "1,4," + THEAD + "/tablerow/tablecell2",
        "2,1," + TBODY + "/tablerow/tablecell",
        "2,2," + TBODY + "/tablerow/tablecell0",
        "2,3,null",
        "2,4," + TBODY + "/tablerow/tablecell1",
        "3,1," + TBODY + "/tablerow0/tablecell",
        "3,2," + TBODY + "/tablerow0/tablecell0",
        "3,3," + TBODY + "/tablerow0/tablecell1",
        "3,4,null",
        "4,1," + TFOOT + "/tablerow/tablecell",
        "4,2,null",
        "4,3,null",
        "4,4,null");
    assertThat(tableMapString).isEqualTo(expectedTableMap);
  }

}
