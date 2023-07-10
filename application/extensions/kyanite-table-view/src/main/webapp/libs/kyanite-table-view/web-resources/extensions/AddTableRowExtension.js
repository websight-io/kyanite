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

import TableActionsComponentsAction
  from "../actions/TableActionsComponentsAction.js";

const config = {
  name: 'addTableRow',
  ranking: 1500,
  metadata: {
    title: 'Add row',
    icon: 'keyboard_arrow_down',
    testId: 'AddTableRow',
  },
  action: 'add-table-row',
  insertBefore: false,
  allowedComponents: [
    "/libs/kyanite/components/table/tablerow",
    "/libs/kyanite/components/table/tableheadcell",
    "/libs/kyanite/components/table/tablecell"
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