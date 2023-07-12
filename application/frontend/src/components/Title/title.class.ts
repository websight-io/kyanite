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

import {
  getCoveringElement,
  getAnchorTargetElement,
  getWindowTopScrollPositionWithVisibleTarget,
} from '../../helpers.dom';

/**
 * Functions that are restricted to invoking just once.
 * Repeat calls to the function return the value of the first invocation.
 */
// eslint-disable-next-line @typescript-eslint/no-explicit-any
const onceAll: Record<string, any> = {
  bindReClickOnSameAnchorHash: false,
};

export class TitleComponent {
  static readonly SELECTOR = '.title';
  static readonly DO_OFFSET_IF_COVERED_BY_TAGS = ['NAV', 'HEADER'];

  public readonly componentElement: HTMLElement;

  constructor(element) {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment
    this.componentElement = element;

    // eslint-disable-next-line @typescript-eslint/no-floating-promises
    this.scrollToIfCovered();

    TitleComponent.bindReClickOnSameAnchorHash();
    this.bindLocationHashChange();
  }

  static get hashAnchor(): Element | null {
    return window.location.hash
      ? document.querySelector(window.location.hash)
      : null;
  }

  /**
   * Allowed to run once across all instances.
   * On re-clicking the same hash: emmit the 'hashchange' event manually.
   */
  // eslint-disable-next-line consistent-return
  private static bindReClickOnSameAnchorHash() {
    if (onceAll.bindReClickOnSameAnchorHash) {
      // eslint-disable-next-line @typescript-eslint/no-unsafe-return
      return onceAll.bindReClickOnSameAnchorHash;
    }

    // bind event on all `<A>` HTML elements - currently existing and created in the future
    document.body.addEventListener('click', (event) => {
      const target = event.target as HTMLAnchorElement;
      // note: 'target' type actually can be any HTMLElement or null, but we look for HTMLAnchorElement
      const link =
        target?.nodeName === 'A'
          ? target
          : target?.closest<HTMLAnchorElement>('A');
      const isAnchorWithCurrentHash =
        link && link?.hash === window.location.hash;

      if (!isAnchorWithCurrentHash) {
        return;
      }

      window.dispatchEvent(new HashChangeEvent('hashchange'));
    });

    onceAll.bindReClickOnSameAnchorHash = true;
  }

  /**
   * On fragment identifier of the URL change.
   *
   * @see https://developer.mozilla.org/en-US/docs/Web/API/Window/hashchange_event
   */
  private bindLocationHashChange() {
    window.addEventListener('hashchange', () => {
      this.onLocationHashChange();
    });
  }

  onLocationHashChange() {
    // setTimeout used, so it resolves at next frame, when :target was already scrolled-to by browser
    setTimeout(async () => {
      await this.scrollToIfCovered(TitleComponent.hashAnchor);
    });
  }

  /**
   * Decorate scrolling to URL anchor target by offsetting position of the header menu items.
   *
   * @param givenAnchorTarget If not provided uses ':target' CSS pseudo-class.
   * @param givenAnchorTarget Can be null from use of 'document.querySelector()'
   *
   * @example localhost/home.html#test - if id="test" element is covered then scroll it to view
   *
   * @see TitleComponent.DO_OFFSET_IF_COVERED_BY_TAGS for list of elements considered a "header menu"
   *
   * @todo add automated tests to future-proof the solution (DS WOS-166)
   */
  async scrollToIfCovered(givenAnchorTarget?: Element | null) {
    const anchorTarget = givenAnchorTarget || (await getAnchorTargetElement());

    // empty (no target)
    if (!anchorTarget) {
      return;
    }

    // target points to another Title Component (not this one)
    if (anchorTarget !== this.componentElement) {
      return;
    }

    const coveredBy = getCoveringElement(
      anchorTarget,
      TitleComponent.DO_OFFSET_IF_COVERED_BY_TAGS
    );

    // not covered, no need to do anything
    if (!coveredBy) {
      return;
    }

    const newPosition = getWindowTopScrollPositionWithVisibleTarget(
      coveredBy,
      anchorTarget
    );

    window.scrollTo(newPosition);
  }
}
