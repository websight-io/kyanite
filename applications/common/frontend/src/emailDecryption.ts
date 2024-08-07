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

window.addEventListener(window.KYANITE_ON_LOAD, () => {
  const links = document.querySelectorAll<HTMLElement>(
    '[data-part1][data-part2][data-part3]'
  );
  for (const link of Array.from(links)) {
    const attrs = link.dataset;
    const result = `${attrs.part1}@${attrs.part2}.${attrs.part3}`;
    const label = link.innerHTML || result;
    link.setAttribute('href', `mailto:${result}`);
    link.innerHTML = label;
  }
});
