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

// We may extend this array later with additional pages that need to have an image preview carousel
const imageCarouselTemplates = ["template-blogarticlepage"];

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

if (
  imageCarouselTemplates.some((className) =>
    document.body.classList.contains(className)
  )
) {
  const imageContainer = document.querySelector<HTMLElement>(
    ".previewImageContainer"
  );
  const previewCarouselImages = document.querySelectorAll<HTMLElement>(
    ".previewImageContainer .image"
  );

  previewCarouselImages.forEach((imageContainer) => {
    const imageElement = imageContainer.querySelector("img");
    imageElement.classList.add("previewCarouselImage");
    imageContainer.innerHTML += `
      <svg class="previewIcon" width="30" height="30" viewBox="9 9 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
        <rect width="45" height="45" rx="24" x="9" y="9" fill="#1B1C20"/>
        <path fill-rule="evenodd" clip-rule="evenodd" d="M32 24H40V32H42V22H32V24ZM24 32H22V42H32V40H24V32Z" fill="#ECF5FF"/>
      </svg>
    `;
  });
  createPreviewCarousel(imageContainer);
} else {
  const imagesWithLightbox =
    document.querySelectorAll<HTMLImageElement>('.image--lightbox');
  createSeparatePreviews(imagesWithLightbox);
}
