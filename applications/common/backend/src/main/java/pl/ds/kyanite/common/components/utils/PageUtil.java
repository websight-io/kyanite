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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.ds.websight.pages.core.api.Page;
import pl.ds.websight.pages.core.api.PageConstants;
import pl.ds.websight.pages.core.api.PageManager;


public class PageUtil {
  private static final String JCR_CONTENT = "jcr:content";

  private PageUtil() {
    // no instance
  }

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

  /**
   * Get parent/containing page path.
   *
   * @param resource - any resource
   * @return  for components        - <b>containing</b> page path
   *          for page              - <b>parent</b> page path
   *          for jcr:content node  - <b>parent</b> (relatively to containing) page path
   */
  public static String getParentPagePath(Resource resource) {
    String resourcePath = resource.getPath();
    boolean isContentNode = org.apache.commons.lang3.StringUtils.endsWith(
        resourcePath, JCR_CONTENT);
    String resourcePurePath = removeContentSuffix(resourcePath);
    if (org.apache.commons.lang3.StringUtils.contains(resourcePurePath, JCR_CONTENT)) {
      //  is a component
      return org.apache.commons.lang3.StringUtils.substringBefore(resource.getPath(), JCR_CONTENT);
    } else {
      //  is a page or jcr:content node
      Resource currentPage = isContentNode ? resource.getParent() : resource;
      Resource parentPage = currentPage.getParent();
      return parentPage == null ? "" : parentPage.getPath();
    }
  }

  /**
   * Get parent/containing page resource.
   *
   * @param resource  any resource: component, page, jcr:content node
   * @param resourceResolver standard resource resolver
   * @return  for components        - <b>containing</b> page resource
   *          for page              - <b>parent</b> page resource
   *          for jcr:content node  - <b>parent</b> (relatively to containing) page resource
   */
  @Nullable
  public static Resource getParentPageResource(
      Resource resource, ResourceResolver resourceResolver
  ) {
    String containingPagePath = getParentPagePath(resource);
    return org.apache.commons.lang3.StringUtils.isBlank(containingPagePath)
        ? null
        : resourceResolver.getResource(containingPagePath);
  }

  /**
   * Remove jcr:content suffix.
   *
   * @param path absolute path to a component/page/content node
   * @return page/component path without suffix
   */
  public static String removeContentSuffix(String path) {
    return org.apache.commons.lang3.StringUtils.endsWith(path, JCR_CONTENT)
        ? StringUtils.substringBefore(path, JCR_CONTENT)
        : path;
  }

  /**
   * return jcr:content node for specified resource,
   * or return resource itself if it's a content node or if it doesn't have one.
   *
   * @param resource any resource: component/page/content node
   * @return jcr:resource node
   */
  public static Resource getContentNode(Resource resource) {
    Resource content = resource.getChild(JCR_CONTENT);
    return content == null ? resource : content;
  }

  public static Collection<Resource> getDescendants(Resource resource) {
    final Collection<Resource> children = Optional.ofNullable(resource)
        .map(res -> StreamSupport.stream(res.getChildren().spliterator(), false).collect(
            Collectors.toSet()))
        .orElse(Collections.emptySet());
    return children.isEmpty() ? Collections.emptySet()
        : Stream.concat(children.stream(), children.stream()
                .map(PageUtil::getDescendants)
                .flatMap(Collection::stream))
            .collect(Collectors.toSet());
  }
}
