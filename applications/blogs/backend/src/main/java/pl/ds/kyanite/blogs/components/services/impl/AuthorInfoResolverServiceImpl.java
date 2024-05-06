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

import static pl.ds.kyanite.blogs.components.exceptions.AuthorInfoResolvingExceptionTemplates.AUTHOR_INFO_NOT_RESOLVING;
import static pl.ds.kyanite.blogs.components.exceptions.AuthorInfoResolvingExceptionTemplates.AUTHOR_INFO_SOURCE_IS_NULL;
import static pl.ds.kyanite.blogs.components.exceptions.AuthorInfoResolvingExceptionTemplates.AUTHOR_INFO_SOURCE_NOT_SET;
import static pl.ds.kyanite.blogs.components.exceptions.AuthorInfoResolvingExceptionTemplates.AUTHOR_INFO_SOURCE_PATH_NOT_SET;
import static pl.ds.kyanite.blogs.components.exceptions.AuthorInfoResolvingExceptionTemplates.AUTHOR_INFO_SOURCE_RESOLVING_EXCEPTION;
import static pl.ds.kyanite.blogs.components.exceptions.AuthorInfoResolvingExceptionTemplates.AUTHOR_INFO_SOURCE_TYPE_UNKNOWN;
import static pl.ds.kyanite.blogs.components.exceptions.AuthorInfoResolvingExceptionTemplates.AUTHOR_NODE_MISSING_IN_CONSUMER;
import static pl.ds.kyanite.blogs.components.exceptions.AuthorInfoResolvingExceptionTemplates.AUTHOR_NODE_MISSING_IN_REFERENCE;
import static pl.ds.kyanite.blogs.components.exceptions.AuthorInfoResolvingExceptionTemplates.CIRCULAR_REFERENCE;
import static pl.ds.kyanite.blogs.components.exceptions.AuthorInfoResolvingExceptionTemplates.COUNSUMER_IS_NULL;

import java.util.HashSet;
import java.util.Set;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.factory.ModelFactory;
import org.jetbrains.annotations.Nullable;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import pl.ds.kyanite.blogs.components.exceptions.AuthorInfoCircularReferenceException;
import pl.ds.kyanite.blogs.components.exceptions.AuthorInfoConfigurationException;
import pl.ds.kyanite.blogs.components.exceptions.AuthorInfoResolvingException;
import pl.ds.kyanite.blogs.components.models.AuthorInfo;
import pl.ds.kyanite.blogs.components.models.AuthorInfo.Fields;
import pl.ds.kyanite.blogs.components.models.AuthorInfo.SourceType;
import pl.ds.kyanite.blogs.components.models.AuthorInfoModel;
import pl.ds.kyanite.blogs.components.services.AuthorInfoResolverService;
import pl.ds.kyanite.common.components.utils.PageUtil;


@Component(service = {AuthorInfoResolverService.class})
public class AuthorInfoResolverServiceImpl implements AuthorInfoResolverService {

  private static final String JCR_CONTENT = "jcr:content";

  @Reference
  @Setter
  private ModelFactory modelFactory;

  @Activate
  public void activate(){}

  @Deactivate
  public void deactivate(){}

  @Override
  public AuthorInfoModel retrieveAuthorInfo(
      Resource resource, ResourceResolver resourceResolver) throws AuthorInfoResolvingException {
    return retrieveAuthorInfo(resource, resourceResolver, new HashSet<>());
  }

  private AuthorInfoModel retrieveAuthorInfo(
      Resource resource, ResourceResolver resourceResolver, Set<String> paths) {

    if (resource == null) {
      throw new AuthorInfoConfigurationException(COUNSUMER_IS_NULL);
    }

    Resource authorNode = resolveToAuthorNode(resource);
    if (authorNode == null) {
      throw new AuthorInfoConfigurationException(AUTHOR_NODE_MISSING_IN_CONSUMER);
    }
    String authorNodePath = PageUtil.removeContentSuffix(authorNode.getPath());

    //  check for circular reference
    if (paths.contains(authorNodePath)) {
      throw new AuthorInfoCircularReferenceException(CIRCULAR_REFERENCE);
    }
    paths.add(authorNodePath);

    //  detect the source type
    String sourceType = authorNode.getValueMap().get(Fields.AUTHOR_SOURCE_TYPE, String.class);
    if (StringUtils.isBlank(sourceType)) {
      throw new AuthorInfoConfigurationException(
          String.format(AUTHOR_INFO_SOURCE_NOT_SET, authorNodePath));
    }

    //  retrieve author info or proceed along the reference chain
    switch (sourceType) {
      case SourceType.AUTHOR_PAGE -> {
        String authorPageLink = authorNode.getValueMap().get(Fields.AUTHOR_PAGE_LINK, String.class);
        Resource authorPageResource = resolveAuthorInfoReference(
            authorPageLink, resourceResolver, authorNodePath);
        return retrieveAuthorInfo(authorPageResource, resourceResolver, paths);
      }
      case SourceType.PARENT_PAGE -> {
        String parentPagePath = PageUtil.getParentPagePath(authorNode);
        Resource parentPage = resolveAuthorInfoReference(
            parentPagePath, resourceResolver, authorNodePath);
        return retrieveAuthorInfo(parentPage, resourceResolver, paths);
      }
      case SourceType.OWN_PROPERTIES -> {
        return resolveToModel(authorNode);
      }
      default -> throw new AuthorInfoConfigurationException(
          String.format(AUTHOR_INFO_SOURCE_TYPE_UNKNOWN, authorNodePath, sourceType));
    }
  }

  @Nullable
  private Resource resolveToAuthorNode(Resource resource) {

    //  assume it's an author node itself
    if (AuthorInfo.AUTHOR_NODE_NAME.equals(resource.getName())) {
      return resource;
    }

    //  assume resource is a component and has an author child
    Resource authorNode = resource.getChild(AuthorInfo.AUTHOR_NODE_NAME);
    if (authorNode != null) {
      return authorNode;
    }

    //  assume resource is a page and has an author child
    Resource contentNode = resource.getChild(JCR_CONTENT);
    if (contentNode != null) {
      return resolveToAuthorNode(contentNode);
    }

    return null;
  }

  private AuthorInfoModel resolveToModel(Resource authorNode) {
    AuthorInfoModel model;
    try {
      model = modelFactory.createModel(authorNode, AuthorInfoModel.class);
    } catch (Exception e) {
      throw new AuthorInfoResolvingException(
          String.format(AUTHOR_INFO_NOT_RESOLVING, authorNode.getPath()));
    }
    String authorOwnerPath = authorNode.getParent().getPath();
    if (authorOwnerPath.endsWith(JCR_CONTENT)) {
      authorOwnerPath = authorOwnerPath.replaceFirst("(?s)(.*)/" + JCR_CONTENT, "$1");
    }
    model.setAuthorOwnerPath(authorOwnerPath);
    return model;
  }

  private Resource resolveAuthorInfoReference(
      String authorInfoSourcePath,
      ResourceResolver resourceResolver,
      String consumerPath
  ) {

    if (StringUtils.isBlank(authorInfoSourcePath)) {
      throw new AuthorInfoConfigurationException(
          String.format(AUTHOR_INFO_SOURCE_PATH_NOT_SET, consumerPath));
    }

    Resource authorInfoResource;
    try {
      authorInfoResource = resourceResolver.getResource(authorInfoSourcePath);
    } catch (Exception e) {
      throw new AuthorInfoConfigurationException(
          String.format(AUTHOR_INFO_SOURCE_RESOLVING_EXCEPTION,
              authorInfoSourcePath, consumerPath, e.getMessage()));
    }
    if (authorInfoResource == null) {
      throw new AuthorInfoConfigurationException(
          String.format(AUTHOR_INFO_SOURCE_IS_NULL, authorInfoSourcePath, consumerPath));
    }

    Resource authorNode = PageUtil.getContentNode(authorInfoResource)
        .getChild(AuthorInfo.AUTHOR_NODE_NAME);
    if (authorNode == null) {
      throw new AuthorInfoConfigurationException(
          String.format(AUTHOR_NODE_MISSING_IN_REFERENCE, authorInfoSourcePath, consumerPath));
    }

    return authorNode;
  }
}
