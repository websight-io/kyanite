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

/*
    Mobile layout for Tabs. Applies only to vertical mode Tabs for narrow breakpoints.
    Switching between normal and mobile layouts is handled by CSS bound to breakpoints
 */

export class TabsMobile {
  static readonly componentSelector = '.tabs-mobile-container';

  static readonly selectors = {
    tab:        '.tab-mobile',
    tabButton:  '.tab-mobile-title',
  };

  static readonly toggleClasses = {
    openedTab: 'opened',
  };

  private componentElement: HTMLElement;

  constructor(element: HTMLElement) {
    this.componentElement = element;
    this.componentElement
      .querySelectorAll<HTMLElement>(TabsMobile.selectors.tab)
      .forEach((tab) => { TabsMobile.handleAccordionOpening(tab) });
  }

  static handleAccordionOpening = (accordionItem) => {
    const accordionButton = accordionItem.querySelector(TabsMobile.selectors.tabButton);

    accordionButton.addEventListener('click', () => {
      accordionItem.classList.toggle(TabsMobile.toggleClasses.openedTab);
      accordionButton.ariaExpanded = accordionItem.classList.contains(TabsMobile.toggleClasses.openedTab)
          ? 'true'
          : 'false';
    });
  };
}
