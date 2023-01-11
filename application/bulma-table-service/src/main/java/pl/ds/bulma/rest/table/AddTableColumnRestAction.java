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

  private static final Logger LOG = LoggerFactory.getLogger(AddTableColumnRestAction.class);

  private static final String CELL_IDENTIFIER = "tablecell";

  @Override
  public RestActionResult<String> perform(AddTableColumnRestModel addTableColumnRestModel) {
    try {
      Resource selectedCell = addTableColumnRestModel.getResource();
      Node selectedCellNode = selectedCell.adaptTo(Node.class);
      if (selectedCellNode == null) {
        throw new RepositoryException("Failed to adapt the selected cell resource to node");
      }

      long pos = getSelectedCellPosition(selectedCellNode);

      for (Resource row : addTableColumnRestModel.getRows()) {
        addCellToRowAtPosition(row, selectedCell, pos,
            addTableColumnRestModel.isInsertBefore());
      }

      addTableColumnRestModel.getResourceResolver().commit();
      return RestActionResult.success("Created column");
    } catch (RepositoryException | PersistenceException e) {
      LOG.error("Failed to create column", e);
      return RestActionResult.failure("Cannot create column",
          "An error occurred during adding the column");
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

  private void addCellToRowAtPosition(Resource row, Resource selectedCell, long position,
      boolean isInsertBefore) throws RepositoryException, PersistenceException {
    Node rowNode = row.adaptTo(Node.class);
    if (rowNode == null) {
      LOG.error("Failed to adapt the row resource to node");
      return;
    }

    if (!hasCellAtPosition(rowNode, position)) {
      return;
    }

    NodeIterator siblings = rowNode.getNodes();
    siblings.skip(position - 1);
    Node baseCell = siblings.hasNext() ? siblings.nextNode() : null;

    String newCellName = ResourceUtil.createUniqueChildName(row, CELL_IDENTIFIER);
    String newCellResourceType =
        baseCell != null ? baseCell.getProperty(ResourceResolver.PROPERTY_RESOURCE_TYPE).getString()
            : selectedCell.getResourceType();
    addCellToRow(rowNode, newCellName, newCellResourceType);

    orderCell(siblings, rowNode, newCellName, baseCell.getName(), isInsertBefore);
  }

  private boolean hasCellAtPosition(Node row, long position) throws RepositoryException {
    return row.getNodes().getSize() >= position;
  }

  private void addCellToRow(Node row, String name, String resourceType) throws RepositoryException {
    Node cell = row.addNode(name);
    cell.setProperty(ResourceResolver.PROPERTY_RESOURCE_TYPE, resourceType);
  }

  private void orderCell(NodeIterator siblings, Node row, String newCellName, String baseCellName,
      boolean isInsertBefore) throws RepositoryException {
    if (isInsertBefore) {
      row.orderBefore(newCellName, baseCellName);
    } else if (siblings.hasNext()) {
      row.orderBefore(newCellName, siblings.nextNode().getName());
    }
  }
}
