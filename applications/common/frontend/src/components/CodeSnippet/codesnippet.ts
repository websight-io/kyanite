/*!
 * <!--
 *     Copyright (C) 2023 Dynamic Solutions
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 * -->
 */

import hljs from 'highlight.js/lib/core';
import xml from 'highlight.js/lib/languages/xml';
import json from 'highlight.js/lib/languages/json';
import java from 'highlight.js/lib/languages/java';
import yaml from 'highlight.js/lib/languages/yaml';
import bash from 'highlight.js/lib/languages/bash';
import typescript from 'highlight.js/lib/languages/typescript';
import 'simplebar';

import { onDOMContentLoaded } from '../../helpers/dom';
import { CodeSnippet } from './codesnippet.class';

hljs.registerLanguage('html', xml);
hljs.registerLanguage('json', json);
hljs.registerLanguage('java', java);
hljs.registerLanguage('yaml', yaml);
hljs.registerLanguage('bash', bash);
hljs.registerLanguage('typescript', typescript);

// on page load
onDOMContentLoaded(() => {
  document
    .querySelectorAll<HTMLDivElement>(CodeSnippet.componentSelector)
    .forEach((element) => {
      new CodeSnippet(element);
    });
});

// on editor component reload
/**
 * @todo https://teamds.atlassian.net/browse/WS-2744
 *       When `event` will have new component reference try to (so we wont need querySelector to find it):
 *       - remove `cssSelectorNotActive`
 *       - remove `component`
 *       - use `event.detail` and from it take the new component reference
 *
 * @see https://drive.google.com/file/d/1FSuKUJyLUavhON3i88_jx_uhU-8lulaC/view?usp=sharing
 */
document.addEventListener(
  'editor-component-dom-updated',
  (/* event: OnDomContentUpdatedEvent */) => {
    const cssSelectorNotActive = `${CodeSnippet.componentSelector}:not(.${CodeSnippet.cssClassIsInitialised})`;

    const componentElementsToRedraw =
      document.querySelectorAll<HTMLDivElement>(cssSelectorNotActive);

    Array.from(componentElementsToRedraw).forEach((elementToRedraw) => {
      new CodeSnippet(elementToRedraw);
    });
  }
);
