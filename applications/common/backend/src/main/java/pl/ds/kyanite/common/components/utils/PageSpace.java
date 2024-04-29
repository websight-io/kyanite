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
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.jetbrains.annotations.Nullable;

@Getter
public class PageSpace {

  private final Resource pageSpaceResource;

  private PageSpace(Resource pageSpaceResource) {
    this.pageSpaceResource = pageSpaceResource;
  }

  @Nullable
  public static PageSpace forResource(Resource resource) {
    return forResource(resource.getPath(), resource.getResourceResolver());
  }

  @Nullable
  public static PageSpace forResource(String resourcePath, ResourceResolver resourceResolver) {
    String pageSpacePath = getCurrentSpace(resourcePath);
    if (pageSpacePath == null) {
      return null;
    }

    Resource pageSpaceResource = resourceResolver.getResource(pageSpacePath);
    if (pageSpaceResource == null) {
      return null;
    }

    return new PageSpace(pageSpaceResource);
  }

  public <T> T getPageSpaceTemplateProperty(String propertyName, T defaultValue) {
    Resource pageSpaceTemplate = getPageSpaceTemplate();
    if (pageSpaceTemplate == null) {
      return null;
    }

    return pageSpaceTemplate.getValueMap().get(propertyName, defaultValue);
  }

  @Nullable
  public String getWsPagesSpaceName() {
    Resource spaceResource = pageSpaceResource.getParent();
    if (spaceResource == null) {
      return null;
    }

    return spaceResource.getName();
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

  private Resource getPageSpaceTemplate() {
    String templatePath = pageSpaceResource.getValueMap().get("ws:template", StringUtils.EMPTY);
    return pageSpaceResource.getResourceResolver().getResource(templatePath);
  }

}
