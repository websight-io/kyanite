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

package pl.ds.kyanite.rte.components.models;

import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RteConfiguration {

  @ChildResource
  private Resource colorsObject;

  @Getter
  private String colors = StringUtils.EMPTY;

  @PostConstruct
  private void init() {
    if (Objects.nonNull(colorsObject)) {
      ValueMap valueMap = colorsObject.getValueMap();
      JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
      for (String key : valueMap.keySet()) {
        jsonObjectBuilder.add(key, valueMap.get(key, String.class));
      }
      JsonObject jsonObject = jsonObjectBuilder.build();
      colors = jsonObject.toString();
    }

  }

}
