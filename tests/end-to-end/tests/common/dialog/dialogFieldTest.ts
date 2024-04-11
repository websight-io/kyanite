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

import {DialogElement} from "./elements/dialogElement";
import {JcrPropertyValidator} from "../validation/jcrPropertyValidator";

/**
 * Contains necessary data for testing dialog property saving
 *  - label   - is involved in generating element's testid - will be used by Cypress to find it
 *  - jcrName - JCR subpath of corresponding property
 *  - value   - value that we assign to the field and expect to be saved in JCR
 *  - element - dialog element serving as input for the property
 */
export class DialogFieldTest {

  readonly  label:   string;
  readonly  jcrName: string;
  private   _value:  string;
  readonly  element: DialogElement;

  constructor(label: string, jcrName: string, field: DialogElement, value='not set') {
    this.label   = label;
    this.jcrName = jcrName;
    this.element = field;
    this._value  = value;
  }

  public set value(v: string) {
    this._value = v;
  }
  public get value(){
    return this._value;
  }

  /**
   * Compare the value that we expect in JCR with the actual result.
   * Obviously should be call after applying the value
   */
  public validateResult(validator: JcrPropertyValidator) {
    validator.validate(this.jcrName, this.value);
  }

}
