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

  constructor(components, editor, config) {
    this.components = components;
    this.editor = editor;
    this.config = config;
  }

  get metadata() {
    return this.config?.metadata;
  }

  getConditions() {
    return [{
      isMatching: components => this.config?.allowedComponents.includes(
          [...components][0].componentDefinition.path)
    }];
  }

  get ranking() {
    return this.config?.ranking;
  }

  execute() {
    performTableRestAction(this.editor,
        `${this.editor.editedPage.contentPath}/${[...this.components][0].path}`,
        this.config?.action, this.config?.insertBefore);
  }
}