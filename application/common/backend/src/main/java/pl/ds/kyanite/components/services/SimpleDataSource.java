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

import java.util.Iterator;
import org.apache.commons.collections4.iterators.ListIteratorWrapper;
import org.apache.sling.api.resource.Resource;

public class SimpleDataSource implements DataSource {

  private final ListIteratorWrapper<Resource> wrapper;

  public SimpleDataSource(final Iterator<Resource> iterator) {
    this.wrapper = new ListIteratorWrapper<>(iterator);
  }

  @Override
  public Iterator<Resource> iterator() {
    this.wrapper.reset();
    return this.wrapper;
  }
}