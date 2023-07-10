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

package pl.ds.kyanite.components.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.junit.jupiter.api.Test;

class IconLibraryDropDownServletTest {

  public static final String WCM_DIALOGS_COMPONENTS_SELECTITEM = "wcm/dialogs/components/selectitem";
  public static final String MDP = "mdp";
  public static final String MATERIAL_DESIGN = "Material Design";
  public final SlingContext context = new SlingContext();

  @Test
  void doGet() {
    LibraryIconFactoryConfig config = mock(LibraryIconFactoryConfig.class);
    IconLibraryDropDownServlet iconLibraryDropDownServlet = new IconLibraryDropDownServlet(
        config);
    when(config.getAllConfigs()).thenReturn(List.of(config));
    when(config.getId()).thenReturn(MDP);
    when(config.getLabel()).thenReturn(MATERIAL_DESIGN);
    SlingHttpServletRequest request = context.request();
    SlingHttpServletResponse response = context.response();
    iconLibraryDropDownServlet.doGet(request, response);
    DataSource dataSource = (DataSource) request.getAttribute(DataSource.class.getName());
    List<Resource> selectItems = new ArrayList<>();
    dataSource.iterator().forEachRemaining(selectItems::add);
    assertFalse(selectItems.isEmpty());
    ValueMap valueMap = selectItems.get(0).getValueMap();
    assertEquals(MATERIAL_DESIGN, valueMap.get("label"));
    assertEquals(MDP, valueMap.get("value"));
    assertEquals(WCM_DIALOGS_COMPONENTS_SELECTITEM, valueMap.get("sling:resourceType"));
  }

}