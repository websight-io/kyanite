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

import { Tabs } from './tabs.class';

export class AutoSwitchingTabs extends Tabs {
  static readonly dataAttributes = {
    autoSwitch: 'autoSwitch',
    switchingTime: 'switchingTime',
  };

  public isAutoSwitching: boolean;
  public numberOfTabs: number;
  /**
   * SwitchingTime is value expressed in seconds.
   * Default value is provided by BE and is equal 5.
   */
  public switchingTime: number;

  constructor(element: HTMLElement) {
    super(element);

    this.isAutoSwitching =
      this.componentElement.dataset[
        AutoSwitchingTabs.dataAttributes.autoSwitch
      ].toLowerCase() === 'true';

    if (this.isAutoSwitching) {
      this.switchingTime = Number(
        this.componentElement.dataset[
          AutoSwitchingTabs.dataAttributes.switchingTime
        ]
      );
      this.tabsPanels = this.componentElement.querySelectorAll(
        Tabs.selectors.tabsPanels
      );
      this.tabsContent = this.componentElement.querySelectorAll(
        Tabs.selectors.tabsContent
      );
      this.numberOfTabs = this.tabsPanels.length;

      this.initAutoSwitching();
    }
  }

  changeActiveTab(index: number) {
    for (let i = 0; i < this.numberOfTabs; i++) {
      Tabs.toggleClassName(
        this.tabsPanels[i],
        Tabs.toggleSelectors.activePanel,
        false
      );
      Tabs.toggleClassName(
        this.tabsContent[i],
        Tabs.toggleSelectors.hiddenContent,
        true
      );
    }

    Tabs.toggleClassName(
      this.tabsPanels[index],
      Tabs.toggleSelectors.activePanel,
      true
    );
    Tabs.toggleClassName(
      this.tabsContent[index],
      Tabs.toggleSelectors.hiddenContent,
      false
    );
  }

  *logger() {
    while (true) {
      for (let i = 0; i < this.numberOfTabs; i++) {
        this.changeActiveTab(i);
        yield;
      }
    }
  }

  /**
   * Disable automatic switching when clicking on one of the tabs.
   * @param interval - intervalID identifies the timer created by the call to setInterval();
   */
  stopAutoSwitching(interval: NodeJS.Timer) {
    for (let i = 0; i < this.numberOfTabs; i++) {
      this.tabsPanels[i].addEventListener('click', () => {
        clearInterval(interval);
        this.changeActiveTab(i);
      });
    }
  }

  initAutoSwitching() {
    const generator = this.logger();
    const interval = setInterval(
      () => generator.next(),
      this.switchingTime * 1000
    );

    this.stopAutoSwitching(interval);
  }
}
