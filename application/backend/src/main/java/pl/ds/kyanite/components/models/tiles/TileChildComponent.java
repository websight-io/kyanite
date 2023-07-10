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

package pl.ds.kyanite.components.models.tiles;


import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class TileChildComponent {

  @SlingObject
  private Resource resource;

  @Inject
  @Getter
  @Default(values = "box")
  private String type;

  @Inject
  @Getter
  @Default(values = StringUtils.EMPTY)
  private String variant;

  @Inject
  @Getter
  private String[] classes;

  @PostConstruct
  private void init() {
    List<String> classList = new ArrayList<>();

    if (StringUtils.isNotEmpty(type)) {
      classList.add(type);
    }
    if (StringUtils.isNotEmpty(variant)) {
      classList.add(variant);
    }

    classes = classList.toArray(new String[]{});
  }
}