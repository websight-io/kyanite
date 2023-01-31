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

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.jcr.RepositoryException;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;

public class RowInfo {

  private static final String ROWSPAN_CELL_PROPERTY = "rowspan";

  @Getter
  private int position;

  @Getter
  private final String name;

  @Getter
  private final Resource resource;

  private List<RowCellInfo> cells = new LinkedList();

  public RowInfo(Resource rowResource, int position) throws RepositoryException {
    this.resource = rowResource;
    this.name = rowResource.getName();
    this.position = position;

    Iterable<Resource> cellResources = rowResource.getChildren();
    for (Resource cellResource : cellResources) {
      cells.add(new RowCellInfo(cellResource, position));
    }
  }

  public List<RowCellInfo> getCellsWithoutRowspan() {
    return cells.stream().filter(cell -> cell.getRowspan() > 1).collect(Collectors.toList());
  }

  public List<RowCellInfo> getCellsInRange(int selectedRowNumber) {
    return cells.stream().filter(cell -> cell.isInRange(selectedRowNumber))
        .collect(Collectors.toList());
  }
}
