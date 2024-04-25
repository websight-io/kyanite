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

package pl.ds.kyanite.common.components.services.config;

import java.util.ArrayList;
import java.util.List;

/**
 * A class containing common logic of binding/unbinding/retrieving service configurations by id.
 *
 * @param <T> class extending ServiceConfiguration.
 *           The later must return unique id to be associated with in the config store
 */

public abstract class BaseConfigStore<T extends ServiceConfiguration> implements ConfigStore<T> {

  protected List<T> configsList = new ArrayList<>();

  @Override
  public T get(String id) {
    for (T confFact : configsList) {
      if (id.equals(confFact.getId())) {
        return confFact;
      }
    }
    return null;
  }

  @Override
  public synchronized void bind(T config) {
    if (configsList == null) {
      configsList = new ArrayList<>();
    }
    configsList.add(config);
  }

  @Override
  public synchronized void unbind(final T config) {
    configsList.remove(config);
  }
}
