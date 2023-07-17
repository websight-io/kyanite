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

import java.util.List;
import java.util.stream.Collectors;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SlingContextExtension.class)
public class DeleteTableRowRestModelTest {

  private static final String PATH = "/content/table";

  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(DeleteTableRowRestModel.class);
    context.load().json(requireNonNull(
            Thread.currentThread().getContextClassLoader().getResourceAsStream("deletetablerow.json")),
        PATH);
  }

  @Test
  void testDeleteTableRowRestModel() {
    String expected = PATH + "/tablerow/tablecell";
    context.request().setResource(context.resourceResolver().getResource(expected));
    DeleteTableRowRestModel model = context.request().adaptTo(DeleteTableRowRestModel.class);
    assertThat(model.getRowParent().getPath()).isEqualTo(PATH);
    assertThat(model.getRowspanStartPosition()).isEqualTo(1);
    assertThat(model.getNextRow()).isNull();
    assertThat(model.getRowspanEndPosition()).isEqualTo(4);
    List<String> rowsToDelete = model.getRowsToDelete().stream().map(r -> r.getPath())
        .collect(Collectors.toList());
    assertThat(rowsToDelete).containsExactlyInAnyOrder(PATH + "/tablerow", PATH + "/tablerow0",
        PATH + "/tablerow1", PATH + "/tablerow2");
  }

}
