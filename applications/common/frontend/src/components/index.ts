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

import "@algolia/autocomplete-theme-classic";

import './CodeSnippet/codesnippet';
import './ContactForm/contactForm';
import './Dropdown/dropdown';
import './Image/image';
import './Modal/modal';
import './Message/message';
import './Navbar/navbar';
import './Notification/notification';
import './PanelTabs/panelTabs';
import './Tabs/tabs';
import './Title/title';
import './Carousel/carousel';
import './Search/search';
import './BlogTableOfContent/blogtableofcontent';

document.querySelectorAll('.navbar-item.has-dropdown').forEach((navbarItem) => {
  navbarItem.addEventListener('keydown', (event: KeyboardEvent) => {
    if (event.key === 'Enter') {
      navbarItem.classList.toggle('is-active');
    }
  });
});
