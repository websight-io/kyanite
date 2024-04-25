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

import { HTMLTemplate, autocomplete } from "@algolia/autocomplete-js";
import { Page, getPages } from "./service";

const getAutocompleteItemUrl = (item: Page, spaceName: string) => {
  return item.path.replace(`/published/${spaceName}/pages`, "");
};

const getItemTemplate = (html: HTMLTemplate, item: Page, spaceName: string) => {
  return html`<a class="aa-ItemLink" href=${getAutocompleteItemUrl(item, spaceName)}>
    <div class="aa-ItemContent">
      <div class="aa-ItemIcon aa-ItemIcon--alignTop">
        <svg
          aria-labelledby="title"
          viewBox="0 0 24 24"
          fill="none"
          stroke-linecap="round"
          stroke-linejoin="round"
          stroke="currentColor"
          stroke-width="2"
          class="block h-full w-auto"
          role="img"
        >
          <title id="title">Guide</title>
          <path
            d="M13 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V9z"
          ></path>
          <polyline points="13 2 13 9 20 9"></polyline>
        </svg>
      </div>
      <div class="aa-ItemContentBody">
        <div
          class="aa-ItemContentTitle"
          dangerouslySetInnerHTML="${{ __html: item.title }}"
        ></div>
        <div
          class="aa-ItemContentDescription"
          dangerouslySetInnerHTML="${{ __html: item.bestFragment }}"
        ></div>
      </div>
      <div class="aa-ItemActions">
        <button class="aa-ItemActionButton" type="button" title="Add to cart">
          <svg
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <polyline points="9 10 4 15 9 20"></polyline>
            <path d="M20 4v7a4 4 0 0 1-4 4H4"></path>
          </svg>
        </button>
      </div>
    </div>
  </a>`;
};

document.querySelectorAll<HTMLElement>("div.autocomplete-search").forEach((searchDiv) => {
  const endpoint  = searchDiv.getAttribute("data-search-endpoint");
  const limit     = searchDiv.getAttribute("data-page-limit");
  const spaceName = searchDiv.getAttribute("data-space-name");
  autocomplete<Page>({
    container: searchDiv,
    placeholder: "Search...",
    detachedMediaQuery: "",
    openOnFocus: false,
    getSources({ query }) {
      return [
        {
          sourceId: "pages",
          async getItems({ query }) {
            if (query.length < 3) {
              return [];
            }
            const pages = await getPages(query, endpoint, +limit);
            return pages.items;
          },
          getItemUrl({ item }) {
            return getAutocompleteItemUrl(item, spaceName);
          },
          templates: {
            item({ item, html }) {
              return getItemTemplate(html, item, spaceName);
            },
            noResults:
                query === ""
                    ? undefined
                    : () => {
                      return "No results";
                    },
          },
        }
      ];
    },
  });
})
