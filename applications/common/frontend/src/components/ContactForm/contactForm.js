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

const initForm = () => {
    document.addEventListener(window.KYANITE_ON_DOM_CONTENT_LOAD, () => {
        Array.from(document.getElementsByClassName("contact-form"))
        .forEach(function(contactForm) {
            contactForm.addEventListener('submit', (e) => {
                e.preventDefault();

                const form = e.target;
                const submitBtn = form.getElementsByClassName('button')[0];
                const formSuccessEl = form.getElementsByClassName('form-success')[0];
                const formFailureEl = form.getElementsByClassName('form-failure')[0];
                const emailValidateErrorEl = form.getElementsByClassName('email-validate-error')[0];
                const emailInputEl = form.getElementsByClassName('email-input')[0];
                const contactForm = form.dataset.configHttpEndPoint;
                const captchaPublicKey = form.dataset.configCaptchaPublicKey;
                
                const errorStatus = (err) => {
                    console.error(err);
                    formFailureEl.classList.remove('is-hidden');
                    submitBtn.removeAttribute('disabled');
                };

                if (captchaPublicKey === undefined) {
                    errorStatus('Invalid configuration: recaptcha key not found');
                    return;
                }

                const sendForm = (submitForm, formData) => {
                    fetch(submitForm, {
                        method: 'POST',
                        body: formData
                        //  if you set 'content-type' header to 'multipart/form', it will be missing
                        //  'boundary' parameter in it, and backend will decline the exchange.
                        //  Instead, omit this header, and browser will do the work for you
                    })
                    .then((response) => response.json())
                    .then((json) => {
                        if (typeof json === 'object'
                                && json.hasOwnProperty("responseCodeEnum")
                                && json["responseCodeEnum"] !== '0') {
                            errorStatus(json["responseCode"]);
                        } else {
                            successStatus();
                        }
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

                const startSubmit = (formData) => {
                    if (contactForm) {
                        submitBtn.setAttribute('disabled', 'disabled');
                        sendForm(contactForm, formData);
                    } else {
                        errorStatus('Invalid configuration: contactForm not found');
                    }
                };

                const isValidEmail = (emailValue) => {
                    return typeof emailValue === 'string'
                        && /\b[\w\.-]+@[\w\.-]+\.\w{2,4}\b/gi.test(emailValue);
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

                const parseMailAddress = (messageType) =>  {
                    const selectedOption = messageType.selectedOptions[0];
                    if (selectedOption == null) {
                        return null;
                    }
                    const data = selectedOption.dataset;
                    if (data.valuePart1 == null || data.valuePart2 == null || data.valuePart3 == null) {
                        return null;
                    }

                    return data.valuePart1.trim() + "@" + data.valuePart2.trim() + "." + data.valuePart3.trim()
                }

                const parseFormData = (form) => {
                    return new Promise(async () => {
                        const data = new FormData();

                        data.set('email', form.email.value);
                        data.set('type', form.type.value);
                        data.set('sendTo', parseMailAddress(form.type));
                        data.set('name', form.name.value);
                        data.set('message', form.message.value);

                        await new Promise((resolve) => grecaptcha.ready(() => {
                            try {
                                grecaptcha.execute(captchaPublicKey, {action: 'submit'}).then((token) => {
                                    data.set('g-recaptcha-response', token);
                                    resolve(data);
                                })
                            } catch(err) {
                                errorStatus(err.message)
                            }
                        }));
                    });
                }

                const submitForm = () => {
                    parseFormData(form).then(formData => {
                        let email = false;
                        for (let data of formData.entries()) {
                            if (data[0] === 'email') {
                                email = data[1];
                            }
                        }
                        isValidEmail(email) ? startSubmit(formData) : showEmailError();
                    })
                };

                hideEmailError();
                hideNotifications();
                submitForm();
            });
        });
    });
};

initForm();
