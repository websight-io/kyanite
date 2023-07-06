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

package pl.ds.bulma.components.services.impl;

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
import pl.ds.bulma.components.configurations.IconLibraryFactoryConfigiguration;
import pl.ds.bulma.components.services.LibraryIconFactoryConfig;


@Component(service = LibraryIconFactoryConfig.class,
    configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = IconLibraryFactoryConfigiguration.class, factory = true)
public class LibraryIconFactoryConfigImpl implements LibraryIconFactoryConfig {

  private String label;
  private String id;
  private String libraryUrl;
  private String[] attributes;

  private List<LibraryIconFactoryConfig> configsList;

  @Activate
  @Modified
  protected void activate(final IconLibraryFactoryConfigiguration config) {
    this.label = config.label();
    this.id = config.id();
    this.libraryUrl = config.libraryUrl();
    this.attributes = config.attributes();
  }

  @Reference(service = LibraryIconFactoryConfig.class,
      cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC,
      bind = "bind", unbind = "unbind")
  public synchronized void bind(final LibraryIconFactoryConfig config) {
    if (configsList == null) {
      configsList = new ArrayList<>();
    }
    configsList.add(config);

  }

  public synchronized void unbind(final LibraryIconFactoryConfig config) {
    configsList.remove(config);
  }

  public String getLabel() {
    return label;
  }

  public String getId() {
    return id;
  }

  @Override
  public String getLibraryUrl() {
    return libraryUrl;
  }

  public String[] getAttributes() {
    return attributes;
  }

  @Override
  public List<LibraryIconFactoryConfig> getAllConfigs() {
    return configsList;
  }

  @Override
  public LibraryIconFactoryConfig get(String id) {
    for (LibraryIconFactoryConfig confFact : configsList) {
      if (id.equals(confFact.getId())) {
        return confFact;
      }
    }
    return null;
  }
}