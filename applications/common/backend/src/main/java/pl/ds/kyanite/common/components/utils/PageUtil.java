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

package pl.ds.kyanite.common.components.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.jetbrains.annotations.NotNull;
import pl.ds.websight.pages.core.api.Page;
import pl.ds.websight.pages.core.api.PageConstants;
import pl.ds.websight.pages.core.api.PageManager;


public class PageUtil {
  private static final String JCR_CONTENT = "jcr:content";

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
      String templateResourcePath = resource.getChild(JCR_CONTENT).getValueMap()
          .get(PageConstants.PN_WS_TEMPLATE, String.class);

      if (templateResourcePath == null) {
        return false;
      }

      return templateResourcePath.equals(template)
          || isSuperResourceType(resource, templateResourcePath, template);
    };
  }

  private static boolean isSuperResourceType(Resource resource, String templateResourcePath,
      String template) {
    Resource templateResource = resource.getResourceResolver()
        .getResource(templateResourcePath);
    if (templateResource == null) {
      return false;
    }

    return templateResource.isResourceType(template);
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

}