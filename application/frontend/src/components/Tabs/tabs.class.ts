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

export class Tabs {
  static readonly componentSelector = '.tabs-container';

  static readonly selectors = {
    tabsPanels: '.tabs li[data-target]',
    tabsContent: '.tabs-content > div',
  };

  static readonly toggleSelectors = {
    activePanel: 'is-active',
    hiddenContent: 'is-hidden',
  };

  public componentElement: HTMLElement;
  public tabsPanels: NodeListOf<HTMLLIElement>;
  public tabsContent: NodeListOf<HTMLDivElement>;

  constructor(element: HTMLElement) {
    this.componentElement = element;
  }

  /**
   *
   * @param element - Objects that represent element
   * @param className  - Toggled css class name.
   * @param force - If set to false, then token will only be removed, but not added.
   *                If set to true, then token will only be added, but not removed.
   */
  static toggleClassName(
    element: HTMLElement,
    className: string,
    force: boolean
  ) {
    element.classList.toggle(className, force);
  }
}
