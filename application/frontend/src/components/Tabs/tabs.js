/*
 * Copyright (C) 2022 Dynamic Solutions
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

const switchTab = () => {
    const check = document.querySelector('#check');
    let isPreviewMode = false;
    if (check != null && check.dataset.editCheck == 'false') {
        isPreviewMode = true;
    }

    if (isPreviewMode) {
        document.addEventListener('DOMContentLoaded', () => {
            const tabContainers = document.querySelectorAll('.tabs');
            let tabContainersTabs = [];
            tabContainers.forEach(container => {
                tabContainersTabs.push(container.querySelectorAll('li'));
            });

            tabContainersTabs.forEach(tabs => {
                if (tabs != null && tabs.length > 0 && tabs[0] != null) {
                    tabs[0].classList.add('is-active');
                }
            });

            const tabContentBoxes = document.querySelectorAll('#tab-content > div');
            tabContentBoxes.forEach(box => {
                tabContainersTabs.forEach(tabs => {
                    if (box.getAttribute('id') === tabs[0].dataset.target) {
                        box.classList.remove('is-hidden');
                    }
                });
            });

            tabContainersTabs.forEach(tabs => {
                tabs.forEach(tab => {

                    tab.addEventListener('click', () => {
                        tabs.forEach(item => item.classList.remove('is-active'));
                        tab.classList.add('is-active');

                        const target = tab.dataset.target;
                        tabContentBoxes.forEach(box => {
                            if (box.getAttribute('id') === target) {
                                box.classList.remove('is-hidden');
                            } else {
                                tabs.forEach(item => {
                                    if (box.getAttribute('id') === item.dataset.target) {
                                        box.classList.add('is-hidden');
                                    }
                                });
                            }
                        });
                    });
                });
            });
        });
    }
};

switchTab();
