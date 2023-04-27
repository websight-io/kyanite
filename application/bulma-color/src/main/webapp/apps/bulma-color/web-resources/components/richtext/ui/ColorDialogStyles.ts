/* eslint-disable max-len */
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

import styled, {css} from 'styled-components';
import { injectGlobal } from 'styled-components';
import {COLORS} from './colors.js';

const createCSS = () => {
    let styles = '';

    for (const color of COLORS) {
       styles += `
         .${color.colorClassName} {
            & + svg rect:first-of-type {
                stroke: ${color.value};
            }
            & + svg g {
                color: ${color.value};
            }
            & + svg {
                background-color: ${color.value};
            }
         }
       `;
    }
  
    return css`${styles}`;
};

const createGlobalCSS = () => {
    let styles = '';

    for (const color of COLORS) {
       styles += `
         .${color.colorClassName} {
            color: ${color.value};
         }
       `;
    }
  
    return css`${styles}`;
};

injectGlobal`
    .ProseMirror {
      ${createGlobalCSS()}
    }
`;

export const PopupContainer = styled.div`
    & > div > div {
        padding: 0;
        display: flex;
        flex-wrap: wrap;
        & > button {
            &:after {
                font-family: Material Icons Outlined;
                content: '\\e313';
            }
        }
    }
`;

export const DialogContainer = styled.div`
    .Richtext_Color-Cards {
        display: flex;
        flex-wrap: wrap;
        margin: 4px;
        label {
            cursor: pointer;
            margin: 4px;
            border: 2px solid transparent;
            border-radius: 6px;
            transition: border-color 0.15s cubic-bezier(0.47, 0.03, 0.49, 1.38) 0s;
            &:hover {
                border-color: #B3D4FF;
            }
            > input {
                & + svg {
                    border-radius: 4px;
                    width: 22px;
                    height: 22px;
                    margin: 1px;
                    path {
                        d: path("M 7.356 10.942 a 0.497 0.497 0 0 0 -0.713 0 l -0.7 0.701 a 0.501 0.501 0 0 0 -0.003 0.71 l 3.706 3.707 a 0.501 0.501 0 0 0 0.705 0.003 l 7.712 -7.712 a 0.493 0.493 0 0 0 -0.006 -0.708 l -0.7 -0.7 a 0.504 0.504 0 0 0 -0.714 0 l -6.286 6.286 a 0.506 0.506 0 0 1 -0.713 0 l -2.288 -2.287 Z");
                    }
                }
            }
        }
        ${createCSS()}
    }
`;
