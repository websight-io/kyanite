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

package pl.ds.kyanite.streamx.resource.impl.selector;

import dev.streamx.sling.connector.PublicationAction;
import dev.streamx.sling.connector.RelatedResource;
import dev.streamx.sling.connector.RelatedResourcesSelector;
import dev.streamx.sling.connector.StreamxPublicationException;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.jetbrains.annotations.Nullable;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.ds.kyanite.common.components.utils.PageSpace;
import pl.ds.kyanite.streamx.resource.ResourceResolverProvider;
import pl.ds.websight.pages.core.api.Page;

@Component(service = RelatedResourcesSelector.class)
@Designate(ocd = HomepageRelatedResourcesSelectorConfig.class)
public class HomepageRelatedResourcesSelector implements RelatedResourcesSelector {

  private static final Logger LOG = LoggerFactory.getLogger(HomepageRelatedResourcesSelector.class);
  private static final String PAGES_PATH_REGEXP = "^/published/[^/]+/pages/.*$";
  private static final String HOMEPAGE_TEMPLATE = "kyanite/common/templates/homepage";

  @Reference
  private ResourceResolverProvider resourceResolverProvider;
  private boolean enabled;

  @Activate
  @Modified
  private void activate(HomepageRelatedResourcesSelectorConfig config) {
    this.enabled = config.enabled();
  }

  @Override
  public Collection<RelatedResource> getRelatedResources(String resourcePath,
      PublicationAction publicationAction) throws StreamxPublicationException {
    if (!enabled || !isPage(resourcePath)) {
      return Collections.emptyList();
    }

    try (ResourceResolver resourceResolver = resourceResolverProvider.getResourceResolver()) {
      Resource resource = resourceResolver.getResource(resourcePath);
      if (isHomepage(resource)) {
        return getAllPublishedPages(resource);
      }
    } catch (LoginException e) {
      LOG.error("Cannot get resource resolver.");
      throw new StreamxPublicationException("Cannot get resource resolver.");
    }
    return Collections.emptyList();
  }

  private boolean isPage(String resourcePath) {
    return resourcePath.matches(PAGES_PATH_REGEXP);
  }

  private Collection<RelatedResource> getAllPublishedPages(Resource resource) {
    PageSpace pageSpace = PageSpace.forResource(resource);
    if (pageSpace == null) {
      return Collections.emptyList();
    }
    Resource pageSpaceResource = pageSpace.getPageSpaceResource();
    if (pageSpaceResource == null) {
      return Collections.emptyList();
    }

    return StreamSupport.stream(pageSpace.getPageSpaceResource().getChildren().spliterator(), false)
        .map(rootPageResource -> rootPageResource.adaptTo(Page.class))
        .filter(Objects::nonNull)
        .flatMap(this::streamAllPages)
        .map(page -> new RelatedResource(page.getPath(), PublicationAction.PUBLISH))
        .toList();
  }

  public boolean isHomepage(Resource resource) {
    return Optional.ofNullable(resource)
        .map(this::getPageContent)
        .map(this::getTemplate)
        .map(template -> isHomepageTemplate(template, resource.getResourceResolver()))
        .orElse(false);
  }

  @Nullable
  private Resource getPageContent(Resource resource) {
    return resource.getChild("jcr:content");
  }

  private String getTemplate(Resource resource) {
    ValueMap valueMap = resource.getValueMap();
    return valueMap.get("ws:template", String.class);
  }

  private boolean isHomepageTemplate(String template, ResourceResolver resourceResolver) {
    if (HOMEPAGE_TEMPLATE.equals(template)) {
      return true;
    }
    Resource templateResource = resourceResolver.getResource(template);
    return templateResource != null
        && templateResource.isResourceType(HOMEPAGE_TEMPLATE);
  }

  private Stream<Page> streamAllPages(Page rootPage) {
    return Stream.concat(Stream.of(rootPage), rootPage.streamOfChildrenRecursively());
  }
}
