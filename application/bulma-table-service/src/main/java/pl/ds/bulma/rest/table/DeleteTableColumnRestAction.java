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

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
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
public class DeleteTableColumnRestAction implements RestAction<DeleteTableColumnRestModel, String> {

  private static final Logger LOG = LoggerFactory.getLogger(DeleteTableColumnRestAction.class);

  private static final String COLSPAN = "colspan";

  @Override
  public RestActionResult<String> perform(DeleteTableColumnRestModel deleteTableColumnRestModel) {
    ResourceResolver resourceResolver = deleteTableColumnRestModel.getResourceResolver();
    int colDeleteStart = deleteTableColumnRestModel.getColDeleteStart();
    int colDeleteEnd = deleteTableColumnRestModel.getColDeleteEnd();
    List<Resource> rows = deleteTableColumnRestModel.getRows();
    List<Resource> cellsToDelete = deleteTableColumnRestModel.getCellsToDelete();
    Map<Integer, Map<Integer, Resource>> tableMap = deleteTableColumnRestModel.getTableMap();

    adjustShadowCellsColspan(tableMap, colDeleteStart, colDeleteEnd);

    try {
      adjustOverflowCellsColspan(tableMap, rows, colDeleteStart, colDeleteEnd, cellsToDelete);
      for (Resource cell : cellsToDelete) {
        resourceResolver.delete(cell);
      }

      cleanUp(resourceResolver, rows);
      resourceResolver.commit();
    } catch (RepositoryException | PersistenceException e) {
      LOG.error("Failed to delete column(s)", e);
      return RestActionResult.failure("Cannot delete column(s)",
          "An error occurred during deleting the column(s)");
    }
    return RestActionResult.success("Table Column(s) deleted");
  }

  private void adjustShadowCellsColspan(Map<Integer, Map<Integer, Resource>> tableMap,
      int colDeleteStart, int colDeleteEnd) {
    List<Entry<Integer, Resource>> entries = tableMap.values().stream()
        .flatMap(m -> m.entrySet().stream())
        .filter(entry -> entry.getValue() != null)
        .collect(Collectors.toList());
    for (Entry<Integer, Resource> entry : entries) {
      Resource cell = entry.getValue();
      int colspan = calculateSpan(cell, COLSPAN);
      int colPos = entry.getKey();
      int cellRange = colPos + colspan - 1;
      if (isShadowCell(cellRange, colPos, colDeleteStart)) {
        int adjustedColspan = colDeleteStart - colPos;
        if (cellRange > colDeleteEnd) {
          adjustedColspan += cellRange - colDeleteEnd;
        }
        setColspan(cell, adjustedColspan);
      }
    }
  }

  private void adjustOverflowCellsColspan(Map<Integer, Map<Integer, Resource>> tableMap,
      List<Resource> rows, int colDeleteStart, int colDeleteEnd, List<Resource> cellsToDelete)
      throws RepositoryException {
    for (Entry<Integer, Map<Integer, Resource>> tableEntry : tableMap.entrySet()) {
      int rowPosition = tableEntry.getKey();
      Resource row = rows.get(rowPosition - 1);
      for (Entry<Integer, Resource> rowEntry : tableEntry.getValue().entrySet()) {
        Resource cell = rowEntry.getValue();
        if (cell == null) {
          break;
        }
        int colPos = rowEntry.getKey();
        int colspan = calculateSpan(cell, COLSPAN);
        int cellRange = colPos + colspan - 1;
        if (isOverflowCell(cellRange, colPos, colDeleteStart, colDeleteEnd)) {
          setColspan(cell, cellRange - colDeleteEnd);
          Optional<Entry<Integer, Resource>> nextCell = findNextCell(tableMap, rowPosition,
              colPos);
          moveCellToNextColumn(row, cell.getName(),
              nextCell.isPresent() ? nextCell.get().getValue().getName() : null);
          cellsToDelete.remove(cell);
        }
      }
    }
  }

  private boolean isShadowCell(int cellRange, int colPos, int colDeleteStart) {
    return colPos < colDeleteStart && cellRange >= colDeleteStart;
  }

  private boolean isOverflowCell(int cellRange, int colPos, int colDeleteStart, int colDeleteEnd) {
    return colPos >= colDeleteStart && colPos <= colDeleteEnd && cellRange > colDeleteEnd;
  }

  private void moveCellToNextColumn(Resource row, String cell, String nextCell)
      throws RepositoryException {
    Node rowNode = row.adaptTo(Node.class);
    if (rowNode == null) {
      LOG.error("Failed to adapt row resource to Node");
      return;
    }
    rowNode.orderBefore(cell, nextCell);
  }

  private Optional<Entry<Integer, Resource>> findNextCell(
      Map<Integer, Map<Integer, Resource>> tableMap, int rowPosition, int colPosition) {
    return tableMap.get(rowPosition).entrySet().stream()
        .filter(entry -> entry.getKey() > colPosition && entry.getValue() != null)
        .findFirst();
  }

  private int calculateSpan(Resource cell, String span) {
    Integer spanValue = cell.getValueMap().get(span, Integer.class);
    if (spanValue == null) {
      return 1;
    }
    return spanValue.intValue();
  }

  private void setColspan(Resource cell, int colspan) {
    ModifiableValueMap valueMap = cell.adaptTo(ModifiableValueMap.class);
    if (valueMap == null) {
      LOG.error("Failed to adapt cell to ModifiableValueMap");
      return;
    }
    valueMap.replace(COLSPAN, colspan);
  }

  private void cleanUp(ResourceResolver resourceResolver, List<Resource> rows)
      throws PersistenceException {
    for (Resource row : rows) {
      if (!row.hasChildren()) {
        resourceResolver.delete(row);
      }
      if (row.getParent() != null && !row.getParent().hasChildren()) {
        resourceResolver.delete(row.getParent());
      }
    }
  }
}
