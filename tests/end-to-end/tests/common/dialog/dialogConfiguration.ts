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

import {v4 as uuidv4} from 'uuid';

import {DialogAction} from "./actions/dialogAction";
import {DialogActionsQueue} from "./actions/dialogActionsQueue";
import {TypeAction} from "./actions/typeAction";
import {DialogClickableElement} from "./elements/clickable/dialogClickableElement";
import {ClickAction} from "./actions/clickAction";
import {DialogFieldTest} from "./dialogFieldTest";
import {ClassUtils} from "../utils/classUtils";
import {DialogInputField} from "./elements/input/dialogInputField";
import {DialogPagePicker} from "./elements/input/picker/dialogPagePicker";
import {DialogFieldDefinition} from "./dialogFieldDefinition";
import {Dialog} from "./dialog";
import {JcrPropertyValidator} from "../validation/jcrPropertyValidator";

/**
 * Represents a queue of 'tests'. Each 'test' contains a dialog field definition
 * and a value for that field.
 * During initialization, all the fields are assigned a value containing the same unique prefix,
 * to tell one applied configuration from another.
 *
 * Configuration applies itself by converting provided tests to a queue of actions
 * and executing them in open dialog ( watch execute() ).
 *
 * Values provided in tests are then used to validate the result (watch validate(validator) ).
 *
 * Blueprint for testing a dialog:
 *  - create a Dialog class containing all dialog field definitions. You can then use those
 *    to build configurations tests without risk of a mistake in field definition.
 *  - create a configuration:
 *    - create a dedicated class, if set of fields is fixed.
 *    - create configuration on-fly, if you want to test different combinations of fields
 */
export abstract class DialogConfiguration extends DialogAction {

  protected modalDialogSelector: string;
  protected currentTextPrefix: string;
  protected testsQueue: DialogFieldTest[] = [];

  constructor(modalDialogSelector: string) {
    super();
    this.modalDialogSelector = modalDialogSelector;
    this.testsQueue = this.getTestsQueue();
    this.generateValues();
  }

  /**
   * Should return all field tests for specific configurations
   * @protected
   */
  protected abstract getTestsQueue(): DialogFieldTest[];

  public get textPrefix() {
    return this.currentTextPrefix;
  }

  public getTest(field: DialogFieldDefinition): DialogFieldTest {
    const test = this.testsQueue.find(t => t.label == field.label);
    if (!test) throw new Error("Dialog configuration doesn't have a field with label " + field.label);
    return test;
  }

  /**
   * generate values instead of providing them explicitly
   *  - text values are copying field names with addition of configuration-unique string
   *  - asset links are constant
   *  - page links are constant
   */
  private generateValues() {
    const values = {};

    this.currentTextPrefix = uuidv4();

    this.testsQueue.forEach(t => {
      if (ClassUtils.isClass(t.element, DialogInputField)) {
        t.value = this.textPrefix + t.label
      } else if(ClassUtils.isClass(t.element, DialogPagePicker)) {
        t.value = Dialog.defaults.pageLink
      }
      /*  TODO can we predict the asset rendition ?
           Until we can, assetPicker will be considered a usual input field and filled with text */
    })

    return values;
  }

  /**
   * Convert provided or generated values to actions
   * For each dialog property that accepts a value - if value is provided - set the provided value
   */
  protected generateActions() {
    const actionQueue = new DialogActionsQueue();
    this.testsQueue.forEach(t => {
      if (ClassUtils.isClass(t.element, DialogInputField)) {
        actionQueue.add(new TypeAction(t.element.getTestId(), this.modalDialogSelector, t.value));
      } else if (ClassUtils.isClass(t.element, DialogClickableElement)) {
        actionQueue.add(new ClickAction(t.element.getTestId(), this.modalDialogSelector))
      } else {
        console.log("No action can be applied to the field " + t.label + " of type " + t.constructor.name);
      }
    });
    return actionQueue;
  }

  /**
   * Set a value for a dialog field. It will be applied during execute() call
   *
   * @param field dialog field definition
   * @param value property value
   */
  public setValue(field: DialogFieldDefinition, value: string) {
    const test = this.getTest(field);
    test.value = value;
  }

  /**
   * Get value of a dialog field set in this configuration
   */
  public getValue(field: DialogFieldDefinition): string {
    const test = this.getTest(field);
    return test.value;
  }

  /**
   * Apply configuration to dialog
   */
  public execute() {
    this.generateActions().execute();
  }

  /**
   * Compare values that were set in the dialog with result values from JCR repository
   */
  public validateJcrResult(validator: JcrPropertyValidator) {
    this.testsQueue.forEach(t => {
      t.validateResult(validator);
    });
  }

  /**
   * Generate test for provided field
   *
   * @param field field definition
   * @param elementExtraConstructorParams extra constructor params for the dialog element
   */
  protected static generateTest(field: DialogFieldDefinition, elementExtraConstructorParams = []) {
    const constructorParams = [];
    constructorParams.push(field.label);
    constructorParams.push(elementExtraConstructorParams);
    return new DialogFieldTest(field.label, field.jcrName, new field.elementClass(...constructorParams));
  }
}
