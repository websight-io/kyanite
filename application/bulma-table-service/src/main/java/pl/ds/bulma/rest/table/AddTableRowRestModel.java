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

import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.jcr.Session;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.websight.request.parameters.support.annotations.RequestParameter;

@Model(adaptables = SlingHttpServletRequest.class)
public class AddTableRowRestModel {

  private static final Pattern cellResourceType = Pattern.compile(
      "bulma/components/table/(tablecell|tableheadcell)");

  @SlingObject
  private Resource resource;

  @SlingObject
  private ResourceResolver resourceResolver;

  @RequestParameter
  private boolean insertBefore;

  private Resource selectedRow;

  @PostConstruct
  private void init() {
    selectedRow = findSelectedRow();
  }

  private Resource findSelectedRow() {
    if (cellResourceType.matcher(resource.getResourceType()).matches()) {
      return resource.getParent();
    }
    return resource;
  }

  public Resource getSelectedRow() {
    return selectedRow;
  }

  public Session getSession() {
    return resourceResolver.adaptTo(Session.class);
  }

  public boolean isInsertBefore() {
    return insertBefore;
  }

}
