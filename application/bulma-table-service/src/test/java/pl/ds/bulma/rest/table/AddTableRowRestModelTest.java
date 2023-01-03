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

import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.ds.websight.request.parameters.support.impl.injectors.RequestParameterInjector;

@ExtendWith({SlingContextExtension.class, MockitoExtension.class})
public class AddTableRowRestModelTest {

  private static final String PATH = "/content/table";

  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  private MockSlingHttpServletRequest request;

  @BeforeEach
  public void init() {
    context.registerService(new RequestParameterInjector());
    context.addModelsForClasses(AddTableRowRestModel.class);
    context.load().json(requireNonNull(
        Thread.currentThread().getContextClassLoader().getResourceAsStream("table.json")), PATH);
    request = context.request();
    request.setMethod(HttpConstants.METHOD_POST);
  }

  @Test
  void testAddTableRowRestModel_When_tableRowSelected() {
    String expected = "/content/table/simple/tablerow1";
    request.setResource(context.resourceResolver().getResource(expected));
    AddTableRowRestModel model = request.adaptTo(AddTableRowRestModel.class);
    assertEquals(expected, model.getSelectedRow().getPath());
  }

  @Test
  void testAddTableRowRestModel_When_tableHeadCellSelected() {
    String expected = "/content/table/simple/tablerow1";
    request.setResource(context.resourceResolver().getResource(expected + "/tableheadcell1"));
    AddTableRowRestModel model = request.adaptTo(AddTableRowRestModel.class);
    assertEquals(expected, model.getSelectedRow().getPath());
  }

  @Test
  void testAddTableRowRestModel_When_tableCellSelected() {
    String expected = "/content/table/simple/tablerow2";
    request.setResource(context.resourceResolver().getResource(expected + "/tablecell1"));
    AddTableRowRestModel model = request.adaptTo(AddTableRowRestModel.class);
    assertEquals(expected, model.getSelectedRow().getPath());
  }

}
