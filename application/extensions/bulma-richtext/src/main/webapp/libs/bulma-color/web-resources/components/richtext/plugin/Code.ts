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

import TipTapCode from "@tiptap/extension-code";
import { injectGlobal } from 'styled-components';

injectGlobal`
.ProseMirror code {
    color: #da1039;
    font-size: .875em;
    font-weight: 400;
    padding: 0.25em 0.5em;
    background-color: #f5f5f5;
}
`;

const Code = () => ({
    getTipTapExtensions: () => [TipTapCode],
    getAction: ({ editor }) => ({
        execute: () => editor.chain().focus().toggleCode().run(),
    }),
    getState: ({ editor }) => ({
        isActive: editor.isActive('code'),
    }),
});

export default Code;
