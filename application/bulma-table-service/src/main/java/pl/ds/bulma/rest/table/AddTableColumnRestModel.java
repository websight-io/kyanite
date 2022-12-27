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

import javax.jcr.Session;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.websight.request.parameters.support.annotations.RequestParameter;

@Model(adaptables = SlingHttpServletRequest.class)
public class AddTableColumnRestModel {

  @SlingObject
  private Resource resource;

  @SlingObject
  private ResourceResolver resourceResolver;

  @RequestParameter
  private boolean insertBefore;

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
