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

import java.util.Iterator;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import pl.ds.websight.rest.framework.RestAction;
import pl.ds.websight.rest.framework.RestActionResult;
import pl.ds.websight.rest.framework.annotations.PrimaryTypes;
import pl.ds.websight.rest.framework.annotations.SlingAction;

@Component
@SlingAction
@PrimaryTypes("nt:base")
public class AddTableColumnRestAction implements RestAction<AddTableColumnRestModel, String> {

  private static final String CELL_IDENTIFIER = "tablecell%d";

  @Override
  public RestActionResult<String> perform(AddTableColumnRestModel addTableColumnRestModel) {
    try {
      Resource selectedCell = addTableColumnRestModel.getResource();
      int pos = getSelectedCellPosition(selectedCell);

      for (Resource row : addTableColumnRestModel.getRows()) {
        addCellToRowAtPosition(row, selectedCell, pos, addTableColumnRestModel.isInsertBefore());
      }

      addTableColumnRestModel.getSession().save();
      return RestActionResult.success("Created column");
    } catch (RepositoryException e) {
      return RestActionResult.failure("Cannot create column", e.getMessage());
    }
  }

  private void addCellToRowAtPosition(Resource row, Resource selectedCell, int position,
      boolean isInsertBefore) throws RepositoryException {
    Node rowNode = row.adaptTo(Node.class);
    long numberOfCells = rowNode.getNodes().getSize();

    // create and add new cell to table row
    Node newCell = rowNode.addNode(String.format(CELL_IDENTIFIER, numberOfCells + 1));

    // find cell where the new cell will be moved before or after
    Iterator<Resource> cells = row.listChildren();
    Resource destCell = null;
    for (int i = 0; cells.hasNext() && i < position; i++) {
      destCell = cells.next();
    }

    newCell.setProperty(ResourceResolver.PROPERTY_RESOURCE_TYPE,
        destCell != null ? destCell.getResourceType() : selectedCell.getResourceType());

    if (isInsertBefore) {
      rowNode.orderBefore(newCell.getName(), destCell.getName());
    } else if (cells.hasNext()) {
      rowNode.orderBefore(newCell.getName(), cells.next().getName());
    }

  }

  private int getSelectedCellPosition(Resource selectedCell) {
    int position = 0;
    for (Resource cell : selectedCell.getParent().getChildren()) {
      position++;
      if (selectedCell.getName().equals(cell.getName())) {
        return position;
      }
    }
    return position;
  }
}
