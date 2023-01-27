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
import java.util.List;
import java.util.NoSuchElementException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import org.apache.sling.api.resource.ModifiableValueMap;
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
public class DeleteTableRowRestAction implements RestAction<DeleteTableRowRestModel, String> {

  private static final String ROWSPAN = "rowspan";
  private static final String TABLECELL = "tablecell";

  private static final Logger LOG = LoggerFactory.getLogger(DeleteTableRowRestAction.class);

  @Override
  public RestActionResult<String> perform(DeleteTableRowRestModel deleteTableRowRestModel) {
    try {
      ResourceResolver resourceResolver = deleteTableRowRestModel.getResourceResolver();
      Resource rowParent = deleteTableRowRestModel.getRowParent();
      int rowspanStartPosition = deleteTableRowRestModel.getRowspanStartPosition();
      int rowspanEndPosition = deleteTableRowRestModel.getRowspanEndPosition();
      Resource nextRow = deleteTableRowRestModel.getNextRow();
      List<Resource> rowsToDelete = deleteTableRowRestModel.getRowsToDelete();

      adjustRowspan(resourceResolver, rowParent, nextRow, rowspanStartPosition, rowspanEndPosition);

      deleteRows(resourceResolver, rowsToDelete);

      resourceResolver.commit();
      return RestActionResult.success("Table row(s) deleted");
    } catch (RepositoryException | PersistenceException e) {
      LOG.error("Failed to delete row(s)", e);
      return RestActionResult.failure("Cannot delete row(s)",
          "An error occurred during deleting the row(s)");
    }
  }

  private void adjustRowspan(ResourceResolver resourceResolver, Resource rowParent,
      Resource nextRow, int rowspanStartPosition, int rowspanEndPosition)
      throws PersistenceException, RepositoryException {
    Iterator<Resource> rows = rowParent.listChildren();
    for (int rowCount = 1; rows.hasNext(); rowCount++) {
      Resource row = rows.next();
      Iterator<Resource> cells = row.listChildren();
      for (int cellCount = 1; cells.hasNext(); cellCount++) {
        Resource cell = cells.next();
        int cellRowspan = getRowspan(cell);
        int cellRange = rowCount + cellRowspan - 1;
        if (isShadowCell(rowspanStartPosition, rowCount, cellRange)) {
          adjustShadowCellRowspan(cell, rowspanStartPosition, rowspanEndPosition, rowCount,
              cellRange);
        } else if (isOverflowCell(rowspanStartPosition, rowspanEndPosition, rowCount, cellRange)) {
          adjustOverflowCellRowspan(cell, rowspanEndPosition, cellRange);
          Resource copiedCell = copyCellToNextRow(nextRow, cell, resourceResolver);
          orderCell(nextRow, copiedCell, cellCount);
        }
      }
    }
  }

  private boolean isShadowCell(int rowspanStartPosition, int rowCount, int cellRange) {
    return rowCount < rowspanStartPosition
        && cellRange >= rowspanStartPosition;
  }

  private boolean isOverflowCell(int rowspanStartPosition, int rowspanEndPosition, int rowCount,
      int cellRange) {
    return rowCount >= rowspanStartPosition && rowCount <= rowspanEndPosition
        && cellRange > rowspanEndPosition;
  }

  private void adjustShadowCellRowspan(Resource cell, int rowspanStartPosition,
      int rowspanEndPosition, int rowCount, int cellRange) {
    int adjustedRowspan = rowspanStartPosition - rowCount;
    if (cellRange > rowspanEndPosition) {
      adjustedRowspan += cellRange - rowspanEndPosition;
    }
    setRowspan(cell, adjustedRowspan);
  }

  private void adjustOverflowCellRowspan(Resource cell, int rowspanEndPosition, int cellRange) {
    int adjustedRowspan = cellRange - rowspanEndPosition;
    setRowspan(cell, adjustedRowspan);
  }

  private void deleteRows(ResourceResolver resourceResolver, List<Resource> rowsToDelete)
      throws PersistenceException {
    for (Resource row : rowsToDelete) {
      resourceResolver.delete(row);
    }
  }

  private void setRowspan(Resource cell, int rowspan) {
    ModifiableValueMap valueMap = cell.adaptTo(ModifiableValueMap.class);
    if (valueMap == null) {
      LOG.error("Failed to adapt cell to ModifiableValueMap");
      return;
    }
    valueMap.replace(ROWSPAN, rowspan);
  }

  private Resource copyCellToNextRow(Resource nextRow, Resource cell,
      ResourceResolver resourceResolver) throws PersistenceException {
    return resourceResolver.create(nextRow,
        ResourceUtil.createUniqueChildName(nextRow, TABLECELL), cell.getValueMap());
  }

  private void orderCell(Resource nextRow, Resource cell, int cellPosition)
      throws RepositoryException {
    Node nextRowNode = nextRow.adaptTo(Node.class);
    if (nextRowNode == null) {
      LOG.error("Failed to adapt next row resource to node.");
      return;
    }
    NodeIterator cells = nextRowNode.getNodes();
    try {
      cells.skip(cellPosition);
      String nextCellName = cells.hasNext() ? cells.nextNode().getName() : null;
      nextRowNode.orderBefore(cell.getName(),
          !cell.getName().equals(nextCellName) ? nextCellName : null);
    } catch (NoSuchElementException e) {
      nextRowNode.orderBefore(cell.getName(), null);
    }
  }

  private int getRowspan(Resource resource) {
    Integer rowspan = resource.getValueMap().get(ROWSPAN, Integer.class);
    if (rowspan == null) {
      rowspan = 1;
    }
    return rowspan.intValue();
  }

}
