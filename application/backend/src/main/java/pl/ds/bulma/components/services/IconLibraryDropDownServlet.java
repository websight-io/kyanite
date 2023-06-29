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

package pl.ds.bulma.components.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.Servlet;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.ds.bulma.components.helpers.ValueMapResource;

@Component(
    service = Servlet.class,
    property = {
        "sling.servlet.resourceTypes=" + "/apps/iconLibraryDatasource"
    })
public class IconLibraryDropDownServlet extends SlingSafeMethodsServlet {

  private static final Logger LOGGER = LoggerFactory.getLogger(IconLibraryDropDownServlet.class);

  private final transient LibraryIconFactoryConfig config;

  @Activate
  public IconLibraryDropDownServlet(@Reference LibraryIconFactoryConfig config) {
    this.config = config;
  }

  @Override
  protected void doGet(@NotNull SlingHttpServletRequest request,
      @NotNull SlingHttpServletResponse response) {
    try {
      List<LibraryIconFactoryConfig> allConfigs = config.getAllConfigs();
      ResourceResolver resourceResolver = request.getResourceResolver();
      List<KeyValue> dropDownList = new ArrayList<>();
      allConfigs.forEach(elem -> dropDownList.add(new KeyValue(elem.getLabel(), elem.getId())));
      DataSource ds =
          new SimpleDataSource(
              new TransformIterator<>(
                  dropDownList.iterator(),
                  input -> {
                    ValueMap vm = new ValueMapDecorator(new HashMap<>());
                    vm.put("label", input.key);
                    vm.put("value", input.value);
                    vm.put("sling:resourceType", "wcm/dialogs/components/selectitem");
                    return new ValueMapResource(
                        resourceResolver, new ResourceMetadata(),
                        "nt:unstructured", vm);
                  }));
      request.setAttribute(DataSource.class.getName(), ds);
    } catch (Exception e) {
      LOGGER.error("Error in get Icon library drop down values", e);
    }
  }

  private static class KeyValue {

    /**
     * key property.
     */
    private final String key;
    /**
     * value property.
     */
    private final String value;

    /**
     * constructor instance intance.
     *
     * @param newKey   -
     * @param newValue -
     */
    private KeyValue(final String newKey, final String newValue) {
      this.key = newKey;
      this.value = newValue;
    }
  }
}