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
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
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
      Resource selectedRow = addTableRowRestModel.getSelectedRow();
      Resource parent = selectedRow.getParent();
      int selectedRowNumber = addTableRowRestModel.getSelectedRowNumber();

      List<RowInfo> rowsInfo = getRowsInfo(addTableRowRestModel);
      //establish real row
      RowInfo realSelectedRow = realSelectedRow(rowsInfo, addTableRowRestModel, selectedRowNumber);
      //  List<RowCellInfo> cellsInRange = getCellsInRange(rowsInfo, realSelectedRow.getPosition());

      //  Node parentNode = parent.adaptTo(Node.class);
      //   if (parentNode == null) {
      //        throw new RepositoryException("Failed to adapt parent resource to node");
      //      }

      //      String newRowName = ResourceUtil.createUniqueChildName(parent, ROW_IDENTIFIER);
      //      Node newRow = addRow(parentNode, newRowName, realSelectedRow.getResource()
      //      .getResourceType());

      Node newRow = addEmptyRow(addTableRowRestModel, realSelectedRow);
      List<RowCellInfo> cellsInRange = getCellsInRange(rowsInfo, realSelectedRow.getPosition());

      addCellsToEmptyRow(cellsInRange, realSelectedRow, newRow,
          addTableRowRestModel.isInsertBefore());
      //
      //      addCellsToRow(selectedRow, newRow);
      //
      //      orderRow(parentNode, newRow, selectedRow, insertBefore);

      expandRowspanInCells(cellsInRange, realSelectedRow.getPosition(),
          addTableRowRestModel.isInsertBefore());

      addTableRowRestModel.getResourceResolver().commit();
      return RestActionResult.success("Table row created",
          "New table row created at " + newRow.getPath(), newRow.getPath());
    } catch (RepositoryException | PersistenceException e) {
      LOG.error("Failed to create row", e);
      return RestActionResult.failure("Cannot create row",
          "An error occurred during adding the row");
    }
  }

  private void expandRowspanInCells(List<RowCellInfo> cellsInRange, int rowPosition,
      boolean insertBefore) {
    List<RowCellInfo> cellsToExpandRowspan = new LinkedList<>();
    if (insertBefore) {
      // cellsToModifyRowspan
      cellsToExpandRowspan = cellsInRange.stream()
          .filter(rowCellInfo -> rowCellInfo.getEndAbsolutePosition() == rowPosition)
          .filter(rowCellInfo -> rowCellInfo.getStartAbsolutePosition() < rowPosition)
          .collect(Collectors.toList());

    } else {
      // cellsToModifyRowspan
      cellsToExpandRowspan = cellsInRange.stream()
          .filter(rowCellInfo -> rowCellInfo.getStartAbsolutePosition() <= rowPosition)
          .filter(rowCellInfo -> rowCellInfo.getEndAbsolutePosition() > rowPosition)
          .collect(Collectors.toList());
    }

    cellsToExpandRowspan.stream().forEach(RowCellInfo::incrementRowspan);
  }

  private void addCellsToEmptyRow(List<RowCellInfo> cellsInRange, RowInfo realSelectedRow,
      Node newRow,
      boolean insertBefore)
      throws RepositoryException, PersistenceException {
    int rowPosition = realSelectedRow.getPosition();
    //    List<RowCellInfo> cellsInRange = getCellsInRange(rowsInfo, rowPosition);

    Iterable<Resource> children = realSelectedRow.getResource().getChildren();
    int size = (int) StreamSupport.stream(children.spliterator(), false).count();

    //    List<RowCellInfo> cellsToExpandRowspan = new LinkedList<>();
    if (!insertBefore) {
      // cellsToModifyRowspan
      //      cellsToExpandRowspan = cellsInRange.stream()
      //          .filter(rowCellInfo -> rowCellInfo.getEndAbsolutePosition() == rowPosition)
      //          .filter(rowCellInfo -> rowCellInfo.getStartAbsolutePosition() < rowPosition)
      //          .collect(Collectors.toList());

      //    } else {
      // cellsToModifyRowspan
      //      cellsToExpandRowspan = cellsInRange.stream()
      //          .filter(rowCellInfo -> rowCellInfo.getStartAbsolutePosition() <= rowPosition)
      //          .filter(rowCellInfo -> rowCellInfo.getEndAbsolutePosition() > rowPosition)
      //          .collect(Collectors.toList());

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

    // number of cells to add

    Resource referenceCell = StreamSupport.stream(children.spliterator(), false)
        .findFirst()
        .orElse(null);

    if (referenceCell != null) {
      //      iter X times fori
      for (int i = 0; i < size; i++) {
        ResourceResolver resourceResolver = referenceCell.getResourceResolver();
        Resource rowResource = resourceResolver.getResource(newRow.getPath());

        String newCellName = ResourceUtil.createUniqueChildName(rowResource, CELL_IDENTIFIER);
        Node newCell = newRow.addNode(newCellName);
        newCell.setProperty(ResourceResolver.PROPERTY_RESOURCE_TYPE,
            referenceCell.getResourceType());
      }

      //      for (Resource cell : realSelectedRow.getResource().getChildren()) {
      //        Node newCell = newRow.addNode(referenceCell.getName());
      //        newCell.setProperty(ResourceResolver.PROPERTY_RESOURCE_TYPE,
      //            referenceCell.getResourceType());
      //      }

    }


  }

  private List<RowCellInfo> getCellsInRange(List<RowInfo> rowsInfo, int selectedRowNumber) {
    List<RowCellInfo> cellsInRange = new LinkedList<>();
    for (RowInfo rowInfo : rowsInfo) {
      // get all cells in range
      cellsInRange.addAll(rowInfo.getCellsInRange(selectedRowNumber));
    }
    return cellsInRange;
  }

  @NotNull
  private List<RowInfo> getRowsInfo(AddTableRowRestModel addTableRowRestModel)
      throws RepositoryException {
    List<RowInfo> rowsInfo = new LinkedList<>();

    int rowCounter = 0;
    for (Resource row : addTableRowRestModel.getRows()) {
      rowCounter++;
      RowInfo rowInfo = new RowInfo(row, rowCounter);
      rowsInfo.add(rowInfo);

    }
    return rowsInfo;
  }

  private Node addEmptyRow(AddTableRowRestModel addTableRowRestModel, RowInfo realRow)
      throws RepositoryException, PersistenceException {

    Resource rowResource = realRow.getResource();
    Node parentNode = rowResource.getParent().adaptTo(Node.class);
    if (parentNode == null) {
      throw new RepositoryException("Failed to adapt parent resource to node");
    }

    //    String newRowName = ResourceUtil.createUniqueChildName(rowResource, ROW_IDENTIFIER);
    //    ???? raczej rowResource.getParent() ??
    String newRowName = ResourceUtil.createUniqueChildName(rowResource.getParent(), ROW_IDENTIFIER);

    Node row = parentNode.addNode(newRowName);
    row.setProperty(ResourceResolver.PROPERTY_RESOURCE_TYPE, rowResource.getResourceType());
    return row;

  }

  private RowInfo realSelectedRow(List<RowInfo> rowsInfo, AddTableRowRestModel addTableRowRestModel,
      int selectedRowNumber) {

    if (!addTableRowRestModel.isInsertBefore() && addTableRowRestModel.getSelectedCell()
        .isPresent()) {
      Resource selectedCell = addTableRowRestModel.getSelectedCell().get();
      RowCellInfo selectedCellInfo = new RowCellInfo(selectedCell, selectedRowNumber);
      return rowsInfo.get(selectedCellInfo.getEndAbsolutePosition() - 1);
    }

    return rowsInfo.get(selectedRowNumber - 1);
  }

  private Node addRow(Node parent, String rowName, String resourceType) throws RepositoryException {
    Node row = parent.addNode(rowName);
    row.setProperty(ResourceResolver.PROPERTY_RESOURCE_TYPE, resourceType);
    return row;
  }

  private void addCellsToRow(Resource selectedRow, Node newRow) throws RepositoryException {
    for (Resource cell : selectedRow.getChildren()) {
      Node newCell = newRow.addNode(cell.getName());
      newCell.setProperty(ResourceResolver.PROPERTY_RESOURCE_TYPE, cell.getResourceType());
    }
  }

  private void orderRow(Node parent, Node newRow, Resource selectedRow, boolean isInsertBefore)
      throws RepositoryException {
    if (isInsertBefore) {
      parent.orderBefore(newRow.getName(),
          selectedRow.getName());
    } else {
      parent.orderBefore(newRow.getName(), nextSiblingName(selectedRow));
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
