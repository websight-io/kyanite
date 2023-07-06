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

import RestClient from 'restclient';

function findTable(component) {
  while (component.parent) {
    if (component.domNodes[0].tagName === 'TABLE') {
      return component;
    }
    component = component.parent;
  }
  return null;
}

export const performTableRestAction = (editor, components, action,
    insertBefore) => {
  const restClient = new RestClient('bulma-table-service');
  const resourcePath = [...components][0].path;
  const table = findTable([...components][0]);
  const config = {
    resourcePath,
    action,
    onSuccess: () => editor.refreshComponentTree(new Set([table]))
  };
  if (insertBefore !== 'undefined') {
    config.data = {insertBefore};
  }
  restClient.post(config);
};
