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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = SlingHttpServletRequest.class)
public class DeleteTableColumnRestModel {

  private static final Logger LOG = LoggerFactory.getLogger(DeleteTableRowRestModel.class);
  private static final String TABLE_RESOURCE_TYPE = "bulma/components/table";
  private static final String TABLEROW_RESOURCE_TYPE = "bulma/components/table/tablerow";

  private static final String COLSPAN = "colspan";
  private static final String ROWSPAN = "rowspan";

  private int colDeleteStart;
  private int colDeleteEnd;
  private List<Resource> cellsToDelete;
  private List<Resource> rows;
  private Map<Integer, Map<Integer, Resource>> tableMap;

  @SlingObject
  private Resource selectedCell;

  @SlingObject
  private ResourceResolver resourceResolver;

  @PostConstruct
  private void init() {
    this.rows = findRows();
    initTableMap();
    initDeletePositions();
    initCellsToDelete();
  }

  private void initDeletePositions() {
    Optional<Entry<Integer, Resource>> selectedCellEntry = tableMap.values().stream()
        .flatMap(m -> m.entrySet().stream())
        .filter(
            entry -> entry.getValue() != null && selectedCell.getPath()
                .equals(entry.getValue().getPath()))
        .findFirst();
    this.colDeleteStart = selectedCellEntry.get().getKey();
    this.colDeleteEnd =
        this.colDeleteStart + calculateSpan(selectedCellEntry.get().getValue(), COLSPAN) - 1;
  }

  private void initCellsToDelete() {
    cellsToDelete = new ArrayList<>();
    tableMap.values().stream()
        .flatMap(m -> m.entrySet().stream())
        .filter(
            entry -> entry.getValue() != null && entry.getKey() >= this.colDeleteStart
                && entry.getKey() <= this.colDeleteEnd)
        .map(entry -> entry.getValue())
        .forEach(cellsToDelete::add);
  }

  private void initTableMap() {
    tableMap = new TreeMap<>();

    for (int i = 0; i < rows.size(); i++) {
      Iterator<Resource> cells = rows.get(i).listChildren();
      int rowCount = i + 1;
      tableMap.putIfAbsent(rowCount, new TreeMap<>());
      for (int cellCount = 1; cells.hasNext(); cellCount++) {
        Resource cell = cells.next();
        int rowspan = calculateSpan(cell, ROWSPAN);
        int colspan = calculateSpan(cell, COLSPAN);
        putShadowCells(rowspan, colspan, rowCount, cellCount);

        int pos = 1;
        while (tableMap.get(rowCount).containsKey(pos)) {
          pos++;
        }
        tableMap.get(rowCount).put(pos, cell);
      }
    }
  }

  private void putShadowCells(int rowspan, int colspan, int rowCount, int cellCount) {
    for (int i = 0; i < rowspan; i++) {
      for (int j = 0; j < colspan; j++) {
        if (i == 0 && j == 0) {
          continue;
        }
        int rowPos = rowCount + i;
        tableMap.putIfAbsent(rowPos, new TreeMap<>());
        int colPos = cellCount + j + getShadowCellCount(rowCount, cellCount);
        tableMap.get(rowPos).putIfAbsent(colPos, null);
      }
    }
  }

  private List<Resource> findRows() {
    List<Resource> rows = new ArrayList<>();
    for (Resource child : findTable().getChildren()) {
      if (TABLEROW_RESOURCE_TYPE.equals(child.getResourceType())) {
        rows.add(child);
      } else {
        for (Resource row : child.getChildren()) {
          rows.add(row);
        }
      }
    }
    return rows;
  }


  private Resource findTable() {
    Resource ascendant = selectedCell.getParent();
    while (ascendant != null) {
      if (TABLE_RESOURCE_TYPE.equals(ascendant.getResourceType())) {
        return ascendant;
      }
      ascendant = ascendant.getParent();
    }
    return null;
  }

  private int calculateSpan(Resource cell, String span) {
    Integer spanValue = cell.getValueMap().get(span, Integer.class);
    if (spanValue == null) {
      return 1;
    }
    return spanValue.intValue();
  }

  private int getShadowCellCount(int rowPos, int cellCount) {
    return (int) tableMap.get(rowPos).entrySet().stream()
        .filter(entry -> entry.getKey() <= cellCount && entry.getValue() == null).count();
  }

  public int getColDeleteStart() {
    return colDeleteStart;
  }

  public int getColDeleteEnd() {
    return colDeleteEnd;
  }

  public List<Resource> getRows() {
    return rows;
  }

  public Map<Integer, Map<Integer, Resource>> getTableMap() {
    return tableMap;
  }

  public List<Resource> getCellsToDelete() {
    return cellsToDelete;
  }

  public ResourceResolver getResourceResolver() {
    return resourceResolver;
  }
}
