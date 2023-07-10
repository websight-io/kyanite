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

package pl.ds.kyanite.rest.table;

import java.util.Optional;
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

  private static final String COLSPAN_CELL_PROPERTY = "colspan";

  private static final int ONE_MODIFIER = 1;

  @Override
  public RestActionResult<String> perform(AddTableColumnRestModel addTableColumnRestModel) {
    try {
      Resource selectedCell = addTableColumnRestModel.getResource();
      Node selectedCellNode = selectedCell.adaptTo(Node.class);
      if (selectedCellNode == null) {
        throw new RepositoryException("Failed to adapt the selected cell resource to node");
      }

      CellInfo selectedCellInfo = new CellInfo(selectedCellNode);
      int targetAbsolutePosition = getTargetCellAbsolutePosition(
          addTableColumnRestModel.isInsertBefore(), selectedCellInfo);

      for (Resource row : addTableColumnRestModel.getRows()) {
        addToRow(row, targetAbsolutePosition, selectedCellInfo);
      }

      addTableColumnRestModel.getResourceResolver().commit();
      return RestActionResult.success("Created column");
    } catch (RepositoryException | PersistenceException e) {
      LOG.error("Failed to create column", e);
      return RestActionResult.failure("Cannot create column",
          "An error occurred during adding the column");
    }
  }

  private void addToRow(Resource row, int targetAbsolutePosition, CellInfo selectedCellInfo)
      throws RepositoryException, PersistenceException {
    Node rowNode = row.adaptTo(Node.class);
    if (rowNode == null) {
      LOG.error("Failed to adapt the row resource to node");
      return;
    }

    if (!validPosition(rowNode, targetAbsolutePosition)) {
      return;
    }

    Optional<CellInfo> currentCell = getCellAtAbsolutePosition(
        rowNode, selectedCellInfo.getStartAbsolutePosition());

    String newCellName = ResourceUtil.createUniqueChildName(row, CELL_IDENTIFIER);
    String newCellResourceType = currentCell
        .map(CellInfo::getResourceType)
        .orElse(selectedCellInfo.getResourceType());

    addNewCellToRow(row, targetAbsolutePosition, newCellName, newCellResourceType);
  }

  private void addNewCellToRow(Resource row, int targetAbsolutePosition, String newCellName,
      String resourceType)
      throws RepositoryException {
    Node rowNode = row.adaptTo(Node.class);
    if (rowNode == null) {
      LOG.error("Failed to adapt the row resource to node");
      return;
    }

    Optional<CellInfo> currentCellAtTargetAbsolutePosition = getCellAtAbsolutePosition(
        rowNode, targetAbsolutePosition);

    if (currentCellAtTargetAbsolutePosition.isPresent()) {
      CellInfo currentCell = currentCellAtTargetAbsolutePosition.get();
      if (currentCell.getStartAbsolutePosition() == targetAbsolutePosition) {
        addCell(newCellName, resourceType, rowNode);
        rowNode.orderBefore(newCellName, currentCell.getName());
      } else {
        expandCell(rowNode, currentCell);
      }
    } else {
      addCell(newCellName, resourceType, rowNode);
    }
  }

  private void expandCell(Node rowNode, CellInfo currentCell) throws RepositoryException {
    Node cellNode = rowNode.getNode(currentCell.getName());
    int newColspan = getColspan(cellNode) + ONE_MODIFIER;
    cellNode.setProperty(COLSPAN_CELL_PROPERTY, newColspan);
  }

  private void addCell(String newCellName, String resourceType, Node node)
      throws RepositoryException {
    Node cell = node.addNode(newCellName);
    cell.setProperty(ResourceResolver.PROPERTY_RESOURCE_TYPE, resourceType);
  }

  private Optional<CellInfo> getCellAtAbsolutePosition(Node rowNode,
      int targetAbsolutePosition) throws RepositoryException {

    CellInfo currentCell = null;
    NodeIterator nodeIterator = rowNode.getNodes();
    int sum = 0;
    Node nextCell = null;
    while (nodeIterator.hasNext() && sum < targetAbsolutePosition) {
      nextCell = nodeIterator.nextNode();
      sum += getColspan(nextCell);
    }

    if (sum >= targetAbsolutePosition && nextCell != null) {
      currentCell = new CellInfo(nextCell);
    }

    return Optional.ofNullable(currentCell);
  }

  private boolean validPosition(Node rowNode, int targetAbsolutePosition)
      throws RepositoryException {
    int absoluteRowLength = getAbsoluteRowLength(rowNode);
    return targetAbsolutePosition <= absoluteRowLength + ONE_MODIFIER;
  }

  private int getAbsoluteRowLength(Node rowNode) throws RepositoryException {
    NodeIterator nodeIterator = rowNode.getNodes();
    int rowLength = 0;
    while (nodeIterator.hasNext()) {
      Node nextCell = nodeIterator.nextNode();
      rowLength += getColspan(nextCell);
    }

    return rowLength;
  }

  private int getColspan(Node cellNode) throws RepositoryException {
    return cellNode.hasProperty(COLSPAN_CELL_PROPERTY)
        ? Integer.parseInt(cellNode.getProperty(COLSPAN_CELL_PROPERTY).getString()) : ONE_MODIFIER;
  }

  private int getTargetCellAbsolutePosition(boolean insertBefore, CellInfo cellInfo) {
    if (insertBefore) {
      return cellInfo.getStartAbsolutePosition();
    } else {
      return cellInfo.getEndAbsolutePosition() + ONE_MODIFIER;
    }
  }
}
