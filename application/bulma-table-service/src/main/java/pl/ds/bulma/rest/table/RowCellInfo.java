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

package pl.ds.bulma.rest.table;

import lombok.Getter;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RowCellInfo {

  private static final String ROWSPAN_CELL_PROPERTY = "rowspan";

  private static final Logger LOG = LoggerFactory.getLogger(RowCellInfo.class);

  @Getter
  private final Resource resource;

  @Getter
  private int rowspan;

  @Getter
  private final int rowNumber;

  @Getter
  private final int startAbsolutePosition;

  @Getter
  private final int endAbsolutePosition;

  public RowCellInfo(Resource cellResource, int rowNumber) {
    this.resource = cellResource;
    this.rowNumber = rowNumber;
    this.rowspan = getRowspan(cellResource);
    this.startAbsolutePosition = rowNumber;
    this.endAbsolutePosition = rowNumber + (rowspan - 1);
  }

  private int getRowspan(Resource cellResource) {
    Integer rowspanValue = cellResource.getValueMap().get(ROWSPAN_CELL_PROPERTY, Integer.class);
    if (rowspanValue == null || rowspanValue == 0) {
      return 1;
    }
    return rowspanValue;
  }

  public boolean isInRange(int rowNumber) {
    boolean inRangeAtTheStart = rowNumber >= startAbsolutePosition;
    boolean inRangeAtTheEnd = rowNumber <= endAbsolutePosition;
    return inRangeAtTheStart && inRangeAtTheEnd;
  }


  public void incrementRowspan() {
    this.rowspan = getRowspan(resource) + 1;

    ModifiableValueMap valueMap = resource.adaptTo(ModifiableValueMap.class);
    if (valueMap == null) {
      LOG.error("Failed to adapt cell to ModifiableValueMap");
      return;
    }

    valueMap.replace(ROWSPAN_CELL_PROPERTY, rowspan);
  }
}
