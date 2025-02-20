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
  parser: '@typescript-eslint/parser',
  extends: [
    'airbnb-base',
    'plugin:@typescript-eslint/recommended',
    'plugin:@typescript-eslint/recommended-requiring-type-checking',
    'prettier',
  ],
  parserOptions: {
    project: true,
    tsconfigRootDir: __dirname,
  },
  root: true,
  plugins: ['@typescript-eslint', 'import'],
  env: {
    browser: true,
  },
  ignorePatterns: ['**/*.js', '!src/**/*', 'src/main/resources',
    "**/*/emailDecryption.js",
    "**/*/contactForm.js",
    "**/*/dropdown.js",
    "**/*/modal.js",
    "**/*/message.js",
    "**/*/navbar.js",
    "**/*/notification.js",
    "**/*/panelTabs.js",
    "**/*/switchTab.js",
    "**/*/video.js"],
  rules: {
    '@typescript-eslint/no-misused-promises': [
      'error',
      { checksVoidReturn: { arguments: false } },
    ], // this breaks events https://github.com/typescript-eslint/typescript-eslint/issues/4619
    'import/extensions': [
      'error',
      'ignorePackages',
      {
        js: 'never',
        mjs: 'never',
        jsx: 'never',
        ts: 'never',
        tsx: 'never',
      },
    ], // airbnb styleguide only handles js, here we add support also for ts
    'no-new': 0, // not compatible with current approach. We create components as classes and we don't use the instances later (for now).
    'import/prefer-default-export': 0,
    'lines-between-class-members': 0, // no point of adding a blank line between every class member
    'no-plusplus': 0, // we need it for loops. Airbnb bans this because their style guide is against loops.
    'no-restricted-syntax': [
      // copied from Airbnb style guide with a change to enable loops
      'error',
      'LabeledStatement',
      'WithStatement',
    ],
  },
  settings: {
    'import/parsers': {
      '@typescript-eslint/parser': ['.ts', '.tsx'],
    },
    'import/resolver': {
      typescript: {
        alwaysTryTypes: true,
        project: 'tsconfig.json',
      },
    },
  },
};
