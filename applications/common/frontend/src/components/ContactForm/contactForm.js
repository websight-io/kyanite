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

            const clsFormSubmitted = 'form-submitted';
            const clsFormSubmitSuccess = 'form-success';
            const clsFormSubmitFailure = 'form-failure';

            const addFormClass = (clsName) => {
                !contactForm.classList.contains(clsName) && contactForm.classList.add(clsName);
            }

            const removeFormClass = (clsName) => {
                contactForm.classList.contains(clsName) && contactForm.classList.remove(clsName);
            }

            const toggleFormClass = (clsName, add) => {
                add ? addFormClass(clsName) : removeFormClass(clsName);
            }

            const resetFormResult = () => {
                removeFormClass(clsFormSubmitSuccess);
                removeFormClass(clsFormSubmitFailure);
            }

            const setSubmitted = (isSubmitted) => {
                toggleFormClass(clsFormSubmitted, isSubmitted);
                !isSubmitted && resetFormResult();
            }

            const setSuccess = (isSuccess) => {
                resetFormResult();
                addFormClass(isSuccess ? clsFormSubmitSuccess : clsFormSubmitFailure);
            }

            contactForm.addEventListener('submit', (e) => {
                e.preventDefault();

                const form = e.target;
                const submitBtn = form.getElementsByClassName('button')[0];
                const emailValidateErrorEl = form.getElementsByClassName('email-validate-error')[0];
                const emailInputEl = form.getElementsByClassName('email-input')[0];
                const contactFormEndpoint = form.dataset.configHttpEndPoint;
                const captchaPublicKey = form.dataset.configCaptchaPublicKey;

                const errorStatus = (err) => {
                    console.error(err);
                    setSuccess(false);
                    submitBtn.removeAttribute('disabled');
                };

                const successStatus = () => {
                    setSuccess(true);
                    submitBtn.removeAttribute('disabled');
                    form.reset();
                };

                if (captchaPublicKey === undefined) {
                    setSubmitted(true);
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

                const startSubmit = (formData) => {
                    if (contactFormEndpoint) {
                        setSubmitted(true);
                        submitBtn.setAttribute('disabled', 'disabled');
                        sendForm(contactFormEndpoint, formData);
                    } else {
                        errorStatus('Invalid configuration: contactFormEndpoint not found');
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
                    return new Promise(async (resolve, reject) => {
                        const data = new FormData();

                        data.set('email', form.email.value);
                        data.set('type', form.type.value);
                        data.set('pageSpace', form.pageSpace.value);
                        data.set('pageName', form.pageName.value);
                        data.set('sendTo', parseMailAddress(form.type));
                        data.set('firstName', form.firstName.value);
                        data.set('lastName', form.lastName.value);
                        data.set('companyName', form.companyName.value);
                        data.set('message', form.message.value);

                        await new Promise((resolveCaptchaInit, rejectCaptchaInit) => {
                            try {
                                grecaptcha.ready(() => resolveCaptchaInit());
                            } catch (err) {
                                rejectCaptchaInit(err);
                            }
                        })
                        .then(() => {
                            grecaptcha.execute(captchaPublicKey, {action: 'submit'})
                            .then((token) => {
                                data.set("g-recaptcha-response", token);
                                resolve(data);
                            })
                        })
                        .catch(err => reject(err));
                    });
                }

                const submitForm = () => {
                    parseFormData(form)
                    .then(formData => {
                        let email = false;
                        for (let data of formData.entries()) {
                            if (data[0] === 'email') {
                                email = data[1];
                            }
                        }
                        isValidEmail(email) ? startSubmit(formData) : showEmailError();
                    })
                    .catch(err => errorStatus(err));
                };

                hideEmailError();
                setSubmitted(false);
                submitForm();
            });

            Array.from(contactForm.getElementsByClassName('after-submit-button'))
                .forEach((b) => {
                    b.addEventListener('click', (e) => {
                        setSubmitted(false);
                    });
            })
        });
    });
};

initForm();
