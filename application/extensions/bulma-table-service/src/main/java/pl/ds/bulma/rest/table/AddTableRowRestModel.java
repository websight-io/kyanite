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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.jetbrains.annotations.NotNull;
import pl.ds.websight.request.parameters.support.annotations.RequestParameter;

@Model(adaptables = SlingHttpServletRequest.class)
public class AddTableRowRestModel {

  private static final String TABLE_RESOURCE_TYPE = "bulma/components/table";

  private static final String TABLEROW_RESOURCE_TYPE = "bulma/components/table/tablerow";

  private static final Pattern cellResourceType = Pattern.compile(
      "bulma/components/table/(tablecell|tableheadcell)");

  @SlingObject
  private Resource resource;

  @SlingObject
  private ResourceResolver resourceResolver;

  @RequestParameter
  private boolean insertBefore;

  private Resource selectedRow;

  private Resource selectedCell;

  private int selectedRowNumber;

  private Resource table;

  private List<Resource> rows;

  @PostConstruct
  private void init() {
    table = findTable();
    selectedRow = findSelectedRow();
    selectedCell = findSelectedCell();
    handleRows(selectedRow);
  }

  private Resource findSelectedRow() {
    if (cellResourceType.matcher(resource.getResourceType()).matches()) {
      return resource.getParent();
    }
    return resource;
  }

  private Resource findSelectedCell() {
    Resource cellResource = null;
    if (cellResourceType.matcher(resource.getResourceType()).matches()) {
      cellResource = resource;
    }
    return cellResource;
  }

  private void handleRows(Resource selectedRow) {
    List<Resource> tableRows = new ArrayList<>();
    int rowsCounter = 0;
    for (Resource child : table.getChildren()) {
      if (TABLEROW_RESOURCE_TYPE.equals(child.getResourceType())) {
        tableRows.add(child);
        rowsCounter++;
        if (selectedRow.getPath().equals(child.getPath())) {
          selectedRowNumber = rowsCounter;
        }
      } else {
        for (Resource row : child.getChildren()) {
          tableRows.add(row);
          rowsCounter++;
          if (selectedRow.getPath().equals(row.getPath())) {
            selectedRowNumber = rowsCounter;
          }
        }
      }
    }
    this.rows = tableRows;
  }

  private Resource findTable() {
    Resource ascendant = resource.getParent();
    while (ascendant != null) {
      if (TABLE_RESOURCE_TYPE.equals(ascendant.getResourceType())) {
        return ascendant;
      }
      ascendant = ascendant.getParent();
    }
    return null;
  }

  public List<Resource> getRows() {
    return rows;
  }

  public @NotNull Resource getSelectedRow() {
    return selectedRow;
  }

  public @NotNull ResourceResolver getResourceResolver() {
    return resourceResolver;
  }

  public boolean isInsertBefore() {
    return insertBefore;
  }

  public int getSelectedRowNumber() {
    return selectedRowNumber;
  }

  public Optional<Resource> getSelectedCell() {
    return Optional.ofNullable(selectedCell);
  }
}
