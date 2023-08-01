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

package pl.ds.kyanite.common.components.services.impl;

import java.util.ArrayList;
import java.util.List;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.metatype.annotations.Designate;
import pl.ds.kyanite.common.components.configurations.IconLibraryFactoryConfigiguration;
import pl.ds.kyanite.common.components.services.LibraryIconConfig;
import pl.ds.kyanite.common.components.services.LibraryIconFactoryConfig;


@Component(service = LibraryIconFactoryConfig.class,
    configurationPolicy = ConfigurationPolicy.REQUIRE)
public class LibraryIconFactoryConfigImpl implements LibraryIconFactoryConfig {

  private List<LibraryIconConfig> configsList;

  @Reference(service = LibraryIconConfig.class,
      cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC,
      bind = "bind", unbind = "unbind")
  public synchronized void bind(final LibraryIconConfig config) {
    if (configsList == null) {
      configsList = new ArrayList<>();
    }
    configsList.add(config);

  }

  public synchronized void unbind(final LibraryIconConfig config) {
    configsList.remove(config);
  }

  @Override
  public List<LibraryIconConfig> getAllConfigs() {
    return configsList;
  }

  @Override
  public LibraryIconConfig get(String id) {
    for (LibraryIconConfig confFact : configsList) {
      if (id.equals(confFact.getId())) {
        return confFact;
      }
    }
    return null;
  }
}