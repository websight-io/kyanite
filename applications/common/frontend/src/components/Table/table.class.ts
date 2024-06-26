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

import SimpleBar from 'simplebar';
import { breakpoints } from 'src/helpers/breakpoints';

export class Table {
  static readonly componentSelector =
    '.is-table-scrollable, .is-table-locked-first-column';

  readonly element: HTMLElement;
  simplebarInstance: SimpleBar;

  constructor(element: HTMLElement) {
    this.element = element;
    this.initSimplebar(element.querySelector('.table-wrapper'));

    if (element.classList.contains('is-table-scrollable')) {
      this.handleScrollButtons();
    }
  }

  initSimplebar(tableWrapperEl: HTMLElement) {
    this.simplebarInstance = new SimpleBar(tableWrapperEl, { autoHide: false });
  }

  handleScrollButtons() {
    const leftButton = this.element.querySelector('.table-button-left');
    const rightButton = this.element.querySelector('.table-button-right');
    const isMobile = window.matchMedia(`(max-width: ${breakpoints.md}px)`).matches;

    rightButton.addEventListener('click', () => {
      this.simplebarInstance
        .getScrollElement()
        .scrollBy({ left: isMobile ? 296 : 320, behavior: 'smooth' });
    });
    leftButton.addEventListener('click', () => {
      this.simplebarInstance
        .getScrollElement()
        .scrollBy({ left: isMobile ? -296 : -320 , behavior: 'smooth' });
    });
  }
}
