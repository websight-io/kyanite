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

package pl.ds.kyanite.fragments.components.models;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.kyanite.fragments.components.utils.PagesSpaceUtil;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ExperienceFragment {

  @Inject
  @Getter
  private String resource;

  @Inject
  private String referenceResourceType;

  @Inject
  String referenceResourceName;

  @Getter
  private Boolean validPage = false;

  @Getter
  private String pagePath;

  @Self
  private Resource resourceObject;

  @SlingObject
  private ResourceResolver resourceResolver;

  @PostConstruct
  private void init() {
    resolveReferenceResource();
    validPage = checkIfValidPage();
    pagePath = preparePagePath();
  }

  private boolean checkIfValidPage() {
    Resource pageResource = resourceResolver.getResource(resource);
    if (pageResource != null) {
      ValueMap valueMap = pageResource.getValueMap();
      String primaryType = valueMap.get("jcr:primaryType", String.class);
      return "ws:Page".equals(primaryType);
    }
    return false;
  }

  private String preparePagePath() {
    String path = resource;

    if (resource == null) {
      return null;
    }

    if (resource.endsWith("/")) {
      path = resource.substring(0, resource.length() - 1);
    }

    return path + ".html";
  }

  private void resolveReferenceResource() {
    if (StringUtils.isBlank(resource)) {
      List<String> clauses = new ArrayList<>();
      if (StringUtils.isNotBlank(referenceResourceType)) {
        clauses.add(String.format(" AND c.[sling:resourceType] = '%s'", referenceResourceType));
      }
      if (StringUtils.isNotBlank(referenceResourceName)) {
        clauses.add(String.format(" AND NAME(page) = '%s'", referenceResourceName));
        if (StringUtils.isBlank(referenceResourceType)) {
          clauses.add(
              " AND (     c.[sling:resourceType]      = 'kyanite/fragments/components/page'"
                  + " OR  c.[sling.resourceSuperType] = 'kyanite/fragments/components/page'"
                  + ")");
        }
      }
      if (!clauses.isEmpty()) {
        try {
          Session session = resourceResolver.adaptTo(Session.class);
          QueryManager queryManager = session.getWorkspace().getQueryManager();
          String queryString = String.format(
              " SELECT page.* "
                  + " FROM [ws:Page] AS page"
                  + " INNER JOIN [ws:PageContent] AS c "
                  + "     ON ISCHILDNODE(c, page) "
                  + " WHERE ISDESCENDANTNODE(page, '/content/%s') "
                  + " %s",
              PagesSpaceUtil.getWsPagesSpaceName(resourceObject),
              String.join(" ", clauses));
          Query query = queryManager.createQuery(queryString, Query.JCR_SQL2);
          QueryResult result = query.execute();
          NodeIterator nodeIterator = result.getNodes();
          if (nodeIterator.hasNext()) {
            Node node = nodeIterator.nextNode();
            resource = node.getPath();
            ModifiableValueMap vm = resourceObject.adaptTo(ModifiableValueMap.class);
            vm.put("resource", resource);
            session.save();
          }
        } catch (Exception ignored) {
          // ignore the error, leave the field to manual authoring
        }
      }
    }
  }
}
