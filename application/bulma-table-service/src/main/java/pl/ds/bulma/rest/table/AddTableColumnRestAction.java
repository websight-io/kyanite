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

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.ds.websight.rest.framework.RestAction;
import pl.ds.websight.rest.framework.RestActionResult;
import pl.ds.websight.rest.framework.annotations.PrimaryTypes;
import pl.ds.websight.rest.framework.annotations.SlingAction;

@Component
@SlingAction
@PrimaryTypes("nt:base")
public class AddTableColumnRestAction implements RestAction<AddTableColumnRestModel, String> {

  private static final String CELL_IDENTIFIER = "tablecell";

  @Override
  public RestActionResult<String> perform(AddTableColumnRestModel addTableColumnRestModel) {
    try {
      Resource selectedCell = addTableColumnRestModel.getResource();
      long pos = getSelectedCellPosition(selectedCell.adaptTo(Node.class));

      for (Resource row : addTableColumnRestModel.getRows()) {
        addCellToRowAtPosition(row, selectedCell, pos,
            addTableColumnRestModel.isInsertBefore());
      }

      addTableColumnRestModel.getSession().save();
      return RestActionResult.success("Created column");
    } catch (RepositoryException | PersistenceException e) {
      return RestActionResult.failure("Cannot create column", e.getMessage());
    }
  }

  private void addCellToRowAtPosition(Resource row, Resource selectedCell, long position,
      boolean isInsertBefore) throws RepositoryException, PersistenceException {
    Node rowNode = row.adaptTo(Node.class);
    long numberOfCells = rowNode.getNodes().getSize();

    // Do not add new cell if row does not have one at position
    if (numberOfCells < position) {
      return;
    }

    // find cell where the new cell will be moved before or after
    NodeIterator siblings = rowNode.getNodes();
    siblings.skip(position - 1);
    Node destCell = siblings.hasNext() ? siblings.nextNode() : null;

    if (destCell == null) {
      return;
    }

    // create and add new cell to table row
    Node newCell = rowNode.addNode(ResourceUtil.createUniqueChildName(row, CELL_IDENTIFIER));
    newCell.setProperty(ResourceResolver.PROPERTY_RESOURCE_TYPE,
        destCell != null ? destCell.getProperty(ResourceResolver.PROPERTY_RESOURCE_TYPE).getString()
            : selectedCell.getResourceType());

    if (isInsertBefore) {
      rowNode.orderBefore(newCell.getName(), destCell.getName());
    } else if (siblings.hasNext()) {
      rowNode.orderBefore(newCell.getName(), siblings.nextNode().getName());
    }

  }

  private long getSelectedCellPosition(Node selectedCell) throws RepositoryException {
    NodeIterator siblings = selectedCell.getParent().getNodes();
    while (siblings.hasNext()) {
      if (selectedCell.getName().equals(siblings.nextNode().getName())) {
        return siblings.getPosition();
      }
    }
    throw new RepositoryException("Position of selected cell was not found");
  }
}
