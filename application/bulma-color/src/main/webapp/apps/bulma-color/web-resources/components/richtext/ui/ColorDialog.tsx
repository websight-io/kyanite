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
import React, { useState } from 'react';
import { Checkbox } from '@atlaskit/checkbox';

import Popup from 'components/richtext/ui/Popup';
import Button from 'components/richtext/ui/Button';

import { DialogContainer, PopupContainer } from './ColorDialogStyles.js';
import {COLORS, createColorsList} from './colors.js';

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
    
    createColorsList(colors);

    const open = () => {
        dialogRef.current.toggle();
    };

    const onSubmit = (params) => {
        execute(params);
        dialogRef.current.close();
    };

     return (
        <PopupContainer>
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
