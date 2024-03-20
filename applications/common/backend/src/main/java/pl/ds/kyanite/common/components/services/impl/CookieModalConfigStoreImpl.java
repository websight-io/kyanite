/*
 * Copyright (C) 2024 Dynamic Solutions
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
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import pl.ds.kyanite.common.components.services.CookieModalConfigStore;
import pl.ds.kyanite.common.components.services.CookieModalConfiguration;


@Component(service = CookieModalConfigStore.class, immediate = true)
public class CookieModalConfigStoreImpl implements CookieModalConfigStore {

  private List<CookieModalConfiguration> configsList = new ArrayList<>();

  @Reference(service = CookieModalConfiguration.class,
      cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC,
      bind = "bind", unbind = "unbind")
  public synchronized void bind(final CookieModalConfiguration config) {
    if (configsList == null) {
      configsList = new ArrayList<>();
    }
    configsList.add(config);
  }

  public synchronized void unbind(final CookieModalConfiguration config) {
    configsList.remove(config);
  }


  @Override
  public CookieModalConfiguration get(String spaceName) {
    for (CookieModalConfiguration confFact : configsList) {
      if (spaceName.equals(confFact.getSpaceName())) {
        return confFact;
      }
    }
    return null;
  }
}