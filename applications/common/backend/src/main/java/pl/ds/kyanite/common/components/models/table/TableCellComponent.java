/*
 * Copyright (C) 2024 Dynamic Solutions
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

package pl.ds.kyanite.common.components.models.table;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import pl.ds.kyanite.common.components.models.table.dto.TableCellData;
import pl.ds.kyanite.common.components.models.table.dto.TableRowData;

@Slf4j
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TableCellComponent {

  private static final String DEFAULT_TEXT = "Content";
  private static final int DEFAULT_COLSPAN = 1;
  private static final int DEFAULT_ROWSPAN = 1;

  @Inject
  @Getter
  private String text;

  @Inject
  private Integer columns;

  @Inject
  private Integer rows;

  @SlingObject
  private Resource currentResource;

  @ValueMapValue
  private String iconVariant;

  @ChildResource(name = "embeddedIcon")
  private Resource iconResource;

  @PostConstruct
  private void init() {
    if (StringUtils.isEmpty(text)) {
      text = DEFAULT_TEXT;
    }
  }

  public int getColspan() {
    return columns != null ? columns : computeColspan();
  }

  public int getRowspan() {
    return rows != null ? rows : DEFAULT_ROWSPAN;
  }

  private int computeColspan() {
    Optional<TableComponent> parentTable = Optional
        .ofNullable(currentResource.getParent()) // table row
        .map(Resource::getParent) // table head or body
        .map(Resource::getParent) // the table
        .map(tableResource -> tableResource.adaptTo(TableComponent.class));

    if (parentTable.isEmpty() || parentTable.get().isScrollable()) {
      return DEFAULT_COLSPAN;
    }

    Resource parentRow = currentResource.getParent();

    List<TableCellData> tableCells = StreamSupport
        .stream(parentRow.getChildren().spliterator(), false)
        .map(cell -> new TableCellData(
            cell.getValueMap().get("columns", Integer.class),
            cell.getPath().equals(currentResource.getPath())))
        .toList();

    TableRowData tableRowData = new TableRowData(tableCells);

    return computeColspan(tableRowData);
  }

  /**
   * Computes colspan for a cell that hasn't got the columns property specified. Attempts to give
   * each such cell the same colspan, if possible. It takes into account colspans from other cells
   * in the same row.
   */
  private static int computeColspan(TableRowData row) {
    int summaryAvailableColspan = TableComponent.DEFAULT_SUMMARY_COLSPAN
        - row.getSummaryColspanTakenByCellsWithColumnsSpecified();

    if (summaryAvailableColspan < 1) {
      return 1;
    }

    int colspanAvailableForSingleCell = summaryAvailableColspan
        / row.getNumberOfCellsWithColumnsUnspecified();
    if (colspanAvailableForSingleCell == 0) {
      colspanAvailableForSingleCell = 1;
    }

    if (summaryAvailableColspan
        % row.getNumberOfCellsWithColumnsUnspecified() == 0) {
      // all cells are able to receive the same colspan
      return colspanAvailableForSingleCell;
    }

    // ... otherwise - we have some additional colspan space to distribute between cells
    int colspanLeftToDistribute = summaryAvailableColspan
        - row.getNumberOfCellsWithColumnsUnspecified() * colspanAvailableForSingleCell;

    int indexOfCurrentCellInCellsWithColumnsUnspecified =
        getIndexOfCurrentCellInCellsWithColumnsUnspecified(row.getTableCellData());

    if (indexOfCurrentCellInCellsWithColumnsUnspecified < colspanLeftToDistribute) {
      // when giving additional +1 to colspan, prefer cells from the left
      return colspanAvailableForSingleCell + 1;
    } else {
      return colspanAvailableForSingleCell;
    }
  }

  private static int getIndexOfCurrentCellInCellsWithColumnsUnspecified(
      List<TableCellData> tableCells) {
    int resultIndex = 0;
    for (TableCellData tableCell : tableCells) {
      if (tableCell.isCurrentCell()) {
        return resultIndex;
      }
      if (tableCell.columns() == null) {
        resultIndex++;
      }
    }

    throw new IllegalStateException("Expected to always find the current cell in cells list");
  }

  public Optional<Resource> getLeftIcon() {
    return getIcon(IconLayout.ON_THE_LEFT);
  }

  public Optional<Resource> getRightIcon() {
    return getIcon(IconLayout.ON_THE_RIGHT);
  }

  private Optional<Resource> getIcon(final IconLayout iconLayout) {
    return Optional.ofNullable(iconResource)
        .filter(resource -> !ResourceUtil.isNonExistingResource(resource))
        .filter(resource -> StringUtils.equalsIgnoreCase(iconLayout.variantName, iconVariant));
  }

  private enum IconLayout {
    ON_THE_LEFT("icon-on-the-left"),
    ON_THE_RIGHT("icon-on-the-right");

    private final String variantName;

    IconLayout(final String variantName) {
      this.variantName = variantName;
    }
  }
}
