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

import TableActionsComponentsAction
  from "../actions/TableActionsComponentsAction.js";

const config = {
  name: 'addTableRowBefore',
  ranking: 1400,
  metadata: {
    title: 'Add row before',
    icon: 'keyboard_arrow_up',
    testId: 'AddTableRowBefore',
  },
  action: 'add-table-row',
  insertBefore: true,
  allowedComponents: [
    "/libs/bulma/components/table/tablerow",
    "/libs/bulma/components/table/tableheadcell",
    "/libs/bulma/components/table/tablecell"
  ]
};

export default {
  init: (editor) => {
    editor.componentsActions.addProvider({
      getActions: components => [new TableActionsComponentsAction(components,
          editor, config)]
    });
  }
};
