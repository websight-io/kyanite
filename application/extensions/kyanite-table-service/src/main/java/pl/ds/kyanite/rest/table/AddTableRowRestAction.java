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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.StreamSupport;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.jetbrains.annotations.NotNull;
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
public class AddTableRowRestAction implements RestAction<AddTableRowRestModel, String> {

  private static final Logger LOG = LoggerFactory.getLogger(AddTableRowRestAction.class);

  private static final String ROW_IDENTIFIER = "tablerow";

  private static final String CELL_IDENTIFIER = "tablecell";

  @Override
  public RestActionResult<String> perform(AddTableRowRestModel addTableRowRestModel) {
    try {
      int selectedRowNumber = addTableRowRestModel.getSelectedRowNumber();
      List<RowInfo> rowsInfo = getRowsInfo(addTableRowRestModel);
      RowInfo realSelectedRow = establishRealSelectedRow(rowsInfo, addTableRowRestModel,
          selectedRowNumber);

      Resource parent = realSelectedRow.getResource().getParent();
      Node parentNode = parent.adaptTo(Node.class);
      if (parentNode == null) {
        throw new RepositoryException("Failed to adapt parent resource to node");
      }

      Node newRow = addEmptyRow(realSelectedRow);
      List<RowCellInfo> cellsInRange = getCellsInRange(rowsInfo, realSelectedRow.getPosition());
      boolean insertBefore = addTableRowRestModel.isInsertBefore();

      addCellsToEmptyRow(cellsInRange, realSelectedRow, newRow,
          insertBefore);

      orderRow(parentNode, newRow, realSelectedRow.getResource(),
          insertBefore);

      List<RowCellInfo> cellsToExpandRowspan = findCellsToExpandRowspan(cellsInRange,
          realSelectedRow.getPosition(), insertBefore, rowsInfo);

      expandRowspanInCells(cellsToExpandRowspan);

      addTableRowRestModel.getResourceResolver().commit();
      return RestActionResult.success("Table row created");
    } catch (RepositoryException | PersistenceException e) {
      LOG.error("Failed to create row", e);
      return RestActionResult.failure("Cannot create row",
          "An error occurred during adding the row");
    }
  }

  private void expandRowspanInCells(List<RowCellInfo> cellsToExpandRowspan) {
    cellsToExpandRowspan.stream().forEach(RowCellInfo::incrementRowspan);
  }

  private List<RowCellInfo> findCellsToExpandRowspan(List<RowCellInfo> cellsInRange,
      int rowPosition, boolean insertBefore, List<RowInfo> rowsInfo) {
    List<RowCellInfo> cellsToExpandRowspan;
    if (insertBefore) {
      cellsToExpandRowspan = cellsInRange.stream()
          .filter(rowCellInfo -> rowCellInfo.getEndAbsolutePosition() >= rowPosition)
          .filter(rowCellInfo -> rowCellInfo.getStartAbsolutePosition() < rowPosition)
          .toList();
    } else {
      cellsToExpandRowspan = cellsInRange.stream()
          .filter(rowCellInfo -> rowCellInfo.getStartAbsolutePosition() <= rowPosition)
          .filter(rowCellInfo -> rowCellInfo.getEndAbsolutePosition() > rowPosition)
          .filter(rowCellInfo -> rowCellInfo.getEndAbsolutePosition() <= rowsInfo.size())
          .toList();
    }

    return cellsToExpandRowspan;
  }

  private void addCellsToEmptyRow(List<RowCellInfo> cellsInRange, RowInfo realSelectedRow,
      Node newRow, boolean insertBefore) throws RepositoryException, PersistenceException {

    int rowPosition = realSelectedRow.getPosition();
    Iterable<Resource> children = realSelectedRow.getResource().getChildren();
    int size = (int) StreamSupport.stream(children.spliterator(), false).count();
    if (!insertBefore) {
      int moreCellsToAdd = (int) cellsInRange.stream()
          .filter(rowCellInfo -> rowCellInfo.getStartAbsolutePosition() < rowPosition)
          .filter(rowCellInfo -> rowCellInfo.getEndAbsolutePosition() == rowPosition)
          .count();

      int moreCellsToRemove = (int) cellsInRange.stream()
          .filter(rowCellInfo -> rowCellInfo.getStartAbsolutePosition() == rowPosition)
          .filter(rowCellInfo -> rowCellInfo.getEndAbsolutePosition() > rowPosition)
          .count();

      size += moreCellsToAdd - moreCellsToRemove;
    }

    Resource referenceCell = StreamSupport.stream(children.spliterator(), false)
        .findFirst()
        .orElse(null);

    if (referenceCell != null) {
      for (int i = 0; i < size; i++) {
        ResourceResolver resourceResolver = referenceCell.getResourceResolver();
        Resource rowResource = resourceResolver.getResource(newRow.getPath());
        String newCellName = ResourceUtil.createUniqueChildName(rowResource, CELL_IDENTIFIER);
        Node newCell = newRow.addNode(newCellName);
        newCell.setProperty(ResourceResolver.PROPERTY_RESOURCE_TYPE,
            referenceCell.getResourceType());
      }
    }
  }

  private List<RowCellInfo> getCellsInRange(List<RowInfo> rowsInfo, int selectedRowNumber) {
    List<RowCellInfo> cellsInRange = new LinkedList<>();
    for (RowInfo rowInfo : rowsInfo) {
      cellsInRange.addAll(rowInfo.getCellsInRange(selectedRowNumber));
    }

    return cellsInRange;
  }

  @NotNull
  private List<RowInfo> getRowsInfo(AddTableRowRestModel addTableRowRestModel) {
    List<RowInfo> rowsInfo = new LinkedList<>();
    int rowCounter = 0;
    for (Resource row : addTableRowRestModel.getRows()) {
      rowCounter++;
      RowInfo rowInfo = new RowInfo(row, rowCounter);
      rowsInfo.add(rowInfo);
    }

    return rowsInfo;
  }

  private Node addEmptyRow(RowInfo realRow)
      throws RepositoryException, PersistenceException {

    Resource rowResource = realRow.getResource();
    Node parentNode = rowResource.getParent().adaptTo(Node.class);
    if (parentNode == null) {
      throw new RepositoryException("Failed to adapt parent resource to node");
    }

    String newRowName = ResourceUtil.createUniqueChildName(rowResource.getParent(), ROW_IDENTIFIER);
    Node row = parentNode.addNode(newRowName);
    row.setProperty(ResourceResolver.PROPERTY_RESOURCE_TYPE, rowResource.getResourceType());

    return row;
  }

  private RowInfo establishRealSelectedRow(List<RowInfo> rowsInfo,
      AddTableRowRestModel addTableRowRestModel, int selectedRowNumber) {

    if (!addTableRowRestModel.isInsertBefore() && addTableRowRestModel.getSelectedCell()
        .isPresent()) {
      Resource selectedCell = addTableRowRestModel.getSelectedCell().get();
      RowCellInfo selectedCellInfo = new RowCellInfo(selectedCell, selectedRowNumber);
      if (selectedCellInfo.getEndAbsolutePosition() > rowsInfo.size()) {
        return rowsInfo.get(rowsInfo.size() - 1);
      } else {
        return rowsInfo.get(selectedCellInfo.getEndAbsolutePosition() - 1);
      }
    }

    return rowsInfo.get(selectedRowNumber - 1);
  }

  private void orderRow(Node parent, Node newRow, Resource selectedRow, boolean isInsertBefore)
      throws RepositoryException {
    String targetRowName = isInsertBefore ? selectedRow.getName() : nextSiblingName(selectedRow);
    if (!newRow.getName().equals(targetRowName)) {
      parent.orderBefore(newRow.getName(), targetRowName);
    }
  }

  private String nextSiblingName(Resource resource) {
    if (resource.getParent() == null) {
      return null;
    }
    Iterator<Resource> siblings = resource.getParent().getChildren().iterator();
    while (siblings.hasNext()) {
      if (siblings.next().getName().equals(resource.getName())) {
        return siblings.hasNext() ? siblings.next().getName() : null;
      }
    }
    return null;
  }
}
