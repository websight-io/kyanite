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

module.exports = {
  'env': {
    'browser': true,
    'es2021': true,
  },
  'overrides': [
  ],
  'parserOptions': {
    'ecmaVersion': 'latest',
    'sourceType': 'module',
  },
  'plugins': [
    'react',
  ],
  rules:  {
    'curly': 1,
    '@typescript-eslint/explicit-function-return-type': [0],
    '@typescript-eslint/no-explicit-any': [0],
    'ordered-imports': [0],
    'object-literal-sort-keys': [0],
    'max-len': [1, 120],
    'new-parens': 1,
    'no-bitwise': 1,
    'no-cond-assign': 1,
    'no-trailing-spaces': 0,
    'eol-last': 1,
    'func-style': ['error', 'declaration', { 'allowArrowFunctions': true }],
    'semi': 1,
    'no-var': 0
  },
};
