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
      Resource authorNode, ResourceResolver resourceResolver, Set<String> paths) {

    String authorNodePath = ResourceUtil.removeContentSuffix(authorNode.getPath());

    //  check for circular reference
    if (paths.contains(authorNodePath)) {
      throw new AuthorInfoCircularReferenceException(
          "Circular reference detected in author info reference chain");
    }
    paths.add(authorNodePath);

    //  detect the source type
    String sourceType = authorNode.getValueMap().get("authorInfoSource", String.class);
    if (StringUtils.isBlank(sourceType)) {
      throw new AuthorInfoConfigurationException(
          String.format("Author info source is not set in %s", authorNodePath));
    }

    //  retrieve author info or proceed along the reference chain
    switch (sourceType) {
      case "authorPage" -> {
        String authorPageLink = authorNode.getValueMap().get("authorPageLink", String.class);
        Resource authorPageResource = resolveAuthorInfoReference(
            authorPageLink, resourceResolver, authorNodePath);
        return retrieveAuthorInfo(authorPageResource, resourceResolver, paths);
      }
      case "parentPage" -> {
        String parentPagePath = ResourceUtil.getParentPagePath(authorNode);
        Resource parentPage = resolveAuthorInfoReference(
            parentPagePath, resourceResolver, authorNodePath);
        return retrieveAuthorInfo(parentPage, resourceResolver, paths);
      }
      case "ownProperties" -> {
        return resolveToModel(authorNode);
      }
      default -> throw new AuthorInfoConfigurationException(
          String.format("Unknown author info source type in %s", authorNodePath));
    }
  }

  private AuthorInfoModel resolveToModel(Resource authorNode) {
    AuthorInfoModel model;
    try {
      model = modelFactory.createModel(authorNode, AuthorInfoModel.class);
    } catch (Exception e) {
      throw new AuthorInfoResolvingException(
          String.format("Resource %s doesn't resolve to AuthorInfo", authorNode.getPath()));
    }
    return model;
  }

  private Resource resolveAuthorInfoReference(
      String authorInfoSourcePath,
      ResourceResolver resourceResolver,
      String consumerPath
  ) {

    if (StringUtils.isBlank(authorInfoSourcePath)) {
      throw new AuthorInfoConfigurationException(
          String.format("Author info reference path is not set in %s", consumerPath));
    }

    Resource authorInfoResource;
    try {
      authorInfoResource = resourceResolver.getResource(authorInfoSourcePath);
    } catch (Exception e) {
      throw new AuthorInfoConfigurationException(
          String.format("Unable to retrieve author info source %s for consumer %s: %s",
              authorInfoSourcePath, consumerPath, e.getMessage()));
    }
    if (authorInfoResource == null) {
      throw new AuthorInfoConfigurationException(
          String.format("Unable to resolve author info source %s to resource for consumer %s",
              authorInfoSourcePath, consumerPath));
    }

    Resource authorNode = ResourceUtil.getContentNode(authorInfoResource).getChild("author");
    if (authorNode == null) {
      throw new AuthorInfoConfigurationException(
          String.format("Author info reference %s doesn't have 'author' node for consumer %s",
              authorInfoSourcePath, consumerPath));
    }

    return authorNode;
  }
}
