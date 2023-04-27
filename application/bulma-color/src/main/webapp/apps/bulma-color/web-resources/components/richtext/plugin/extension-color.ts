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

import '@tiptap/extension-text-style';
import { Extension } from '@tiptap/core';

export type ColorOptions = {
  types: string[],
}

declare module '@tiptap/core' {
  interface Commands<ReturnType> {
    color: {
      toggleColor: (color: string) => ReturnType
    }
  }
}

export const CustomColor = Extension.create<ColorOptions>({
  name: 'color',

  addOptions() {
    return {
      types: ['textStyle'],
    };
  },

  addGlobalAttributes() {
    return [
      {
        types: this.options.types,
        attributes: {
          color: {
            default: null,
            parseHTML: element => element.style.color?.replace(/['"]+/g, ''),
            renderHTML: attributes => {
              if (!attributes.color) {
                return {};
              }

              return {
                style: `color: ${attributes.color}`,
              };
            },
          },
        },
      },
    ];
  },

  addCommands() {
    return {
      toggleColor: color => ({editor, chain}) => {
        if (editor.isActive('textStyle')) {
          return chain()
            .setMark('textStyle', { color: null })
            .removeEmptyTextStyle()
            .run();
        } else {
          return chain()
            .setMark('textStyle', { color: color })
            .run();
        }
      },
    };
  },
});
