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

import Typed from 'typed.js';

export class AnimatedEndings {
  static readonly SELECTOR = '.typed-animation';
  public readonly STRINGS_SELECTOR = '.typed-strings-animation';
  public readonly element: HTMLElement;
  public readonly titleElement: HTMLElement;
  public readonly showCursor: boolean;
  public readonly speed: number;

  constructor(element, title) {
    this.element = element;
    this.titleElement = title;
    this.showCursor = JSON.parse(element.dataset.showCursor);
    this.speed = Number(element.dataset.speed);
    this.init();
  }

  init() {
    new Typed(this.element, {
      stringsElement: this.titleElement.querySelector(this.STRINGS_SELECTOR),
      loop: true,
      typeSpeed: this.speed,
      showCursor: this.showCursor,
      smartBackspace: false,
    });
    this.setHeightOfTitle();
    window.onresize = () => {
      this.setHeightOfTitle();
    };
  }

  setHeightOfTitle() {
    setTimeout(() => {
      const heights = Array.from(
        this.titleElement.querySelectorAll('.container-with-endings > span')
      ).map((ending) => {
        return ending.clientHeight;
      });
      const titleContainer: HTMLElement =
        this.titleElement.querySelector('.title-container');
      titleContainer.style.height = Math.max(...heights) + 'px';
    }, 0);
  }
}
