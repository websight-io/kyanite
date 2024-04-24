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

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import pl.ds.kyanite.common.components.services.SearchConfigStore;
import pl.ds.kyanite.common.components.services.SearchConfiguration;
import pl.ds.kyanite.common.components.services.config.BaseSpaceDependentConfigStore;

@Component(service = SearchConfigStore.class, immediate = true)
public class SearchConfigStoreImpl
    extends     BaseSpaceDependentConfigStore<SearchConfiguration>
    implements  SearchConfigStore {

  @Override
  @Reference(
      service = SearchConfiguration.class,
      cardinality = ReferenceCardinality.MULTIPLE,
      policy = ReferencePolicy.DYNAMIC,
      bind = "bind", unbind = "unbind")
  public synchronized void bind(final SearchConfiguration config) {
    super.bind(config);
  }

  @Override
  public synchronized void unbind(final SearchConfiguration config) {
    super.unbind(config);
  }
}
