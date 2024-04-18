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

import {DialogFieldDefinition} from "../dialog/dialogFieldDefinition";
import {DialogInputField} from "../dialog/elements/input/dialogInputField";
import {DialogAssetPicker} from "../dialog/elements/input/picker/dialogAssetPicker";
import {DialogPagePicker} from "../dialog/elements/input/picker/dialogPagePicker";

export class AuthorInfoDialog {

  static readonly sourceTypes = {
    authorPage: 'authorPage',
    parentPage: 'parentPage',
    ownProperties: 'ownProperties'
  }

  static readonly fields = {
    AuthorName:           new DialogFieldDefinition("Author name", "author/authorName", DialogInputField),
    AuthorDescription:    new DialogFieldDefinition("Author description", "author/authorDescription", DialogInputField),
    AuthorRole:           new DialogFieldDefinition("Author role", "author/authorRole", DialogInputField),
    AuthorPhoto:          new DialogFieldDefinition("Author photo", "author/authorPhoto", DialogAssetPicker),
    AuthorPhotoAlt:       new DialogFieldDefinition("Author photo alt", "author/authorPhotoAlt", DialogInputField),
    AuthorPageLink:       new DialogFieldDefinition("Link to the author content page", "author/authorPageLink", DialogPagePicker)
  }

}
