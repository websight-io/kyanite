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

import PropTypes from 'prop-types';
import React, { useState, useEffect } from 'react';
import { Checkbox } from '@atlaskit/checkbox';
import styled from 'styled-components';
import { injectGlobal } from 'styled-components';

import Popup from 'components/richtext/ui/Popup';
import Button from 'components/richtext/ui/Button';

import { PopupContainer, createGlobalCSS, createCSS } from './ColorDialogStyles.js';
import {COLORS, createColorsList} from './colors.js';

const RTE_DEFAULT_BUTTON_COLOR = '#344563';

const ColorDialogContent = ({
    submit,
    colorClassName: initialColorClassName
}) => {
    const [value, setValue] = useState(initialColorClassName);
    const onColorChange = (event) => {
        const cssClassName = event.target.value;
        if (event.target.checked) {
            setValue(cssClassName);
            submit({ cssClassName });
        } else {
            setValue('');
            submit({});
        }
    };

    const DialogContainer = styled.div`
        .Richtext_Color-Cards {
            display: flex;
            flex-wrap: wrap;
            margin: 4px;
            max-width: 214px;
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
                        box-shadow: inset 0px 0px 0px 1px var(--ds-background-inverse-subtle,rgba(13,20,36,0.18));
                        path {
                            d: path("M 7.356 10.942 a 0.497 0.497 0 0 0 -0.713 0 l -0.7 0.701 a 0.501 0.501 0 0 0 -0.003 0.71 l 3.706 3.707 a 0.501 0.501 0 0 0 0.705 0.003 l 7.712 -7.712 a 0.493 0.493 0 0 0 -0.006 -0.708 l -0.7 -0.7 a 0.504 0.504 0 0 0 -0.714 0 l -6.286 6.286 a 0.506 0.506 0 0 1 -0.713 0 l -2.288 -2.287 Z");
                        }
                    }
                }
            }
            ${createCSS(COLORS)}
        }
    `;


    return (
        <DialogContainer>
            <div className='Richtext_Color-Cards'>
                {COLORS.map((color) => {
                    return (
                        <Checkbox
                            className={color.colorClassName}
                            value={color.colorClassName}
                            name="checkbox-basic"
                            size="large"
                            type="checkbox"
                            onChange={onColorChange}
                            isChecked={value === color.colorClassName}
                        />
                    );
                })}
            </div>
        </DialogContainer>
    );
};

ColorDialogContent.propTypes = {
    submit: PropTypes.func.isRequired,
    colorClassName: PropTypes.string.isRequired,
};

const ColorDialog = ({ configuration, state, action }) => {
    const { title, icon, colors } = configuration;
    const { isActive, color } = state;
    const { execute } = action;
    const dialogRef = React.createRef();

    useEffect(() => {
        createColorsList(colors);
        injectGlobal`
            .ProseMirror {
              ${createGlobalCSS(COLORS)}
            }
        `;
    }, []);

    const getColorValue = (colorClassName: string) => {
        const color = COLORS.find(color => color.colorClassName === colorClassName);
        return color ? color.value : RTE_DEFAULT_BUTTON_COLOR;
    };

    const applyActiveColor = (color): string => {
        return isActive ? getColorValue(color) : RTE_DEFAULT_BUTTON_COLOR;
    };

    const open = () => {
        dialogRef.current.toggle();
    };

    const onSubmit = (params) => {
        execute(params);
        dialogRef.current.close();
    };

    const ColorIndicator = styled.span`
        position: absolute;
        z-index: 1;
        background-color: ${applyActiveColor(color)};
        width: 22px;
        height: 4px;
        bottom: 8px;
        left: 11px;
    `;

     return (
        <PopupContainer>
            <ColorIndicator />
            <Popup
                ref={dialogRef}
                content={
                    <ColorDialogContent
                        submit={onSubmit}
                        colorClassName={color}
                    />
                }
            >
                <Button
                    configuration={{ title, icon }}
                    state={{ isActive }}
                    action={{ execute: open }}
                    iconAfter="expand_more"
                />
            </Popup>
        </PopupContainer>
     );
};

ColorDialog.propTypes = {
    configuration: PropTypes.shape({
        title: PropTypes.string.isRequired,
        icon: PropTypes.string,
    }).isRequired,
    state: PropTypes.shape({
        isActive: PropTypes.bool.isRequired,
        colorClassName: PropTypes.string.isRequired,
    }).isRequired,
    action: PropTypes.shape({
        execute: PropTypes.func.isRequired,
    }).isRequired,
};

export default ColorDialog;
