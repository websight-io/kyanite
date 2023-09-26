/*!
 * <!--
 *     Copyright (C) 2023 Dynamic Solutions
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 * -->
 */

const VISIBLE_ITEMS_COUNT = 4;

const initCarousel = (element: HTMLDivElement) => {
  const items = element.querySelectorAll<HTMLDivElement>('.carousel-item');
  const previousPageButton = element.querySelector<HTMLButtonElement>('.previous-page-button');
  const nextPageButton = element.querySelector<HTMLButtonElement>('.next-page-button');

  const getFirstVisibleItemIndex = () => {
    return Array.from(items).findIndex((item) =>
      item.classList.contains('visible')
    );
  }

  const updateButtonsState = (firstVisibleItemIndex: number) => {
    previousPageButton.disabled = firstVisibleItemIndex === 0;
    nextPageButton.disabled = firstVisibleItemIndex + VISIBLE_ITEMS_COUNT >= items.length;
  }

  previousPageButton.addEventListener('click', () => {
    const firstVisibleItemIndex = getFirstVisibleItemIndex();

    for (let i = 0; i < items.length; i++) {
      const item = items[i];
      if (
        i >= firstVisibleItemIndex - VISIBLE_ITEMS_COUNT &&
        i <= firstVisibleItemIndex - 1
      ) {
        item.classList.add('visible');
      } else {
        item.classList.remove('visible');
      }
    }

    updateButtonsState(firstVisibleItemIndex - VISIBLE_ITEMS_COUNT);
  });

  nextPageButton.addEventListener('click', () => {
    const firstVisibleItemIndex = getFirstVisibleItemIndex();

    for (let i = 0; i < items.length; i++) {
      const item = items[i];
      if (
        i >= firstVisibleItemIndex + VISIBLE_ITEMS_COUNT &&
        i <= firstVisibleItemIndex + 2 * VISIBLE_ITEMS_COUNT - 1
      ) {
        item.classList.add('visible');
      } else {
        item.classList.remove('visible');
      }
    }

    updateButtonsState(firstVisibleItemIndex + VISIBLE_ITEMS_COUNT);
  });
};

document.querySelectorAll<HTMLDivElement>('.carousel').forEach((element) => {
  initCarousel(element);
});
