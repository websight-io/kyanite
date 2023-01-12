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
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pl.ds.websight.request.parameters.support.impl.injectors.RequestParameterInjector;
import pl.ds.websight.rest.framework.RestActionResult;

@ExtendWith(SlingContextExtension.class)
public class AddTableColumnRestActionTest {

  private static final String PATH = "/content/table";
  private static final String TABLE = PATH + "/simple";
  private static final String ROW1 = TABLE + "/tablerow";
  private static final String ROW2 = TABLE + "/tablerow0";
  private static final String CELL1 = "/tablecell";
  private static final String CELL2 = "/tablecell0";

  private final SlingContext context = new SlingContext(ResourceResolverType.JCR_MOCK);

  @BeforeEach
  public void init() {
    context.registerInjectActivateService(new RequestParameterInjector());
    context.addModelsForClasses(AddTableColumnRestModel.class);
    context.load().json(requireNonNull(
        Thread.currentThread().getContextClassLoader().getResourceAsStream("table.json")), PATH);
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  void testAddTableColumnRestAction(boolean insertBefore) {
    context.request().addRequestParameter("insertBefore", String.valueOf(insertBefore));
    AddTableColumnRestModel model = getModel(ROW1 + "/tablecell");
    AddTableColumnRestAction addTableColumnRestAction = new AddTableColumnRestAction();
    RestActionResult<String> result = addTableColumnRestAction.perform(model);
    assertThat(result.getStatus()).isEqualTo(SUCCESS);
    Iterator<Resource> row1Iterator = getResource(ROW1).listChildren();
    Iterator<Resource> row2Iterator = getResource(ROW2).listChildren();
    assertThat(row1Iterator.next().getPath()).isEqualTo(ROW1 + (insertBefore ? CELL2 : CELL1));
    assertThat(row1Iterator.next().getPath()).isEqualTo(ROW1 + (insertBefore ? CELL1 : CELL2));
    assertThat(row2Iterator.next().getPath()).isEqualTo(ROW2 + (insertBefore ? CELL2 : CELL1));
    assertThat(row2Iterator.next().getPath()).isEqualTo(ROW2 + (insertBefore ? CELL1 : CELL2));
    assertThat(row1Iterator.hasNext()).isFalse();
    assertThat(row2Iterator.hasNext()).isFalse();
  }

  private AddTableColumnRestModel getModel(String resourcePath) {
    context.request().setResource(getResource(resourcePath));
    return requireNonNull(context.request().adaptTo(AddTableColumnRestModel.class));
  }

  private Resource getResource(String resourcePath) {
    ResourceResolver resourceResolver = requireNonNull(context.resourceResolver());
    return resourceResolver.getResource(resourcePath);
  }

}
