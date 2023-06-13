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
import isDarkColor from 'is-dark-color';

const WHITE = '#ffffff';
const BLACK = '#000000';

export const createGlobalCSS = (colors) => {
    let styles = '';

    for (const color of colors) {
       styles += `
         .${color.colorClassName} {
            color: ${color.value};
         }
       `;
    }
  
    return css`${styles}`;
};

export const createCSS = (colors) => {
    let styles = '';

    for (const color of colors) {
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
                fill: ${isDarkColor(color.value) ? WHITE : BLACK} !important;
            }
         }
       `;
    }
  
    return css`${styles}`;
};

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
