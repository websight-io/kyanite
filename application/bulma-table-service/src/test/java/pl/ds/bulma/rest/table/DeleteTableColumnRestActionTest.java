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

import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pl.ds.websight.rest.framework.RestActionResult;

@ExtendWith(SlingContextExtension.class)
public class DeleteTableColumnRestActionTest {

  private static final String PATH = "/content/table";

  private final SlingContext context = new SlingContext(ResourceResolverType.JCR_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(DeleteTableRowRestModel.class);
    context.load().json(requireNonNull(
            Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("deletetablecolumn.json")),
        PATH);
  }

  @Test
  void testDeleteTableColumnRestAction() {
    context.request().setResource(context.resourceResolver().getResource(PATH + "/tablerow/tablecell"));
    DeleteTableColumnRestModel model = requireNonNull(
        context.request().adaptTo(DeleteTableColumnRestModel.class));
    DeleteTableColumnRestAction restAction = new DeleteTableColumnRestAction();
    RestActionResult<String> result = restAction.perform(model);
    assertThat(result.getStatus()).isEqualTo(SUCCESS);
  }

}
