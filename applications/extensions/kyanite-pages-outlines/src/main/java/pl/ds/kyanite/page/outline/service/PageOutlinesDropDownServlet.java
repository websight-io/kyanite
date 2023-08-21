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

package pl.ds.kyanite.page.outline.service;

import java.util.HashMap;
import java.util.List;
import javax.servlet.Servlet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.iterators.TransformIterator;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import pl.ds.kyanite.common.components.services.DataSource;
import pl.ds.kyanite.common.components.services.SimpleDataSource;
import pl.ds.kyanite.common.components.utils.ValueMapResource;
import pl.ds.kyanite.page.outline.configuration.PageOutlineConfig;
import pl.ds.kyanite.page.outline.configuration.PageOutlineConfigStore;

@Component(
    service = Servlet.class,
    property = {
        "sling.servlet.resourceTypes=" + "/libs/kyanite/outlines/dropDownDatasource"
    })
@Slf4j
public class PageOutlinesDropDownServlet extends SlingSafeMethodsServlet {

  private final transient PageOutlineConfigStore config;

  @Activate
  public PageOutlinesDropDownServlet(@Reference PageOutlineConfigStore config) {
    this.config = config;
  }

  @Override
  protected void doGet(@NotNull SlingHttpServletRequest request,
      @NotNull SlingHttpServletResponse response) {
    try {
      List<PageOutlineConfig> allConfigs = config.getAll();
      ResourceResolver resourceResolver = request.getResourceResolver();
      DataSource ds =
          new SimpleDataSource(
              new TransformIterator<>(
                  allConfigs.iterator(),
                  input -> {
                    ValueMap vm = new ValueMapDecorator(new HashMap<>());
                    vm.put("label", input.getLabel());
                    vm.put("value", input.getId());
                    vm.put("sling:resourceType", "wcm/dialogs/components/selectitem");
                    return new ValueMapResource(
                        resourceResolver, new ResourceMetadata(),
                        "nt:unstructured", vm);
                  }));
      request.setAttribute(DataSource.class.getName(), ds);
    } catch (Exception e) {
      log.error("Error in get Icon library drop down values", e);
    }
  }
}