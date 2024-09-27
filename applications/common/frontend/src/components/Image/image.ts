/*
 * Copyright (C) 2024 Dynamic Solutions
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

import Viewer from "viewerjs";

/**
 *  There are two cases: preview carousel and separate previews for every message.
 *  They differ by presence of 'carousel root' container with 'previewImageContainer' class.
 *  Only the body can be the root for preview carousel since https://teamds.atlassian.net/browse/KYAN-291
 *
 *  Examples of structure:
 *    1)  preview carousel:
 *          <body class="previewImageContainer">
 *            ...
 *              <figure class="image">
 *                ...
 *                  <img class="image--lightbox">
 *    2)  separate previews:
 *          <body>
 *            ...
 *              <figure class="image">
 *                ...
 *                  <img class="image--lightbox">
 */

const IMAGES_PREVIEW_ROOT_SELECTOR = `body.previewImageContainer`;
const IMAGE_PREVIEW_IMAGE_SELECTOR = "img.image--lightbox";
const IMAGE_PREVIEW_CONTAINER_SELECTOR = `.image:has(${IMAGE_PREVIEW_IMAGE_SELECTOR})`;

const onViewed = (event, viewer) => {
  const heightFillFactor = 0.8;
  const widthFillFactor = 0.9;
  const availVieportHeight = viewer.containerData.height * heightFillFactor;
  const availVieportWidth = viewer.containerData.width * widthFillFactor;
  const viewerInstance = viewer;
  const { naturalHeight, naturalWidth } = viewerInstance.imageData;

  // Calculate aspect ratios
  const imageAspectRatio = naturalWidth / naturalHeight;
  const viewportAspectRatio = availVieportWidth / availVieportHeight;

  // Determine if should fit to width or height
  let zoomRatio;
  if (imageAspectRatio < viewportAspectRatio) {
    // Fit to height
    zoomRatio = availVieportHeight / naturalHeight;
  } else {
    // Fit to width
    zoomRatio = availVieportWidth / naturalWidth;
  }

  viewer.zoomTo(zoomRatio, false);
};

const defaultSettings = {
  navbar: false,
  toolbar: false,
  zoomable: true,
  movable: false,
  title: false,
  url: 'data-lightbox-asset',
  zoomOnTouch: false,
  zoomOnWheel: false,
  scalable: false,
  toggleOnDblclick: false,
  // use the default value from the library but without `loading` attibute,
  // becasue there are some problems in Safari - it is not working with lazy loaded images
  inheritedAttributes: [
    'crossOrigin',
    'decoding',
    'isMap',
    'referrerPolicy',
    'sizes',
    'srcset',
    'useMap',
  ],
  // transition needs to be disabled because there is strange animation pause between initial image and the zoomed one.
  // there is no way to set the default zoom level at the start (https://github.com/fengyuanchen/viewerjs/issues/268)
  transition: false,
}

const createSeparatePreviews = (imageList) => {
  imageList.forEach((image) => {
    const viewer = new Viewer(image, {
     ...defaultSettings,
    show() {
      document.querySelector("html").style.overflowY = "hidden";
    },
    hidden() {
      document.querySelector("html").style.overflowY = "scroll";
    },
    viewed(e) {
      onViewed(e, viewer);
    },
    });
  });
};

const createPreviewCarousel = (imageContainer) => {
  const viewer = new Viewer(imageContainer, {
    ...defaultSettings,
    toolbar: {
      prev: 1,
      next: 1,
    },
    show() {
      document.querySelector("html").style.overflowY = "hidden";
    },
    hidden() {
      document.querySelector("html").style.overflowY = "scroll";
    },
    viewed(e) {
      onViewed(e, viewer);
    },
  });
};

window.addEventListener(window.KYANITE_ON_LOAD, () => {

  const previewsCarouselContainer = document.querySelector<HTMLElement>(
      IMAGES_PREVIEW_ROOT_SELECTOR);
  const previewsRootContainer = previewsCarouselContainer ? previewsCarouselContainer : document.body;

  const previewImageContainers = previewsRootContainer.querySelectorAll<HTMLElement>(
      IMAGE_PREVIEW_CONTAINER_SELECTOR);
  if (previewImageContainers.length == 0) return;

  previewImageContainers.forEach((imageContainer) => {
    imageContainer.innerHTML +=
        '<svg class="previewIcon" width="30" height="30" viewBox="9 9 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">\n' +
        '  <rect width="45" height="45" rx="24" x="9" y="9" fill="#1B1C20"/>\n' +
        '  <path fill-rule="evenodd" clip-rule="evenodd" d="M32 24H40V32H42V22H32V24ZM24 32H22V42H32V40H24V32Z" fill="#ECF5FF"/>\n' +
        '</svg>';
  });

  const previewImages = previewsRootContainer.querySelectorAll<HTMLElement>(
      `${IMAGE_PREVIEW_CONTAINER_SELECTOR} ${IMAGE_PREVIEW_IMAGE_SELECTOR}`
  );

  if (previewsCarouselContainer) {
    previewImages.forEach((imageElement) => {
      imageElement.classList.add('previewCarouselImage');
    });
    createPreviewCarousel(previewsCarouselContainer);
  } else {
    createSeparatePreviews(previewImages)
  }
});
