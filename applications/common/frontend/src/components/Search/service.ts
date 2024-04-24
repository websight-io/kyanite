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

// must be type, not interface because in Algolia code BaseItem is defined like this:
// export declare type BaseItem = Record<string, unknown>;
// and only types in TS infer an implicit index signature
export type Page = {
  path: string;
  title: string;
  bestFragment: string;
  score: number;
};

export interface GetPagesResponse {
  items: Page[];
  executionTimeMs: number;
}

export const SEARCH_RESULTS_LIMIT = 40;

const buildUrl = (endpoint: string, query: string, limit = SEARCH_RESULTS_LIMIT) => {
  return `${endpoint}/search?limit=${limit}&query=${query}*`;
};

export const getPages = async (query: string, endpoint = "", limit) => {
  const response = await fetch(buildUrl(endpoint, query, limit ?? SEARCH_RESULTS_LIMIT));
  return response.json() as Promise<GetPagesResponse>;
};
