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

package pl.ds.kyanite.common.components.models;

import java.util.List;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.kyanite.common.components.models.background.ComponentWithBackground;
import pl.ds.kyanite.common.components.models.background.DefaultComponentWithBackground;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SectionComponent implements ComponentWithBackground {

  @SlingObject
  private Resource resource;

  @Inject
  @Getter
  @Default(values = StringUtils.EMPTY)
  private String size;

  @Inject
  @Getter
  private String id;

  @Getter
  List<Resource> children;

  @Self
  @Delegate
  private DefaultComponentWithBackground componentWithBackground;

  @PostConstruct
  void init() {
    children = findValidItems();
  }

  private List<Resource> findValidItems() {
    return StreamSupport.stream(resource.getChildren().spliterator(), false)
        //Image is defined on dialog level and should not be displayed in as separate component
        .filter(res -> !StringUtils.equals("nt:unstructured", res.getResourceType()))
        .toList();
  }
}
