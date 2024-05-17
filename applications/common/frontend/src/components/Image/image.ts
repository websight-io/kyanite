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

const createSeparatePreviews = (imageList) => {
  imageList.forEach((image) => {
    new Viewer(image, {
      navbar: false,
      toolbar: false,
      zoomable: false,
      movable: false,
      title: false,
      url: 'data-lightbox-asset',
      show() {
        document.querySelector("html").style.overflowY = "hidden";
      },
      hidden() {
        document.querySelector("html").style.overflowY = "scroll";
      }
    });
  });
};

const createPreviewCarousel = (imageContainer) => {
  new Viewer(imageContainer, {
    navbar: false,
    zoomable: false,
    movable: false,
    title: false,
    scalable: false,
    url: 'data-lightbox-asset',
    toolbar: {
      prev: 1,
      next: 1,
    },
    show() {
      document.querySelector("html").style.overflowY = "hidden";
    },
    hidden() {
      document.querySelector("html").style.overflowY = "scroll";
    }
  });
}

if (imageCarouselTemplates.some(className => document.body.classList.contains(className))) {
  const imageContainer = document.querySelector<HTMLElement>(".blogArticleContent");
  const previewCarouselImages = document.querySelectorAll<HTMLElement>(".blogArticleContent .image");

  previewCarouselImages.forEach(imageContainer => {
    const imageElement = imageContainer.querySelector("img");
    imageElement.classList.add("previewCarouselImage");
    imageContainer.innerHTML += '<svg class="previewIcon" width="30" height="30" viewBox="9 9 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">\n' +
        '  <rect width="45" height="45" rx="24" x="9" y="9" fill="#1B1C20"/>\n' +
        '  <path fill-rule="evenodd" clip-rule="evenodd" d="M32 24H40V32H42V22H32V24ZM24 32H22V42H32V40H24V32Z" fill="#ECF5FF"/>\n' +
        '</svg>';
  });
  createPreviewCarousel(imageContainer);
} else {
  const imagesWithLightbox = document.querySelectorAll<HTMLImageElement>(
      ".image--lightbox"
  );
  createSeparatePreviews(imagesWithLightbox);
}