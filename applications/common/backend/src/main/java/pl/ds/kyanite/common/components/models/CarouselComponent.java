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

import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

import java.util.List;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class CarouselComponent {

  private static final String RT_CAROUSEL_ITEM = "kyanite/common/components/carousel/carouselitem";
  private static final String PN_ELEMENTS_IN_ROW = "elementsInRow";
  private static final int ROW_SIZE = 12;

  @ChildResource
  private Resource desktop;

  @ChildResource
  private Resource tablet;

  @ChildResource
  private Resource mobile;

  @Getter
  private Integer desktopElementsInRow;

  @Getter
  private Integer tabletElementsInRow;

  @Getter
  private Integer mobileElementsInRow;

  @Getter
  private Integer desktopColumns;

  @Getter
  private Integer tabletColumns;

  @Getter
  private Integer mobileColumns;

  @Getter
  private List<Resource> items;

  @SlingObject
  private Resource resource;

  public Boolean getHasChildren() {
    return items != null && !items.isEmpty();
  }

  @PostConstruct
  void init() {
    desktopElementsInRow = getElementsInRow(desktop, 2);
    tabletElementsInRow = getElementsInRow(tablet, 2);
    mobileElementsInRow = getElementsInRow(mobile, 1);

    desktopColumns = calculateColumnSize(desktopElementsInRow);
    tabletColumns = calculateColumnSize(tabletElementsInRow);
    mobileColumns = calculateColumnSize(mobileElementsInRow);
    items = findCarouseItems();
  }

  private List<Resource> findCarouseItems() {
    return StreamSupport.stream(resource.getChildren().spliterator(), false)
        .filter(this::isCarouselItem)
        .toList();
  }

  private boolean isCarouselItem(Resource resource) {
    return RT_CAROUSEL_ITEM.equals(resource.getResourceType())
        || resource.isResourceType(RT_CAROUSEL_ITEM);
  }

  private Integer calculateColumnSize(Integer elementInRow) {
    return ROW_SIZE / elementInRow;
  }

  private Integer getElementsInRow(Resource resource, Integer defaultValue) {
    if (resource != null) {
      return resource.getValueMap().get(PN_ELEMENTS_IN_ROW, defaultValue);
    }
    return defaultValue;
  }

  public String isControlHidden() {
    if (items.size() <= mobileElementsInRow) {
      return "hidden-from-all";
    } else if (items.size() <= tabletElementsInRow) {
      return "hidden-from-tablet";
    } else if (items.size() <= desktopElementsInRow) {
      return "hidden-from-desktop";
    }
    return "";
  }
}
