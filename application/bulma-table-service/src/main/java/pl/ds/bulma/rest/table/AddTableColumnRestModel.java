/*
 * Copyright (C) 2022 Dynamic Solutions
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


package pl.ds.bulma.rest.table;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.jcr.Session;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.websight.request.parameters.support.annotations.RequestParameter;

@Model(adaptables = SlingHttpServletRequest.class)
public class AddTableColumnRestModel {

  private static final String TABLE_RESOURCE_TYPE = "bulma/components/table";
  private static final String TABLEROW_RESOURCE_TYPE = "bulma/components/table/tablerow";

  @SlingObject
  private Resource resource;

  @SlingObject
  private ResourceResolver resourceResolver;

  @RequestParameter
  private boolean insertBefore;

  private Resource table;

  private List<Resource> rows;

  @PostConstruct
  private void init() {
    table = findTable();
    rows = findRows();
  }

  private Resource findTable() {
    Resource ascendant = resource.getParent();
    while (ascendant != null) {
      if (TABLE_RESOURCE_TYPE.equals(ascendant.getResourceType())) {
        return ascendant;
      }
      ascendant = ascendant.getParent();
    }
    return null;
  }

  private List<Resource> findRows() {
    List<Resource> rows = new ArrayList<>();
    for (Resource child : table.getChildren()) {
      if (TABLEROW_RESOURCE_TYPE.equals(child.getResourceType())) {
        rows.add(child);
      } else {
        for (Resource row : child.getChildren()) {
          rows.add(row);
        }
      }
    }
    return rows;
  }

  public List<Resource> getRows() {
    return rows;
  }

  public Session getSession() {
    return resourceResolver.adaptTo(Session.class);
  }

  public Resource getResource() {
    return resource;
  }

  public boolean isInsertBefore() {
    return insertBefore;
  }
}
