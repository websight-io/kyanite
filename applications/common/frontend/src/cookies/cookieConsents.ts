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

/* eslint-disable max-len */
import 'vanilla-cookieconsent';

const cc: CookieConsent = initCookieConsent();

export default cc.run({
  current_lang: 'en',
  autoclear_cookies: true,
  page_scripts: true,
  languages: {
    en: {
      consent_modal: {
        title: 'We use cookies!',
        description:
          'Hi, this website uses essential cookies to ensure its proper operation and tracking cookies to understand how you interact with it. The latter will be set only after consent.',
        primary_btn: {
          text: 'Accept all',
          role: 'accept_all', // 'accept_selected' or 'accept_all'
        },
        secondary_btn: {
          text: 'Customize',
          role: 'settings',
        },
      },
      settings_modal: {
        title: 'Cookie preferences',
        save_settings_btn: 'Save settings',
        accept_all_btn: 'Accept all',
        close_btn_label: 'Close',
        cookie_table_headers: [
          { col1: 'Name' },
          { col2: 'Domain' },
          { col3: 'Expiration' },
          { col4: 'Description' },
        ],
        blocks: [
          {
            title: 'Cookie usage',
            description:
              'I use cookies to ensure the basic functionalities of the website and to enhance your online experience. You can choose for each category to opt-in/out whenever you want. For more details relative to cookies and other sensitive data, please read the full <a href="/privacy-policy.html" class="cc-link">privacy policy</a>.',
          },
          {
            title: 'Strictly necessary cookies',
            description:
              'These cookies are essential for the proper functioning of my website. Without these cookies, the website would not work properly',
            toggle: {
              value: 'necessary',
              enabled: true,
              readonly: true, // cookie categories with readonly=true are all treated as "necessary cookies"
            },
          },
          {
            title: 'Performance, Analytics and Targeting cookies',
            description:
              'These cookies allow the website to remember the choices you have made in the past',
            toggle: {
              value: 'analytics', // your cookie category
              enabled: true,
              readonly: false,
            },
            cookie_table: [
              // list of all expected cookies
              {
                col1: '^_ga', // match all cookies starting with "_ga"
                col2: 'google.com',
                col3: '2 years',
                col4: 'Analytics cookies',
                is_regex: true,
              },
              {
                col1: '_gid',
                col2: 'google.com',
                col3: '1 day',
                col4: 'Analytics cookies',
              },
              {
                col1: 'bcookie',
                col2: 'linkedin.com',
                col3: '1 year',
                col4: 'Stores browser details',
              },
              {
                col1: 'bscookie',
                col2: 'www.linkedin.com',
                col3: '1 year',
                col4: 'Stores performed actions on the website',
              },
              {
                col1: 'JSESSIONID',
                col2: 'www.linkedin.com',
                col3: 'session',
                col4: 'Protects Cross Site Request Forgery (CSRF) and validates URL signature',
              },
              {
                col1: 'lang',
                col2: 'linkedin.com',
                col3: 'session',
                col4: "User's language setting",
              },
              {
                col1: 'lidc',
                col2: 'linkedin.com',
                col3: '1 day',
                col4: 'Stores performed actions on the website',
              },
              {
                col1: 'sdsc',
                col2: 'linkedin.com',
                col3: 'session',
                col4: 'Ensure consistency across all databases when a change is made',
              },
              {
                col1: 'li_gc',
                col2: 'linkedin.com',
                col3: '6 months',
                col4: 'Stores consent of guests regarding the use of cookies for non-essential purposes',
              },
              {
                col1: 'li_mc',
                col2: 'linkedin.com',
                col3: '6 months',
                col4: "Temporary cache to avoid database lookups for a member's consent",
              },
              {
                col1: 'AnalyticsSyncHistory',
                col2: 'linkedin.com',
                col3: '30 days',
                col4: 'Stores information about the time a sync took place with the lms_analytics cookie',
              },
              {
                col1: 'lms_analytics',
                col2: 'linkedin.com',
                col3: '30 days',
                col4: 'Identifies LinkedIn Members off LinkedIn for analytics',
              },
              {
                col1: 'UID',
                col2: 'scorecardresearch.com',
                col3: '720 days',
                col4: 'Used for market and user research',
              },
              {
                col1: 'UserMatchHistory',
                col2: 'linkedin.com',
                col3: '30 days',
                col4: 'LinkedIn Ads ID syncing',
              },
              {
                col1: 'lms_ads',
                col2: '.linkedin.com',
                col3: '30 days',
                col4: 'Identifies LinkedIn Members off LinkedIn for advertising',
              },
              {
                col1: 'li_fat_id',
                col2: 'first party domain',
                col3: '30 days',
                col4: 'Member indirect identifier for Members for conversion tracking, retargeting, analytics',
              },
              {
                col1: 'li_sugr',
                col2: '.linkedin.com',
                col3: '90 days',
                col4: 'Stores browser details',
              },
              {
                col1: '_guid',
                col2: 'linkedin.com',
                col3: '90 days',
                col4: 'Identifies a LinkedIn Member for advertising through Google Ads',
              },
              {
                col1: 'BizographicsOptOut',
                col2: '.linkedin.com',
                col3: '10 years',
                col4: 'Stores privacy preferences',
              },
              {
                col1: 'li_giant',
                col2: 'first party domain',
                col3: '7 days',
                col4: 'Indirect indentifier for groups of LinkedIn Members used for conversion tracking',
              },
            ],
          },
          {
            title: 'More information',
            description:
              'For any queries in relation to our policy on cookies and your choices, please <a class="cc-link" href="/about/contact-us.html">contact us</a>.',
          },
        ],
      },
    },
  },
  gui_options: {
    consent_modal: {
      layout: 'bar', // box/cloud/bar
      position: 'bottom center', // bottom/middle/top + left/right/center
      transition: 'slide', // zoom/slide
      swap_buttons: false, // enable to invert buttons
    },
    settings_modal: {
      layout: 'box', // box/bar
      // position: 'left',           // left/right
      transition: 'slide', // zoom/slide
    },
  },
});

document.addEventListener(window.KYANITE_ON_DOM_CONTENT_LOAD, () => {
  const openModalLinks: NodeListOf<Element> = document.querySelectorAll(
    'a[href="#cookieSettings"'
  );

  openModalLinks.forEach((link) => {
    link.addEventListener('click', (e) => {
      e.preventDefault();
      cc.showSettings(0);
    });
  });
});
