/*
 * Copyright (C) 2025 Dynamic Solutions
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

import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;
import static pl.ds.kyanite.common.components.utils.SlingUtils.SLING_RESOURCE_TYPE;

import java.util.List;
import java.util.stream.StreamSupport;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class AccordionComponent {

  public static final String ACCORDION_ITEM_RESOURCE_TYPE
          = "kyanite/common/components/accordion/accordionitem";

  @SlingObject
  private Resource resource;

  @ChildResource
  @Getter
  private List<AccordionItemComponent> elements;

  public List<Resource> getChildrenComponents() {
    return StreamSupport
            .stream(resource.getChildren()
                    .spliterator(), false)
            .filter(it ->
                    ACCORDION_ITEM_RESOURCE_TYPE.equals(it.getValueMap()
                            .get(SLING_RESOURCE_TYPE)))
            .toList();
  }

}
