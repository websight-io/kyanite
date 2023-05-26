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

const importMap = require('./import-map.json');

prepareAliasesMap = () => {
  const { rootDir, aliases } = importMap;

  Object.keys(aliases).forEach((key) =>
      aliases[key] = `${rootDir}${aliases[key]}`
  );

  return aliases;
}

prepareAliasesMapForDevelopment = () => {
  const { aliases, devAliases } = importMap;

  Object.keys(devAliases).forEach((devKey) =>
    Object.keys(aliases)
      .filter((key) => aliases[key].includes(devKey))
      .forEach((key) => aliases[key] = aliases[key].replace(devKey, devAliases[devKey]))
  );

  return aliases;
}

module.exports = (api) => {
  api.cache(true);

  let aliases = {};

  if (process.env.BABEL_ENV === 'development') {
    aliases = prepareAliasesMapForDevelopment(aliases);
  } else {
    aliases = prepareAliasesMap(aliases);
  }

  return {
    presets: [
      "@babel/preset-react", "@babel/preset-typescript"
    ],
    plugins: [
      [ "module-resolver", {
        loglevel: 'silent',
        alias: aliases,
      }],
      ["transform-react-remove-prop-types", {
        removeImport: true,
      }],
    ]
  };
}
