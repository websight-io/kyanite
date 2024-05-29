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

require('dotenv').config();

const silent = process.env.SILENT === '1';

const { baseUrlsPublish } = require('./src/baseUrl');

const selectors = {
  cookie_banner: '#cc--main',
  header: '.navbar',
  footer: '.footer',
  body: 'body',
  content: 'body > section', // todo refactor name to section and plural all
  container: 'body > .container',
  all: 'body > *',
};

const viewports = [
  { label: 'md-mini', width: 360,  height: 480  },
  { label: 'md',      width: 768,  height: 1024 },
  { label: 'lg',      width: 1025, height: 768  },
  { label: 'xl',      width: 1216, height: 1024 },
  { label: 'mx',      width: 1408, height: 900  }
];

const getPublishedPageUrl = ({ space, page }) => {
  //FIXME: WS-3121
  if (space === 'kyanite-visual-tests-dark-mode') {
    return `${baseUrlsPublish[space]}/content/${space}/pages/${page}.html`;
  }
  return `${baseUrlsPublish[space]}/published/${space}/pages/${page}.html`;
};

const spaces = ['kyanite-visual-tests', 'kyanite-visual-tests-dark-mode'];

const postHoverInterval = 1000;
const postClickInterval = 1000;

const scenarios = spaces.flatMap(space => [
  { space: space, page: 'icons' },
  { space: space, page: 'heading-typography', selectors: [selectors.container] },
  { space: space, page: 'colors', selectors: [selectors.all] },
  { space: space, page: 'eyebrow', selectors: [selectors.container] },
  { space: space, page: 'content-typography', selectors: [selectors.container] },
  { space: space, page: 'level-item--feature-small', selectors: [selectors.container] },
  { space: space, page: 'level-item--feature', selectors: [selectors.content] },
  { space: space, page: 'button', selectors: [selectors.all] },
  { space: space, page: 'footer', selectors: [selectors.footer] },
  { space: space, page: 'level' },
  { space: space, page: 'link', selectors: [selectors.container] },
  { space: space, page: 'navbar', selectors: [selectors.header] },
  { space: space, page: 'navbar', selectors: [selectors.body],
    hoverSelector: '.navbar-item.has-dropdown.is-hoverable',
    viewports: viewports.filter((viewport) => ['lg', 'xl', 'mx'].includes(viewport.label)),
    postInteractionWait: postHoverInterval,
  },
  { space: space, page: 'navbar-megadropdown', selectors: [selectors.body],
    hoverSelector: '.navbar-item.has-dropdown.is-hoverable',
    viewports: viewports.filter((viewport) => ['lg', 'xl', 'mx'].includes(viewport.label)),
    postInteractionWait: postHoverInterval,
  },
  { space: space, page: 'navbar-megadropdown', selectors: [selectors.body],
    clickSelectors: ['.navbar-burger', '.navbar-item.has-dropdown'],
    viewports: viewports.filter((viewport) => ['md-mini', 'md'].includes(viewport.label)),
    postInteractionWait: postClickInterval,
  },
  { space: space, page: 'navbar-megadropdown-columns', selectors: [selectors.body],
    hoverSelector: '.navbar-item.has-dropdown.is-hoverable',
    viewports: viewports.filter((viewport) => ['lg', 'xl', 'mx'].includes(viewport.label)),
    postInteractionWait: postHoverInterval,
  },
  { space: space, page: 'blog-article-header/blog-article-page', selectors: [selectors.container] },
  { space: space, page: 'blog-listing', selectors: [selectors.container] },
  { space: space, page: 'blog-article-author-bio', selectors: [selectors.content] },
  { space: space, page: 'blog-listing-author-filtered', selectors: [selectors.content] },
  { space: space, page: 'blog-listing-author-filtered/blog-category-1-author-filtered', selectors: [selectors.content] },
  { space: space, page: 'blog-listing-author-filtered/blog-category-2', selectors: [selectors.content] },
  { space: space, page: 'blog-listing-author-filtered/author-1', selectors: [selectors.content] },
  { space: space, page: 'blog-listing-author-filtered/author-2', selectors: [selectors.content] },
  { space: space, page: 'code-snippet', selectors: ['.code-snippet'] },
  { space: space, page: 'grid/grid-columns-number', selectors: ['.grid'] },
  { space: space, page: 'grid/grid-layout',         selectors: ['.grid'] },
  { space: space, page: 'hero', selectors: ['.hero'] },
  { space: space, page: 'message', selectors: [selectors.container] },
  { space: space, page: 'table-of-content/table-of-content' },
  { space: space, page: 'card-full-height-variant', selectors: [selectors.body] },
  { space: space, page: 'navbar-external-links', selectors: [selectors.body],
    label: `${space}_external-link-meganav`,
    clickSelector: '.navbar-item.has-dropdown.is-hoverable',
    viewports: viewports.filter((viewport) => ['lg', 'xl', 'mx'].includes(viewport.label)),
    postInteractionWait: postClickInterval,
  },
  { space: space, page: 'navbar-external-links', selectors: [selectors.body],
    label: `${space}_external-link-dropdown-menu`,
    clickSelector: '.navbar-item.has-dropdown.is-hoverable:nth-of-type(2)',
    viewports: viewports.filter((viewport) => ['lg', 'xl', 'mx'].includes(viewport.label)),
    postInteractionWait: postClickInterval,
  },
  { space: space, page: 'container/container-alignment',  selectors: [ selectors.container] },
  { space: space, page: 'level/level-item-alignment',     selectors: [ selectors.all] },
  { space: space, page: 'carousel/carousel-itemsshown-06-noscroll', selectors: [selectors.all] },
  { space: space, page: 'carousel/carousel-itemsshown-06-scroll',   selectors: [selectors.all] },
  { space: space, page: 'carousel/carousel-itemsshown-12-noscroll', selectors: [selectors.all] },
  { space: space, page: 'carousel/carousel-itemsshown-12-scroll',   selectors: [selectors.all] },
  { space: space, page: 'table',   selectors: [selectors.all] },
  { space: space, page: 'tag',   selectors: [selectors.container] },

  { space: space, page: 'tabs', selectors: ['.tabs-container'],
    label: `${space}_tabs-container-horizontal-desktop`,
    viewports: viewports.filter((viewport) => ['xl', 'mx'].includes(viewport.label)),
  },
  { space: space, page: 'tabs', selectors: ['.tabs-mobile-container'],
    label: `${space}_tabs-container-horizontal-mobile`,
    viewports: viewports.filter((viewport) => ['md-mini', 'md', 'lg'].includes(viewport.label)),
  },
  { space: space, page: 'tabs/tabs-layouts-and-styles', selectors: ['.tabs-container.is-vertical'],
    label: `${space}_tabs-container-vertical`,
  },
  { space: space, page: 'tabs/tabs-layouts-and-styles', selectors: ['.tabs-container:not(.is-vertical)'],
    label: `${space}_tabs-container-horizontal-desktop`,
    viewports: viewports.filter((viewport) => ['xl', 'mx'].includes(viewport.label)), },
  { space: space, page: 'tabs/tabs-layouts-and-styles', selectors: ['.tabs-mobile-container'],
    label: `${space}_tabs-container-horizontal-mobile`,
    viewports: viewports.filter((viewport) => ['md-mini', 'md', 'lg'].includes(viewport.label)), },
])
  .map((scenario) => {
    const removeSelectors = [
      'script',
      'noscript',
      selectors.cookie_banner,
    ];

    const deafultScenarioConfig = {
      removeSelectors,
      label: `${scenario.space}_${scenario.page}`,
      cookiePath: 'backstop_data/engine_scripts/cookies.json',
      url: getPublishedPageUrl(scenario),
      delay: 500,
      postInteractionWait: 0,
      selectorExpansion: true,
      expect: 0,
      /*
       * sometimes page height is ~100px higher than normal,
       * the website looks correct, but footer looks like with `padding-bottom: 100px`
       * maybe that's what's left after removing `selectors.cookie_banner`?
       */
      requireSameDimensions: false,
    };

    const scenarioConfig = { ...deafultScenarioConfig, ...scenario };

    return scenarioConfig;
  });

const config = {
  id: 'kyanite',
  viewports: viewports,
  onBeforeScript: 'puppet/onBefore.js',
  onReadyScript: 'puppet/onReady.js',
  scenarios: [...scenarios],
  paths: {
    bitmaps_reference: 'backstop_data/bitmaps_reference',
    bitmaps_test: 'backstop_data/bitmaps_test',
    engine_scripts: 'backstop_data/engine_scripts',
    html_report: 'backstop_data/html_report',
    ci_report: 'backstop_data/ci_report'
  },
  report: [process.env.CI ? 'CI' : 'browser'],
  misMatchThreshold: 0.01,
  /*
   * puppeteer is headless
   * see https://github.com/garris/BackstopJS#chrome-headless-the-latest-webkit-library
   *
   * warning, changing to phantomjs consumes RAM ~1.5G + comparison may also need >0.5G
   * see https://github.com/garris/BackstopJS/issues/561#issuecomment-335194574
   */
  engine: 'puppeteer',
  engineOptions: {
    headless: 'new',
    args: ['--no-sandbox', '--headless="new"']
  },
  asyncCaptureLimit: process.env.CI ? 5 : 30,
  asyncCompareLimit: process.env.CI ? 10 : 60,
  debug: false,
  debugWindow: false
};

if (!silent) {
  console.log(JSON.stringify({backstopJS_config: config}, null, 4));
}

module.exports = config;
