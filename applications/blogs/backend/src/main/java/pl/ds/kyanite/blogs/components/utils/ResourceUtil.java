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

package pl.ds.kyanite.blogs.components.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.jetbrains.annotations.Nullable;

public class ResourceUtil {

  public static final String JCR_CONTENT = "jcr:content";

  //  no instance
  private ResourceUtil() {
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
    boolean isContentNode = StringUtils.endsWith(resourcePath, JCR_CONTENT);
    String resourcePurePath = removeContentSuffix(resourcePath);
    if (StringUtils.contains(resourcePurePath, JCR_CONTENT)) {
      //  is a component
      return StringUtils.substringBefore(resource.getPath(), JCR_CONTENT);
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
    return StringUtils.isBlank(containingPagePath)
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
    return StringUtils.endsWith(path, JCR_CONTENT)
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
}
