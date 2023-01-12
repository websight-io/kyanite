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

package pl.ds.bulma.actions.table;

import java.util.Arrays;
import org.osgi.service.component.annotations.Component;
import pl.ds.websight.ui.framework.actions.service.WebAction;
import pl.ds.websight.ui.framework.actions.service.WebActionConfig;

@Component
public class AddTableColumnBeforeWebAction implements WebAction {

  private static final WebActionConfig CONFIG = WebActionConfig.Builder
      .newWebActionConfig(
          "/apps/bulma-table-view/web-resources/actions/AddTableColumnBeforeAction.js")
      .forAllModules()
      .forResourceTypes(
          Arrays.asList("bulma/components/table/tableheadcell", "bulma/components/table/tablecell"))
      .forViewTypes("page-editor-toolbar")
      .withRanking(1200)
      .build();

  @Override
  public WebActionConfig getConfig() {
    return CONFIG;
  }
}
