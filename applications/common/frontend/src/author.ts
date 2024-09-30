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

// Stylesheets that will be loaded in Edit mode only
import "./author.scss";
import {onDOMContentLoaded} from "./helpers/dom";


const detectDuplicateIds = () => {
  const idCnt = {};
  document.querySelectorAll("[id]").forEach((element) => {
    const id = element.id;
    idCnt[id] ? idCnt[id]++ : idCnt[id] = 1;
  });
  const duplCnt = Object.keys(idCnt)
    .filter(k => idCnt[k] > 1)
    .reduce((m, k) => {
      m[k] = idCnt[k];
      return m;
    }, {});
  if (duplCnt) {
    const duplicatesString = Array.from(
        Object.keys(duplCnt)
        .map(k => `${k}: ${duplCnt[k]} times`)
    ).join('\n');
    alert("Page contains elements with duplicate ids:\n" + duplicatesString);
  }
}

onDOMContentLoaded(() => detectDuplicateIds());
