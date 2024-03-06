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

import { breakpoints } from "src/helpers/breakpoints";

document.addEventListener('scroll', () => {
    const navbar = document.querySelector('.navbar');
    const isTransparent = navbar.classList.contains('is-transparent');
    if (isTransparent) {
        if (window.scrollY > 10) {
            navbar.classList.add('is-scrolled');
        } else {
            navbar.classList.remove('is-scrolled');
        }
    }
})
document.addEventListener(window.KYANITE_ON_DOM_CONTENT_LOAD, () => {

    handleFixedNavbarPosition();

    // Get all "navbar-burger" elements
    const $navbarBurgers = Array.prototype.slice.call(document.querySelectorAll('.navbar-burger'), 0);

    // Add a click event on each of them
    $navbarBurgers.forEach( el => {
        el.addEventListener('click', () => {

            // Get the target from the "data-target" attribute
            const target = el.dataset.target;
            const $target = document.getElementById(target);

            // Toggle the "is-active" class on both the "navbar-burger" and the "navbar-menu"
            el.classList.toggle('is-active');
            $target.classList.toggle('is-active');

        });
    });

    // Get all dropdowns items
    const $navbarMega = Array.prototype.slice.call(document.querySelectorAll('.navbar-end .navbar-item.has-dropdown'));
    const desktopMQ = window.matchMedia(`(min-width: ${breakpoints.lg}px)`);

    const onClick = (el) => {
        el.target.parentElement.classList.toggle('is-active');
    }

    const actionForNavbarMega = (desktopMQ) => {
        // On desktop the dropdowns are hoverable
        if (desktopMQ.matches) {
            $navbarMega.forEach((el) => {
                el.classList.add('is-hoverable');
                el.classList.remove('is-active');
                el.removeEventListener('click', onClick);
            });

            return;
        }

        // On mobile the dropdowns are clickable
        $navbarMega.forEach((el) => {
            el.classList.remove('is-hoverable');
            el.classList.remove('is-active');
            el.addEventListener('click', onClick);
        });
    }

    actionForNavbarMega(desktopMQ);

    desktopMQ.addEventListener('change', () => actionForNavbarMega(desktopMQ));
});

/**
 * Bulma implements possibility to fix the navbar position to the top or bottom of the page.
 * https://bulma.io/documentation/components/navbar/#fixed-navbar
 *
 * Documentation tells to add is-fixed-top/is-fixed-bottom class to the navbar
 * and has-navbar-fixed-top/has-navbar-fixed-bottom class to the body.
 * Classes added to the body use a static value for the navbar height, which is not correct -
 * the navbar height may differ based on brand logo used and depending on if it contains meta-menu or not.
 *
 * Instead of adding the class to the body, navbar height is dynamically calculated and proper styles are added .
 */
function handleFixedNavbarPosition() {
    const navbar = document.querySelector('.navbar');
    const shouldPageHaveNavbarPadding = !document.body.classList.contains('no-navbar-padding');
    if (navbar && shouldPageHaveNavbarPadding) {
        const navbarHeight = navbar.getBoundingClientRect().height;
        if (navbar.classList.contains('is-fixed-top')) {
            document.body.style.paddingTop = `${navbarHeight}px`;
        } else if (navbar.classList.contains('is-fixed-bottom')) {
            document.body.style.paddingBottom = `${navbarHeight}px`;
        }
    }
}
