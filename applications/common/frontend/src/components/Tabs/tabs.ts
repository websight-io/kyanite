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

import { onDOMContentLoaded } from '../../helpers/dom';
import { Tabs } from './tabs.class';
import { TabsMobile } from "./tabsMobile.class";
import { AutoSwitchingTabs } from './autoSwitchingTabs.class';
import './switchTab';
import './tabsMobile.class'

onDOMContentLoaded(() => {
  document
    .querySelectorAll<HTMLDivElement>(Tabs.componentSelector)
    .forEach((element) => {
      new AutoSwitchingTabs(element);
    });
  document
    .querySelectorAll<HTMLElement>(TabsMobile.componentSelector)
    .forEach((element) => {
      new TabsMobile(element);
    });
});
