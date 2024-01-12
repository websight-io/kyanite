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

document.addEventListener(window.KYANITE_ON_DOM_CONTENT_LOAD, () => {

  const dropdowns = document.querySelectorAll('.dropdown:not(.is-hoverable)');

  if (dropdowns.length > 0) {
    dropdowns.forEach(dropdown => {
      dropdown.addEventListener('click', (e) => {
        e.stopPropagation();
        dropdown.classList.toggle('is-active');
      });
    });

    //Close dropdowns if user clicks outside of dropdowns
    document.addEventListener('click', () => {
      closeDropdowns();
    });

    //Close dropdowns if escape pressed
    document.addEventListener('keydown', (e) => {
      const event = e || window.event;
      if (event.keyCode === 27) {
        closeDropdowns();
      }
    });

    function closeDropdowns() {
      dropdowns.forEach(dropdown => {
        dropdown.classList.remove('is-active');
      });
    }
  }

});
