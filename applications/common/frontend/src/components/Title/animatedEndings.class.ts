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

import { breakpoints } from 'src/helpers/breakpoints';
import Typed from 'typed.js';

/**
 * Helper function which takes the longest from all endings and check in each iteration if it fits in one line.
 * It uses already rendered endings (in html for SEO purposes) to take the longest element and test height.
 *
 * @param titleEl Title html element to which font-size is applied
 * @param currentSize current size of the title element, desreasing in each iteration by one pixel
 * @param lineHeight Desired height of the animated ending (max height)
 *
 */
const adjustTitleFontSize = (
  titleEl: HTMLElement,
  currentSize: number,
  lineHeight: number
) => {
  titleEl.style.fontSize = currentSize + 'px';
  const heights = Array.from(
    titleEl.querySelectorAll('.container-with-endings > span')
  ).map((ending) => {
    return ending.clientHeight;
  });
  const longestEnding = Math.max(...heights);
  if (longestEnding > lineHeight) {
    adjustTitleFontSize(titleEl, currentSize - 1, lineHeight);
  } else {
    return;
  }
};

export class AnimatedEndings {
  static readonly SELECTOR = '.typed-animation';
  public readonly element: HTMLElement;
  public readonly titleElement: HTMLElement;
  public readonly showCursor: boolean;
  public readonly noTitleWrapping: boolean;
  public readonly speed: number;
  public readonly delay: number;
  public readonly strings: string[];

  constructor(element, title) {
    this.element = element;
    this.titleElement = title;
    this.showCursor = JSON.parse(element.dataset.showCursor);
    this.noTitleWrapping = JSON.parse(element.dataset.noTitleWrapping);
    this.speed = Number(element.dataset.speed);
    this.delay = Number(element.dataset.delay);
    this.strings = JSON.parse(element.dataset.endings);
    this.init();
  }

  init() {
    new Typed(this.element, {
      strings: this.strings,
      loop: true,
      typeSpeed: this.speed,
      showCursor: this.showCursor,
      smartBackspace: false,
      backDelay: this.delay,
      onBegin: () => {
        this.titleElement
          .getElementsByClassName('typed-first')[0]
          ?.classList.add('is-hidden');
      },
    });

    if (this.noTitleWrapping) {
      window.onresize = () => {
        AnimatedEndings.setFontSize(this.titleElement);
      };
    } else {
      AnimatedEndings.setHeightOfTitle(this.titleElement); // will be overwritten in onload event (to avoid jumping, onload needed for Safari bug)
      window.onresize = () => {
        AnimatedEndings.setHeightOfTitle(this.titleElement);
      };
    }
  }

  static setHeightOfTitle(titleEl: HTMLElement) {
    const heights = Array.from(
      titleEl.querySelectorAll('.container-with-endings > span')
    ).map((ending) => {
      return ending.clientHeight;
    });
    const titleContainer: HTMLElement =
      titleEl.querySelector('.title-container');
    titleContainer.style.height = Math.max(...heights) + 'px';
  }

  static setFontSize(titleEl: HTMLElement) {
    if (window.matchMedia(`(max-width: ${breakpoints.md}px)`).matches) {
      const initialSize = Number(
        window.getComputedStyle(titleEl).fontSize.split('px')[0]
      );
      const lineHeight = Number(
        window.getComputedStyle(titleEl).lineHeight.split('px')[0]
      );

      if (!isNaN(initialSize) && !isNaN(lineHeight)) {
        adjustTitleFontSize(titleEl, initialSize, lineHeight);
      }
    }
  }
}
