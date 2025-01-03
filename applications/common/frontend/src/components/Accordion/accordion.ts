/*
 * Copyright (C) 2025 Dynamic Solutions
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

const accordionItemClass = 'accordion-item';
const accordionButtonClass = 'accordion-item__title';
const openedClass= 'opened';

const handleAccordionOpening = (accordionItem) => {
  const accordionButton = accordionItem.querySelector(`.${accordionButtonClass}`);
  const icon = accordionItem.querySelector(`i`);
  accordionButton.addEventListener('click', (() => {
      accordionItem.classList.toggle(openedClass);
      let hasOpenedClass = accordionItem.classList.contains(openedClass);
      if (hasOpenedClass) {
          accordionButton.ariaExpanded = 'true';
          icon.classList.remove("mdi-chevron-up")
          icon.classList.add("mdi-chevron-down")
      } else {
          accordionButton.ariaExpanded = 'false';
          icon.classList.remove("mdi-chevron-down")
          icon.classList.add("mdi-chevron-up")
      }
  }) as EventListener);
};

const initAccordion = () => {
  document.addEventListener(
    'DOMContentLoaded',
    () => {
      const elements = document.querySelectorAll(`.${accordionItemClass}`);

      if (!elements?.length) {
        return;
      }

      elements.forEach((accordionItem) => handleAccordionOpening(accordionItem));
    },
    { once: true }
  );
};

initAccordion();
