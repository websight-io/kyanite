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

import './commands';
import '@percy/cypress';
import { SnapshotOptions } from '@percy/core';
import { ExecutableOperation, WebSightAction } from './ws-action';

declare global {
  // eslint-disable-next-line @typescript-eslint/no-namespace
  namespace Cypress {
    interface Chainable {
      getPageIframe(): Chainable<any>;
      getByTestId(testId: string): Chainable<JQuery<HTMLElement>>;
      findByTestId(testId: string): Chainable<JQuery<HTMLElement>>;
      dragByTestId(targetTestId: string): void;
      listByTestIdPrefix(testIdPrefix: string): Chainable<JQuery<HTMLElement>>;
      saveDataAttrAsNumber(attrName: string, alias: string): void;
      shouldAttrAsNumber(
        attrName: string,
        chainer: string,
        alias: string
      ): void;
      login(): void;
      percySnapshotWithAuth(name: string, options?: SnapshotOptions): void;
      percySnapshotPreview(name: string, options?: SnapshotOptions): void;
      percySnapshotPageEditor(name: string, options?: SnapshotOptions): void;
      percySnapshotDialog(name?: string, options?: SnapshotOptions): void;
    }
  }
}
