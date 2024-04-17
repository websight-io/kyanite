/*
 * Copyright (C) 2024 Dynamic Solutions
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

/**
 *  A dialog field or any other element (e.g. tab header) that requires action on it while testing.
 *  Class hierarchy is used to choose the corresponding action to be applied to field
 *  (e.g. click, typing some text etc.)
 */
export abstract class DialogElement {

  readonly label: string;

  constructor(label: string) {
    this.label = label;
  }

  protected abstract getTestIdPrefix(): string;
  protected abstract getTestIdSuffix(): string;

  /**
   * Get TestId for the dialog field.
   * WebSight builds data-tesid as [field type]_[field label, no whitespaces, Title case]
   * Example:
   *  - input 'text Property'  -> data-testid='Input_Textproperty'
   */
  public getTestId() {
    return this.getTestIdPrefix() + "_" + this.getTestIdSuffix();
  }

}
