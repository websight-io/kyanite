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

import {AuthorInfoDialogConfiguration} from "./authorInfoDialogConfiguration";
import { DialogFieldTest } from "../dialog/dialogFieldTest";
import {DialogConfiguration} from "../dialog/dialogConfiguration";
import {AuthorInfoDialog} from "./authorInfoDialog";

/**
 * 'authorPage' source type requires only one field - reference to an author page
 */
export class AuthorInfoDialogConfigurationAuthorPage extends AuthorInfoDialogConfiguration {

  protected getSourceType(): string {
      return AuthorInfoDialog.sourceTypes.authorPage;
  }

  protected getTests(): DialogFieldTest[] {
    return[
        DialogConfiguration.generateTest(AuthorInfoDialog.fields.AuthorPageLink)
    ]
  }
}
