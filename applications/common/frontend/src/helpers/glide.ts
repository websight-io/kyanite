/*
 * Copyright (C) 2022 Dynamic Solutions
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

import Glide from '@glidejs/glide';
import { breakpoints } from '../helpers/breakpoints';

export const getBreakpoint = () => {
  let breakpoint = breakpoints.lg;

  if (window.innerWidth < breakpoints.md) {
    breakpoint = breakpoints.sm;
  } else if (window.innerWidth < breakpoints.lg) {
    breakpoint = breakpoints.md;
  }

  return breakpoint;
};

export const initGlideJsSlider = (selector, optionsFn) => {
  const listener = () => {
    const elements = document.querySelectorAll(selector);
    if (elements.length === 0) {
      return;
    }

    for (let i = 0; i < elements.length; i++) {
      const element = elements[i];
      const glide = new Glide(element, optionsFn(element));

      /*
       * Glide JS provides a throttled 'resize' event, so it's safe to use it here
       * without throttling/debouncing on our side.
       */
      glide.on('resize', () => {
        /*
         * Peek value depends on the current width of the window and the slider element,
         * so it needs to be updated upon resize.
         */
        const updateOptions: Record<string, string> = {
          peek: optionsFn(element).peek
        };

        // Workaround for following issue: https://github.com/glidejs/glide/issues/541
        if (getBreakpoint() === breakpoints.lg) {
          updateOptions.perView = optionsFn(element).perView;
        }

        glide.update(updateOptions);
      });

      glide.mount();
    }
  };

  window.addEventListener(window.KYANITE_ON_LOAD, listener, { once: true });
};
