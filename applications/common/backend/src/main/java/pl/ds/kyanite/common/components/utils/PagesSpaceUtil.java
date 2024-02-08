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

package pl.ds.kyanite.common.components.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

public class PagesSpaceUtil {

  public static Resource getSpace(String resourcePath, ResourceResolver resourceResolver) {
    String spacePath = getCurrentSpace(resourcePath);
    if (spacePath == null) {
      return null;
    }

    return resourceResolver.getResource(spacePath);
  }

  public static String getWsPagesSpaceName(String resourcePath, ResourceResolver resourceResolver) {
    Resource pagesSpace = getSpace(resourcePath, resourceResolver);
    if (pagesSpace == null) {
      return StringUtils.EMPTY;
    }

    return pagesSpace.getParent().getName();
  }

  private static String getCurrentSpace(String resourcePath) {
    String regex = "^((/content|/published)/[^/]*/pages/).*";
    if (StringUtils.isNotBlank(resourcePath)) {
      final Pattern pattern = Pattern.compile(regex);
      final Matcher matcher = pattern.matcher(resourcePath);

      if (matcher.find()) {
        return matcher.group(1);
      }
    }
    return null;
  }

  private PagesSpaceUtil() {
    // no instance
  }

}