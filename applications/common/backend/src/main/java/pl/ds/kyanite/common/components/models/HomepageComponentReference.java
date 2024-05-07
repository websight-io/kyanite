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

package pl.ds.kyanite.common.components.models;

import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.kyanite.common.components.utils.PageUtil;
import pl.ds.kyanite.fragments.api.models.ExperienceFragment;
import pl.ds.websight.pages.core.api.Page;
import pl.ds.websight.pages.core.api.PageManager;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public abstract class HomepageComponentReference implements ExperienceFragment {

  @SlingObject
  private Resource resource;

  @Inject
  private String referencePath;

  private String referenceTargetPath;

  protected Resource homepage;

  @PostConstruct
  private void initReferenceComponent() throws PersistenceException {

    //  resolve reference path
    if (!isReferenceExists() && canBeEdited()) {
      referencePath = resolveReferencePath();
    }
    referenceTargetPath = preparePagePath(referencePath);
  }

  private boolean isReferenceExists() {
    return referencePath != null
        && resource.getResourceResolver().getResource(referencePath) != null;
  }

  private boolean canBeEdited() {
    return !resource.getPath().startsWith("/published");
  }

  private String resolveReferencePath() throws PersistenceException {
    ResourceResolver resourceResolver = resource.getResourceResolver();
    PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
    if (pageManager != null) {
      Page rootPage = PageUtil.findTopLevelParentPageForCurrentPage(
          Objects.requireNonNull(pageManager.getContainingPage(resource.getPath())));
      homepage = PageUtil.findSiblingPageForCurrentPage(rootPage,
          "/libs/kyanite/common/templates/homepage");

      //  find reference
      Resource reference = null;
      if (Objects.nonNull(homepage)) {
        //  by path...
        reference = homepage.getChild(getPath());
        if (reference == null) {
          //   ...or by resourceType
          reference = PageUtil.getDescendants(homepage)
              .stream()
              .filter(res -> getReferencedResourceType().equals(res.getResourceType()))
              .findFirst()
              .orElse(null);
        }
      }

      //  save reference path
      if (reference != null) {
        referencePath = reference.getPath();
        ModifiableValueMap vm = resource.adaptTo(ModifiableValueMap.class);
        if (vm != null) {
          vm.put("referencePath", referencePath);
          resourceResolver.commit();
          return referencePath;
        } else {
          return null;
        }
      }
    }
    return null;
  }

  @Override
  public boolean isValidPage() {
    return StringUtils.isNotBlank(referenceTargetPath);
  }

  @Override
  public String getPagePath() {
    return referenceTargetPath;
  }

  @Override
  public String getResource() {
    return referencePath;
  }

  protected abstract String getPath();

  protected abstract String getReferencedResourceType();
}
