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

const initForm = () => {
    document.addEventListener('DOMContentLoaded', () => {
        document.addEventListener('submit', (e) => {
            e.preventDefault();
            
            const form = e.target;
            const submitBtn = form.getElementsByClassName('button')[0];
            const formSuccessEl = form.getElementsByClassName('form-success')[0];
            const formFailureEl = form.getElementsByClassName('form-failure')[0];
            const emailValidateErrorEl = form.getElementsByClassName('email-validate-error')[0];
            const emailInputEl = form.getElementsByClassName('email-input')[0];
            let formPostData = {};
            
            const sendForm = (api) => {
                    fetch(api, {
                        method: 'POST',
                        body: JSON.stringify(formPostData)
                    })
                    .then((response) => response.json())
                    .then(() => {
                        successStatus();
                    })
                    .catch(err => {
                        errorStatus(err);
                    });
            };

            const successStatus = () => {
                formSuccessEl.classList.remove('is-hidden');
                submitBtn.removeAttribute('disabled');
                form.reset();
            };

            const errorStatus = (err) => {
                console.error(err);
                formFailureEl.classList.remove('is-hidden');
                submitBtn.removeAttribute('disabled');
            };
            
            const getEntityName = () => {
                const contactFormEntityName = JSON.parse(document.body.dataset.config);
                if (contactFormEntityName.constructor === Object && Object.keys(contactFormEntityName).length !== 0) {
                    submitBtn.setAttribute('disabled', 'disabled');
                    sendForm(contactFormEntityName.contactForm);
                } else {
                    console.error('Invalid configuration');
                }
            };
            
            const emailIsValid = (emailValue) => {
                return /\b[\w\.-]+@[\w\.-]+\.\w{2,4}\b/gi.test(emailValue); 
            };
            
            const showEmailError = () => {
                emailValidateErrorEl.classList.remove('is-hidden');
                !emailInputEl.classList.contains('is-danger') && emailInputEl.classList.add('is-danger');
            };
            
            const hideEmailError = () => {
                !emailValidateErrorEl.classList.contains('is-hidden') 
                && emailValidateErrorEl.classList.add('is-hidden');
                emailInputEl.classList.remove('is-danger');
            };
            
            const hideNotifications = () => {
                !formSuccessEl.classList.contains('is-hidden') && formSuccessEl.classList.add('is-hidden');
                !formFailureEl.classList.contains('is-hidden') && formFailureEl.classList.add('is-hidden');
            };

            const prepareFormData = () => {
                const formData = new FormData(form);
                let validateEmail = false;
                formPostData = {};
                for (let data of formData.entries()) {
                    formPostData[data[0]] = data[1];
                    if (data[0] === 'email') {
                        validateEmail = data[1];
                    }
                }
                if (validateEmail) {
                    emailIsValid(validateEmail) ? getEntityName() : showEmailError();
                } else {
                    getEntityName();
                }
            };

            hideEmailError();
            hideNotifications();
            prepareFormData();
        });
    });
};

initForm();
