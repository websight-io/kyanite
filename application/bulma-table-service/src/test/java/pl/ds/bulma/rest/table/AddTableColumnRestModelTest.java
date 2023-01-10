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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pl.ds.websight.request.parameters.support.impl.injectors.RequestParameterInjector;

@ExtendWith(SlingContextExtension.class)
public class AddTableColumnRestModelTest {

  private static final String PATH = "/content/table";
  private static final String TABLE = PATH + "/complex";

  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.registerService(new RequestParameterInjector());
    context.addModelsForClasses(AddTableColumnRestModel.class);
    context.load().json(requireNonNull(
        Thread.currentThread().getContextClassLoader().getResourceAsStream("table.json")), PATH);
  }

  @Test
  void addTableColumnRestModelTest() {
    context.request().setResource(
        context.resourceResolver().getResource(TABLE + "/tablerow/tablecell"));
    AddTableColumnRestModel model = context.request().adaptTo(AddTableColumnRestModel.class);
    List<String> expected = Arrays.asList(
        TABLE + "/tablerow",
        TABLE + "/tablehead/tablerow",
        TABLE + "/tablebody/tablerow",
        TABLE + "/tablefoot/tablerow");

    List<String> actual = model.getRows().stream().map(Resource::getPath)
        .collect(Collectors.toList());
    assertEquals(expected, actual);
  }

}
