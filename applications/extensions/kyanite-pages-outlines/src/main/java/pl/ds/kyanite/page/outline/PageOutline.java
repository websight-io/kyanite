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

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import pl.ds.websight.pages.core.api.PageException;
import pl.ds.websight.pages.core.api.PageManager;
import pl.ds.websight.pages.core.api.PageManager.CreateOptions;

@Slf4j
@AllArgsConstructor
public class PageOutline {

  private static final String RT_OUTLINE_PAGE_TEMPLATE_TYPE =
      "/libs/kyanite/outline/templates/outline";
  private static final String NN_OUTLINES = "outlines";
  public static final String PN_OUTLINE_TYPE = "outlineType";
  public static final String PN_REFERENCE_PAGE = "referencePage";
  public static final String PN_PRIMARY_TYPE = "jcr:primaryType";
  public static final String PV_UNSTRUCTURED_TYPE = "nt:unstructured";

  private final Resource sourcePageResource;

  private final String outlineTypeName;

  private final String outlineResourceType;

  public Resource getOutlineResource() {
    Resource parentResource = getParentResource();
    if (parentResource == null) {
      return null;
    }

    return sourcePageResource.getResourceResolver()
        .getResource(parentResource, outlineTypeName);
  }

  public void delete() {
    if (exists()) {
      deleteOutlineResource();
      commit();
    }
  }

  public void create() {
    if (exists()) {
      deleteOutlineResource();
    }
    createPage();
    commit();
  }

  public boolean exists() {
    return getOutlineResource() != null;
  }

  private Resource getParentResource() {
    return sourcePageResource.getChild(NN_OUTLINES);
  }

  private Resource creatParentResource() throws PersistenceException {
    Map<String, Object> properties = new HashMap<>();
    properties.put(PN_PRIMARY_TYPE, PV_UNSTRUCTURED_TYPE);
    return sourcePageResource.getResourceResolver()
        .create(sourcePageResource, NN_OUTLINES, properties);
  }

  private void createPage() {
    PageManager pageManager = getPageManager();
    try {
      pageManager.create(prepareOutlinesResource().getPath(), outlineTypeName,
          prepareCreateOptions());
    } catch (PageException | PersistenceException e) {
      log.error(e.getMessage());
      throw new PageOutlineActionException("Cannot create outline page.");
    }
  }

  private PageManager getPageManager() {
    PageManager pageManager = sourcePageResource.getResourceResolver().adaptTo(PageManager.class);
    if (pageManager == null) {
      throw new PageOutlineActionException("Cannot create outline page. Page Manager unavailable.");
    }
    return pageManager;
  }

  private CreateOptions prepareCreateOptions() {
    return CreateOptions.createOptions().resolveConflict(false)
        .templatePath(RT_OUTLINE_PAGE_TEMPLATE_TYPE)
        .property(PN_OUTLINE_TYPE, outlineResourceType)
        .property(PN_REFERENCE_PAGE, sourcePageResource.getPath()).build();
  }

  private Resource prepareOutlinesResource() throws PersistenceException {
    Resource outlinesResource = getParentResource();
    if (outlinesResource == null) {
      outlinesResource = creatParentResource();
    }
    return outlinesResource;
  }

  private void commit() {
    try {
      sourcePageResource.getResourceResolver().commit();
    } catch (PersistenceException e) {
      log.error(e.getMessage());
      throw new PageOutlineActionException("Cannot create outline page.");
    }
  }

  private void deleteOutlineResource() {
    try {
      sourcePageResource.getResourceResolver().delete(getOutlineResource());
    } catch (PersistenceException e) {
      log.error(e.getMessage());
      throw new PageOutlineActionException(
          "Cannot remove previous outline page: " + getOutlineResource().getPath());
    }
  }

}
