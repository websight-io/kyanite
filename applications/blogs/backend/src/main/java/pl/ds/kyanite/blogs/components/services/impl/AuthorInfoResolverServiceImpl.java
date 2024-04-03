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

package pl.ds.kyanite.blogs.components.services.impl;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.factory.ModelFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import pl.ds.kyanite.blogs.components.exceptions.AuthorInfoCircularReferenceException;
import pl.ds.kyanite.blogs.components.exceptions.AuthorInfoConfigurationException;
import pl.ds.kyanite.blogs.components.exceptions.AuthorInfoResolvingException;
import pl.ds.kyanite.blogs.components.models.AuthorInfoModel;
import pl.ds.kyanite.blogs.components.services.AuthorInfoResolverService;
import pl.ds.kyanite.blogs.components.utils.ResourceUtil;

@Component(service = {AuthorInfoResolverService.class})
public class AuthorInfoResolverServiceImpl implements AuthorInfoResolverService {

  @Reference
  private ModelFactory modelFactory;

  @Override
  public AuthorInfoModel retrieveAuthorInfo(Resource resource, ResourceResolver resourceResolver) {
    return retrieveAuthorInfo(resource, resourceResolver, new HashSet<>());
  }

  private AuthorInfoModel retrieveAuthorInfo(
      Resource resource, ResourceResolver resourceResolver, Set<String> paths) {

    Resource content = ResourceUtil.getContentNode(resource);
    String componentPath = ResourceUtil.removeContentSuffix(resource.getPath());
    String contentPath = content.getPath();

    //  check for circular reference
    if (paths.contains(componentPath)) {
      throw new AuthorInfoCircularReferenceException(
          "Circular reference detected in author info reference chain");
    }
    paths.add(componentPath);

    //  detect the source type
    String sourceType = content.getValueMap().get("authorInfoSource", String.class);
    if (StringUtils.isBlank(sourceType)) {
      throw new AuthorInfoConfigurationException(
          String.format("Author info source is not set in %s", contentPath));
    }

    //  retrieve author info or proceed along the reference chain
    switch (sourceType) {
      case "authorPage" -> {
        String authorPageLink = content.getValueMap().get("link", String.class);
        Resource authorPageResource = resolveAuthorInfoReference(authorPageLink, resourceResolver);
        return retrieveAuthorInfo(authorPageResource, resourceResolver, paths);
      }
      case "parentPage" -> {
        String parentPagePath = ResourceUtil.getParentPagePath(resource);
        Resource parentPage = resolveAuthorInfoReference(parentPagePath, resourceResolver);
        return retrieveAuthorInfo(parentPage, resourceResolver, paths);
      }
      case "ownProperties" -> {
        AuthorInfoModel model;
        try {
          model = modelFactory.createModel(content, AuthorInfoModel.class);
        } catch (Exception e) {
          throw new AuthorInfoResolvingException(
              String.format("Resource %s doesn't resolves to AuthorInfo", componentPath));
        }
        return model;
      }
      default -> throw new AuthorInfoConfigurationException(
          String.format("Unknown author info source type in %s", componentPath));
    }
  }

  private Resource resolveAuthorInfoReference(
      String authorInfoSourcePath,
      ResourceResolver resourceResolver
  ) {

    if (StringUtils.isBlank(authorInfoSourcePath)) {
      throw new AuthorInfoConfigurationException("Author info reference path is not set");
    }

    Resource authorInfoResource;
    try {
      authorInfoResource = resourceResolver.getResource(authorInfoSourcePath);
    } catch (Exception e) {
      throw new AuthorInfoConfigurationException(
          String.format("Unable to retrieve author info source %s: %s", authorInfoSourcePath,
              e.getMessage()));
    }
    if (authorInfoResource == null) {
      throw new AuthorInfoConfigurationException(
          String.format("%s doesn't resolve to a resource", authorInfoSourcePath));
    }

    return authorInfoResource;
  }
}
