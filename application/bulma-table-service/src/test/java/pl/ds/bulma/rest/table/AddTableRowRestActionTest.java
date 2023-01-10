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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pl.ds.websight.request.parameters.support.impl.injectors.RequestParameterInjector;
import pl.ds.websight.rest.framework.RestActionResult;

@ExtendWith(SlingContextExtension.class)
public class AddTableRowRestActionTest {

  private static final String PATH = "/content/table";
  private static final String TABLE = PATH + "/simple";
  private static final String ROW1 = TABLE + "/tablerow";
  private static final String ROW2 = TABLE + "/tablerow0";
  private static final String ROW3 = TABLE + "/tablerow1";

  private final SlingContext context = new SlingContext(ResourceResolverType.JCR_MOCK);

  @BeforeEach
  public void init() {
    context.registerService(new RequestParameterInjector());
    context.addModelsForClasses(AddTableRowRestModel.class);
    context.load().json(requireNonNull(
        Thread.currentThread().getContextClassLoader().getResourceAsStream("table.json")), PATH);
  }

  @ParameterizedTest
  @ValueSource(strings = {ROW1, ROW1 + "/tablecell"})
  void testAddTableRowRestAction(String selectedResource) {
    AddTableRowRestModel model = getModel(selectedResource);
    RestActionResult<String> result = performRestAction(model);
    assertThat(result.getStatus()).isEqualTo(SUCCESS);
    Iterator<Resource> rows = model.getSelectedRow().getParent().listChildren();
    assertThat(rows.next().getPath()).isEqualTo(ROW1);
    assertThat(rows.next().getPath()).isEqualTo(ROW3);
    assertThat(rows.next().getPath()).isEqualTo(ROW2);
    assertThat(rows.hasNext()).isFalse();
  }

  @Test
  void testAddTableRowRestAction_When_insertBefore() {
    context.request().addRequestParameter("insertBefore", "true");
    AddTableRowRestModel model = getModel(ROW1);
    RestActionResult<String> result = performRestAction(model);
    assertThat(result.getStatus()).isEqualTo(SUCCESS);
    Iterator<Resource> rows = model.getSelectedRow().getParent().listChildren();
    assertThat(rows.next().getPath()).isEqualTo(ROW3);
    assertThat(rows.next().getPath()).isEqualTo(ROW1);
    assertThat(rows.next().getPath()).isEqualTo(ROW2);
    assertThat(rows.hasNext()).isFalse();
  }

  private RestActionResult<String> performRestAction(AddTableRowRestModel model) {
    AddTableRowRestAction addTableRowRestAction = new AddTableRowRestAction();
    return addTableRowRestAction.perform(model);
  }

  private AddTableRowRestModel getModel(String resourcePath) {
    context.request().setResource(
        context.resourceResolver().getResource(resourcePath));
    return requireNonNull(
        context.request().adaptTo(AddTableRowRestModel.class));
  }

}
