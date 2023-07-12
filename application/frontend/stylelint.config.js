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

module.exports = {
  extends: ['stylelint-config-standard-scss', 'stylelint-config-prettier-scss'],
  rules: {
    'scss/at-extend-no-missing-placeholder': null, // TODO use placeholders with @extend, i.e. %mt-2
    'scss/dollar-variable-empty-line-before': null, // this disallows grouping SASS variables
    'scss/dollar-variable-pattern': null, // TODO fix names of variables
    'scss/at-mixin-pattern': null, // TODO fix names of mixins
    'scss/at-function-pattern': null, // TODO fix names of functions
    'selector-class-pattern': null, // not compatible with BEM
    'at-rule-empty-line-before': null, // does not make sense because we use a lot of @extends
    'declaration-empty-line-before': null, // this disallows grouping CSS declarations
    'color-hex-length': null, // no benefits from shortening the colors, it makes comparison with design harder
  },
  ignoreFiles: ['src/main/resources/**/*'],
};
