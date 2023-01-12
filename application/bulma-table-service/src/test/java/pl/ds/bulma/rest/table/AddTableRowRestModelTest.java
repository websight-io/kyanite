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
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pl.ds.websight.request.parameters.support.impl.injectors.RequestParameterInjector;

@ExtendWith(SlingContextExtension.class)
public class AddTableRowRestModelTest {

  private static final String PATH = "/content/table";
  private static final String TABLE = PATH + "/simple";

  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.registerService(new RequestParameterInjector());
    context.addModelsForClasses(AddTableRowRestModel.class);
    context.load().json(requireNonNull(
        Thread.currentThread().getContextClassLoader().getResourceAsStream("table.json")), PATH);
  }

  @Test
  void testAddTableRowRestModel_When_tableRowSelected() {
    String expected = TABLE + "/tablerow";
    context.request().setResource(context.resourceResolver().getResource(expected));
    AddTableRowRestModel model = context.request().adaptTo(AddTableRowRestModel.class);
    assertEquals(expected, model.getSelectedRow().getPath());
  }

  @Test
  void testAddTableRowRestModel_When_tableHeadCellSelected() {
    String expected = TABLE + "/tablerow";
    context.request().setResource(context.resourceResolver().getResource(expected + "/tablecell"));
    AddTableRowRestModel model = context.request().adaptTo(AddTableRowRestModel.class);
    assertEquals(expected, model.getSelectedRow().getPath());
  }

  @Test
  void testAddTableRowRestModel_When_tableCellSelected() {
    String expected = TABLE + "/tablerow0";
    context.request().setResource(context.resourceResolver().getResource(expected + "/tablecell"));
    AddTableRowRestModel model = context.request().adaptTo(AddTableRowRestModel.class);
    assertEquals(expected, model.getSelectedRow().getPath());
  }

}
