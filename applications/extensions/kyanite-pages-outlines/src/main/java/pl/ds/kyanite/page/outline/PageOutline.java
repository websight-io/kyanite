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

package pl.ds.kyanite.page.outline;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import pl.ds.websight.pages.core.api.PageException;
import pl.ds.websight.pages.core.api.PageManager;
import pl.ds.websight.pages.core.api.PageManager.CreateOptions;

@Slf4j
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageOutline {

  private static final String NN_OUTLINES = "outlines";
  private static final String RT_OUTLINE_PAGE_TEMPLATE_TYPE =
      "/libs/kyanite/outline/templates/outline";

  private final Resource sourcePageResource;

  private final String outlinesRoot;

  private final String outlineTypeName;

  private final String outlinePageName;

  public static Optional<PageOutline> from(Resource pageResource, String outlineTypeName) {
    String pageSpace = findPageSpace(pageResource.getPath());
    if (pageSpace == null) {
      return Optional.empty();
    }

    String outlinesRoot = prepareOutlinesRoot(pageSpace);
    String outlinePageName = prepareOutlinePageName(pageResource.getPath(), pageSpace);

    return Optional.of(
        new PageOutline(pageResource, outlinesRoot, outlineTypeName, outlinePageName));
  }

  public String getOutlinePagePath() {
    return getOutlineParentPath() + outlinePageName;
  }

  private String getOutlineParentPath() {
    return outlinesRoot + outlineTypeName + "/";
  }

  public Resource getOutlineResource() {
    return sourcePageResource.getResourceResolver().getResource(getOutlinePagePath());
  }

  public PageOutline delete() {
    Resource outlineResource = getOutlineResource();
    if (outlineResource != null) {
      try {
        outlineResource.getResourceResolver().delete(outlineResource);
      } catch (PersistenceException e) {
        log.error(e.getMessage());
        throw new PageOutlineActionException(
            "Cannot remove previous outline page: " + outlineResource.getPath());
      }
    }

    return this;
  }

  public PageOutline createPage(String outlineType) {
    PageManager pageManager = sourcePageResource.getResourceResolver().adaptTo(PageManager.class);
    if (pageManager == null) {
      throw new PageOutlineActionException("Cannot create outline page. Page Manager unavailable.");
    }
    try {
      if (!existsPageOutlines()) {
        createOutlineRootPage(findPageSpace(sourcePageResource.getPath()), NN_OUTLINES);
      }

      if (!existsPageOutlineType()) {
        createOutlineRootPage(outlinesRoot, outlineTypeName);
      }

      pageManager.create(getOutlineParentPath(), outlinePageName,
          CreateOptions.createOptions().resolveConflict(false)
              .templatePath(RT_OUTLINE_PAGE_TEMPLATE_TYPE)
              .property("outlineType", outlineType)
              .property("referencePage", sourcePageResource.getPath()).build());
    } catch (PageException e) {
      log.error(e.getMessage());
      throw new PageOutlineActionException("Cannot create outline page.");
    }

    return this;
  }

  public void commit() {
    try {
      sourcePageResource.getResourceResolver().commit();
    } catch (PersistenceException e) {
      log.error(e.getMessage());
      throw new PageOutlineActionException("Cannot create outline page.");
    }
  }

  private boolean existsPageOutlines() {
    return sourcePageResource.getResourceResolver().getResource(outlinesRoot) != null;
  }

  private boolean existsPageOutlineType() {
    return sourcePageResource.getResourceResolver().getResource(getOutlineParentPath()) != null;
  }

  private void createOutlineRootPage(String rootPath, String pageName) {
    PageManager pageManager = sourcePageResource.getResourceResolver().adaptTo(PageManager.class);
    if (pageManager == null) {
      throw new PageOutlineActionException("Cannot create outline page. Page Manager unavailable.");
    }
    try {
      pageManager.create(rootPath, pageName, CreateOptions.createOptions().resolveConflict(false)
          .templatePath(RT_OUTLINE_PAGE_TEMPLATE_TYPE).build());
      ;
    } catch (PageException e) {
      log.error(e.getMessage());
      throw new PageOutlineActionException("Cannot create outline page.");
    }
  }

  private static String prepareOutlinesRoot(String pageSpacePath) {
    return pageSpacePath + NN_OUTLINES + "/";
  }

  private static String prepareOutlinePageName(String resourcePath, String pageSpacePath) {
    return resourcePath.substring(pageSpacePath.length()).replaceAll("/", "-");
  }

  private static String findPageSpace(String resourcePath) {
    String regex = "^(/content/[^/]*/pages/).*";
    if (StringUtils.isNotBlank(resourcePath)) {
      final Pattern pattern = Pattern.compile(regex);
      final Matcher matcher = pattern.matcher(resourcePath);

      if (matcher.find()) {
        return matcher.group(1);
      }
    }
    return null;
  }

}
