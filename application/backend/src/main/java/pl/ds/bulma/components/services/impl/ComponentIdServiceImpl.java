/*
 * Copyright (C) 2022 Dynamic Solutions
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

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.ds.bulma.components.services.ComponentIdService;

@Component(service = ComponentIdService.class)
public class ComponentIdServiceImpl implements ComponentIdService {

  private final Logger log = LoggerFactory.getLogger(this.getClass());
  public static final String UNDERSCORE_SEPARATOR = "_";
  public static final String ID_PROPERTY_NAME = "id";

  public String getId(Resource resource, String prefix) {
    String id = StringUtils.EMPTY;
    Session session = resource.getResourceResolver().adaptTo(Session.class);

    try {
      if (session != null) {
        String storedId = resource.getValueMap().get(ID_PROPERTY_NAME, String.class);
        if (storedId != null && !storedId.isBlank()) {
          return storedId;
        }

        id = generateId(prefix);
        resource.adaptTo(ModifiableValueMap.class).put(ID_PROPERTY_NAME, id);
        session.save();
      }

    } catch (RepositoryException e) {
      log.error(e.getMessage(), e);
    }

    return id;
  }

  private String generateId(String prefix) {
    return prefix + UNDERSCORE_SEPARATOR + Long.toHexString(Double.doubleToLongBits(Math.random()));
  }
}
