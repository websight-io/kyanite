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

package pl.ds.kyanite.fragments.api.models;

import static pl.ds.kyanite.fragments.api.models.ExperienceFragmentConstants.DEFAULT_TEMPLATES_PATH;

import java.util.UUID;

public interface ExperienceFragment {

  boolean isValidPage();

  String getPagePath();

  String getResource();

  default String preparePagePath(String pagePath) {
    String path = pagePath;

    if (pagePath == null) {
      return null;
    }

    if (pagePath.endsWith("/")) {
      path = pagePath.substring(0, pagePath.length() - 1);
    }

    return path + ".html";
  }

  /**
   * Get unique ID for this fragment instance to tell it from other fragments on the page.
   *
   * @return unique ID string
   */
  default String getFragmentInstanceId() {
    return UUID.randomUUID().toString();
  }

  default String getIncludeFailTemplatePath() {
    return DEFAULT_TEMPLATES_PATH + "fragment-error-virtual-include-fail-template.html";
  }

  default String getReferenceMissingTemplatePath() {
    return DEFAULT_TEMPLATES_PATH + "fragment-error-reference-missing-template.html";
  }

}
