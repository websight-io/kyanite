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

package pl.ds.kyanite.common.components.models.table.dto;

import java.util.List;
import lombok.Getter;

@Getter
public final class TableRowData {
  private final List<TableCellData> tableCellData;
  private final int numberOfCellsWithColumnsUnspecified;
  private final int summaryColspanTakenByCellsWithColumnsSpecified;

  public TableRowData(List<TableCellData> tableCellData) {
    this.tableCellData = List.copyOf(tableCellData);

    numberOfCellsWithColumnsUnspecified = (int) tableCellData.stream()
            .filter(cell -> cell.columns() == null)
            .count();

    summaryColspanTakenByCellsWithColumnsSpecified = tableCellData.stream()
            .filter(cell -> cell.columns() != null)
            .mapToInt(TableCellData::columns)
            .sum();
  }
}
