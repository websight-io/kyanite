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

/**
 * This file is loaded only in WCM disabled mode. It's not present in edit or publish mode.
 * Used only for end-user.
 * Loaded in `head-libs.html` & `body-content.html`
 */

// styles
import './main.published.scss';

// scripts
import './helpers/load-events';
import './cookies/cookieConsents';