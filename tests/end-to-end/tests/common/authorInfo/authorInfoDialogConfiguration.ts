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

import {DialogRadioGroup} from "../dialog/elements/clickable/dialogRadioGroup";
import {DialogConfiguration} from "../dialog/dialogConfiguration";
import {DialogFieldDefinition} from "../dialog/dialogFieldDefinition";
import {DialogFieldTest} from "../dialog/dialogFieldTest";

export abstract class AuthorInfoDialogConfiguration extends DialogConfiguration {

  /**
   * Overwrite the base method, because we always want SourceType switching method first -
   * switching source type makes corresponding set of fields visible.
   */
  protected getTestsQueue(): DialogFieldTest[] {
    const testsQueue = [];

    //  make a test for the radioGroup with corresponding option selected
    const sourceType = this.getSourceType();
    const sourceTypeActivation = DialogConfiguration.generateTest(
        new DialogFieldDefinition(sourceType, 'author/authorInfoSource', DialogRadioGroup),
        [sourceType]);
    sourceTypeActivation.value = sourceType;

    testsQueue.push(sourceTypeActivation);

    //  add normal fields
    const tests = this.getTests();
    testsQueue.push(...tests);
    return testsQueue;
  }

  protected abstract getSourceType(): string;

  /**
   * Should return tests for all fields related to specific sourceType - others will not be visible,
   * so attempts of any actions on them will fail
   */
  protected abstract getTests(): DialogFieldTest[];
}
