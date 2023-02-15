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

import {performTableRestAction} from "./common.js";

export default class TableActionsComponentsAction {

  name = 'Add Table Row';
  ranking = 2000;
  metadata = {
    title: 'Add table row',
    icon: 'keyboard_arrow_down'
  }

  constructor(components, editor) {
    this.components = components;
    this.editor = editor;
  }

  getConditions() {
    return [{
      isMatching: components => ["/apps/bulma/components/table/tablerow",
        "/apps/bulma/components/table/tableheadcell",
        "/apps/bulma/components/table/tablecell"].includes(
          [...components][0].componentDefinition.path)
    }];
  }

  execute() {
    performTableRestAction(this.editor, [...this.components][0].path, 'add-table-row', false);
  }
}