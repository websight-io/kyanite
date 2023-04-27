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

import { CustomColor } from "./extension-color.js";
import { TextCssClass } from "./extension-text-class.js";

const Color = () => ({
    getTipTapExtensions: () => [TextCssClass, CustomColor],
    getAction: ({ editor }) => ({
        execute: ({cssClassName}) => {
            if (cssClassName !== '') {
                editor.chain().focus().setColor(cssClassName).run();
            } else {
                editor.chain().focus().unsetColor().run();
            }
        }
    }),
    getState: ({ editor }) => ({
        isActive: editor.isActive('textCssClass'),
        ...editor.getAttributes('textCssClass'),
    }),
});

export default Color;
