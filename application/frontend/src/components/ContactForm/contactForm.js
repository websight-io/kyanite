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

const initForm = () => {
    document.addEventListener('DOMContentLoaded', () => {
        document.addEventListener('submit', (e)=> {
            e.preventDefault();
            
            const form = e.target;
            const submitBtn = form.getElementsByClassName('button')[0];
            const formSuccessEl = form.getElementsByClassName('form-success')[0];
            const formFailureEl = form.getElementsByClassName('form-failure')[0];
            let formPostData = {};

            const getContactFormEntityName = () => {
                const domain = window.location.origin;
            
                fetch(domain + '/apps/bulma-backend/bin/read-contact-form-config.action')
                .then((response) => response.json())
                .then((data) => {
                    APIService.getInstance().setContactFormEntity(data.entity.endpoint);
                    sendForm(data.entity.endpoint);
                })
                .catch(err => {
                    console.error(err);
                    formFailureEl.classList.remove('is-hidden');
                    submitBtn.removeAttribute('disabled');
                });
            };
            
            const sendForm = (api) => {
                const domain = window.location.origin;
            
                    fetch(domain + '/' + api, {
                        method: 'POST',
                        body: JSON.stringify(formPostData)
                    })
                    .then((response) => response.json())
                    .then(() => {
                        formSuccessEl.classList.remove('is-hidden');
                        submitBtn.removeAttribute('disabled');
                        form.reset();
                    })
                    .catch(err => {
                        console.error(err);
                        formFailureEl.classList.remove('is-hidden');
                        submitBtn.removeAttribute('disabled');
                    });
            };
            
            const sendOrGetEntityName = () => {
                const contactFormEntityName = APIService.getInstance().getContactFormEntity();
                submitBtn.setAttribute('disabled', 'disabled');
                contactFormEntityName ? sendForm(contactFormEntityName) : getContactFormEntityName();
            };
            
            const emailIsValid = (emailValue) => {
                return /\b[\w\.-]+@[\w\.-]+\.\w{2,4}\b/gi.test(emailValue); 
            };
            
            const showEmailError = () => {
                const emailErrorMessageEl = document.getElementById('emailError');
                const emailInputEl = form.getElementsByClassName('email-input')[0];
                emailErrorMessageEl.classList.remove('is-hidden');
                emailInputEl.classList.contains('is-danger') ? false : emailInputEl.classList.add('is-danger');
            };
            
            const hideEmailError = () => {
                const emailErrorMessageEl = document.getElementById('emailError');
                const emailInputEl = form.getElementsByClassName('email-input')[0];
                emailErrorMessageEl.classList.contains('is-hidden') 
                ? false 
                : emailErrorMessageEl.classList.add('is-hidden');
                emailInputEl.classList.remove('is-danger');
            };
            
            const hideNotifications = () => {
                formSuccessEl.classList.contains('is-hidden') ? false : formSuccessEl.classList.add('is-hidden');
                formFailureEl.classList.contains('is-hidden') ? false : formFailureEl.classList.add('is-hidden');
            };

            const prepareFormData = () => {
                const formData = new FormData(form);
                formPostData = {};
                for (let data of formData.entries()) {
                    formPostData[data[0]] = data[1];
                    if (data[0] === 'email') {
                        emailIsValid(data[1]) ? sendOrGetEntityName(): showEmailError();
                    }
                }
            };

            hideEmailError();
            hideNotifications();
            prepareFormData();
        });
    });
};

initForm();
