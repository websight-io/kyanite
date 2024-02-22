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

import Typed from "typed.js";

  
  export class AnimatedEndings {
    static readonly SELECTOR = '#typed';
    public readonly element: HTMLElement;
    public readonly showCursor: boolean;
    public readonly speed: number;
  
    constructor(element) {
      this.element = element;
      this.showCursor = JSON.parse(element.dataset.showCursor);
      this.speed = Number(element.dataset.speed);
      this.init()
    }

    init() {
      const typed = new Typed(this.element, { stringsElement: '#typed-strings', loop: true, typeSpeed: this.speed, showCursor: this.showCursor, smartBackspace: false })
    }
  }
  