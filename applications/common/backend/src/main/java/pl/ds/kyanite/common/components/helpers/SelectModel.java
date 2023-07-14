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

package pl.ds.kyanite.common.components.helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestDispatcherOptions;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import pl.ds.kyanite.common.components.services.DataSource;

@Model(adaptables = SlingHttpServletRequest.class)
public class SelectModel {

  @ScriptVariable
  private SlingHttpServletRequest request;

  @ScriptVariable
  private SlingHttpServletResponse response;

  private DataSource dataSource;

  @Getter
  private final List<Resource> selectItems = new ArrayList<>();

  @PostConstruct
  public void init() throws ServletException, IOException {
    Resource resource = request.getResource();
    Resource datasource = resource.getChild("datasource");
    if (Objects.nonNull(datasource)) {
      this.dataSource = this.getDatasource(datasource, DataSource.class);
      if (Objects.nonNull(dataSource)) {
        this.dataSource.iterator().forEachRemaining(selectItems::add);
      }
    }
  }

  public boolean hasDataSource() {
    return Objects.nonNull(dataSource);
  }

  public <T> T getDatasource(Resource resource, Class<T> type)
      throws ServletException, IOException {
    try {
      RequestDispatcher dispatcher = this.request.getRequestDispatcher(resource,
          new RequestDispatcherOptions(resource.getResourceType()));
      if (dispatcher != null) {
        dispatcher.include(this.request, this.response);
        return (T) this.request.getAttribute(type.getName());
      }
      return null;
    } finally {
      this.request.removeAttribute(type.getName());
    }
  }
}
