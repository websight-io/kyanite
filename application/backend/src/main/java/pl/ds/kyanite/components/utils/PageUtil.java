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

package pl.ds.kyanite.components.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.jetbrains.annotations.NotNull;
import pl.ds.websight.pages.core.api.Page;
import pl.ds.websight.pages.core.api.PageConstants;
import pl.ds.websight.pages.core.api.PageManager;


public class PageUtil {
  private static final String JCR_CONTENT = "jcr:content";

  public static final String CONTENT = "/content";

  public static Page findTopLevelParentPageForCurrentPage(@NotNull Page page) {
    while (page.getParent() != null) {
      page = page.getParent();
    }

    return page;
  }

  public static Resource findSiblingPageForCurrentPage(@NotNull Page rootPage,
                                                       @NotNull String template) {
    List<Resource> resources = new ArrayList<>();
    Objects.requireNonNull(rootPage.getResource().getParent()).getChildren()
        .forEach(resources::add);

    return resources.stream()
        .filter(PageUtil.isResourceSelectedTemplate(template))
        .findFirst().orElse(null);
  }

  private static Predicate<Resource> isResourceSelectedTemplate(String template) {
    return resource -> {
      if (resource.getChild(JCR_CONTENT) == null) {
        return false;
      }
      Object o = resource.getChild(JCR_CONTENT).getValueMap()
          .get(PageConstants.PN_WS_TEMPLATE);
      return o != null && o.equals(template);
    };
  }

  public static String getPageProperty(Resource resource, String propName) {
    ResourceResolver resourceResolver = resource.getResourceResolver();
    PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
    Page containingPage = pageManager.getContainingPage(resource.getPath());
    return containingPage.getContentProperty(propName, String.class);
  }

  private PageUtil() {
    // no instance
  }

  public static String getCurrentPageSpace(String path) {
    String regex = "^(\\/content\\/[^\\/]*\\/).*";
    if (path.startsWith("/published")) {
      regex = "^(\\/published\\/[^\\/]*\\/).*";
    }
    if (StringUtils.isNotBlank(path)) {
      final Pattern pattern = Pattern.compile(regex);
      final Matcher matcher = pattern.matcher(path);

      if (matcher.find()) {
        return matcher.group(1);
      }
    }
    return CONTENT;
  }

}