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
    getMarkAttributes,
    Mark,
    mergeAttributes,
  } from '@tiptap/core';
  
  export interface TextCssClassOptions {
    HTMLAttributes: Record<string, any>,
  }
  
  declare module '@tiptap/core' {
    interface Commands<ReturnType> {
      textCssClass: {
        removeEmptyTextCssClass: () => ReturnType,
      }
    }
  }
  
  export const TextCssClass = Mark.create<TextCssClassOptions>({
    name: 'textCssClass',
  
    addOptions() {
      return {
        HTMLAttributes: {},
      };
    },
  
    parseHTML() {
      return [
        {
          tag: 'span',
          getAttrs: element => {
            const hasCssClass = (element as HTMLElement).hasAttribute('class');
  
            if (!hasCssClass) {
              return false;
            }
  
            return {};
          },
        },
      ];
    },
  
    renderHTML({ HTMLAttributes }) {
      return ['span', mergeAttributes(this.options.HTMLAttributes, HTMLAttributes), 0];
    },
  
    addCommands() {
      return {
        removeEmptyTextCssClass: () => ({ state, commands }) => {
          const attributes = getMarkAttributes(state, this.type);
          const hasCssClass = Object.entries(attributes).some(([, value]) => !!value);

          if (hasCssClass) {
            return true;
          }
  
          return commands.unsetMark(this.name);
        },
      };
    },
  
  });
