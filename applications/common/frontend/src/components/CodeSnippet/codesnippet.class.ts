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

import hljs from 'highlight.js/lib/core';

export class CodeSnippet {
  static readonly backendComponentName = 'Code snippet';

  static readonly copyMessageVisibilityTime = 1000;
  static readonly copyMessageLabel = 'Copied!';
  static readonly cssClassIsInitialised = 'active';
  static readonly cssClassIsHighlighted = 'code-snippet--highlighted';

  static readonly componentSelector = '.code-snippet';
  static readonly codeSelector = '.code-snippet__pre > code';
  static readonly copyButtonSelector = '.code-snippet__icon';
  static readonly copyMessageSelector = '.code-snippet__icon .icon__message';

  readonly collapsedHeightInPixels = 337;
  readonly expandedClassName = 'is-expanded';
  expandHeightInPixels: number;
  isExpandingOn: boolean;
  buttonElement: HTMLElement;
  buttonTextElement: HTMLElement;

  readonly componentElement: HTMLDivElement;
  readonly codeElement: HTMLElement;
  readonly copyButton: HTMLSpanElement | null;
  readonly copyMessage: HTMLSpanElement | null;

  private copyMessageTimeout: ReturnType<typeof setTimeout>;

  constructor(element: HTMLDivElement) {
    this.componentElement = element;
    this.codeElement = this.componentElement.querySelector(
      CodeSnippet.codeSelector
    );
    this.copyButton = this.componentElement.querySelector(
      CodeSnippet.copyButtonSelector
    );
    this.copyMessage = this.componentElement.querySelector(
      CodeSnippet.copyMessageSelector
    );

    this.bindCopyButton();

    this.highlightCode();

    this.markAsInitialised();

    this.prepareExpandCollapseButton();
  }

  private markAsInitialised() {
    this.componentElement.classList.add(CodeSnippet.cssClassIsInitialised);
  }

  private bindCopyButton() {
    this.copyButton?.addEventListener('click', async () => {
      await this.copyElementContent();
    });
  }

  isContentHighlighted(): boolean {
    return this.componentElement.classList.contains(
      CodeSnippet.cssClassIsHighlighted
    );
  }

  // eslint-disable-next-line consistent-return
  highlightCode() {
    // can not re-run it, the lib will break
    if (this.isContentHighlighted()) {
      return false;
    }

    try {
      hljs.highlightElement(this.codeElement);
      this.componentElement.classList.add(CodeSnippet.cssClassIsHighlighted);
    } catch (error) {
      // eslint-disable-next-line no-console
      console.error(error);
    }
  }

  async copyElementContent() {
    const contentToCopy = this.codeElement.innerText;

    await navigator.clipboard.writeText(contentToCopy);

    this.showVisibilityCopyMessage();
  }

  showVisibilityCopyMessage() {
    clearTimeout(this.copyMessageTimeout);

    // Note: setting text on demand here prevents content "flash" upon page load
    this.copyMessage.innerText = CodeSnippet.copyMessageLabel;

    this.copyMessage.classList.add('visible');

    // hide the message after some time and overwrite any previous queued hide actions
    // use case: few clicks on the copy button
    this.copyMessageTimeout = setTimeout(() => {
      this.copyMessage.classList.remove('visible');
    }, CodeSnippet.copyMessageVisibilityTime);
  }

  prepareExpandCollapseButton() {
    this.expandHeightInPixels = this.componentElement.offsetHeight;

    if (this.expandHeightInPixels <= 422) {
      this.componentElement.classList.remove('expanding-on');
    }

    this.isExpandingOn =
      this.componentElement.classList.contains('expanding-on');

    if (this.isExpandingOn) {
      this.buttonElement = this.componentElement.querySelector(
        '.code-snippet-button'
      );
      this.buttonTextElement = this.buttonElement.querySelector(
        '.code-snippet-button-text'
      );

      this.expandCollapseButton();
    }
  }

  expandCollapseButton() {
    this.handleExpandButtonClick();
    this.buttonElement.addEventListener('click', () => {
      this.componentElement.classList.toggle(this.expandedClassName);
      this.handleExpandButtonClick();
    });
  }

  handleExpandButtonClick() {
    if (this.componentElement.classList.contains(this.expandedClassName)) {
      this.codeElement.style.maxHeight = this.expandHeightInPixels + 'px';
      this.buttonTextElement.innerText = 'Show less';
    } else {
      this.codeElement.style.maxHeight = this.collapsedHeightInPixels + 'px';
      this.buttonTextElement.innerText = 'Show more';
    }
  }
}
