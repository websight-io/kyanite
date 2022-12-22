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
import APIService from '../../ts/services/APIService';

const getContactFormEntityName = () => {
    const domain = window.location.origin;

    fetch(domain + '/apps/bulma-backend/bin/read-contact-form-config.action')
    .then((response) => response.json())
    .then((data) => APIService.getInstance().setContactFormEntity(data.entity.endpoint));
};

const sendForm = (api) => {
    const domain = window.location.origin;
    // post
    // fetch(domain + api)
    // .then((response) => response.json())
    // .then((data) => console.log(data));
};

const initSubmitListener = () => {
    document.addEventListener('DOMContentLoaded', () => {
        const submitBtn = document.getElementById('submit-form');
        submitBtn.addEventListener( 'click', ()=> {
            submitBtn.setAttribute('disabled', 'disabled');
            const contactFormEntityName = APIService.getInstance().getContactFormEntity();
            
            if (contactFormEntityName) {
                sendForm(contactFormEntityName);
            } else {
                getContactFormEntityName();
                sendForm(APIService.getInstance().getContactFormEntity());
            }
            

            submitBtn.removeAttribute('disabled');
        });
    });
};

initSubmitListener();
