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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@ExtendWith(SlingContextExtension.class)
public class TableColspansCalculationTest {

  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(TableCellComponent.class, TableHeadCellComponent.class, TableComponent.class);
  }

  private static Stream<Arguments> inputColumnsAndExpectedComputedColspans() {
    return Stream.of(
            Arguments.of(
                    columns(4, 4, 4, null),
                    colspans(4, 4, 4, 1)),
            Arguments.of(
                    columns(4, null, 4, null),
                    colspans(4, 2, 4, 2)),
            Arguments.of(
                    columns(null, null, 4, null),
                    colspans(3, 3, 4, 2)),
            Arguments.of(
                    columns(null, null, 5, null),
                    colspans(3, 2, 5, 2)),
            Arguments.of(
                    columns(null, null, 6, null),
                    colspans(2, 2, 6, 2)),
            Arguments.of(
                    columns(null, null, 7, null),
                    colspans(2, 2, 7, 1)),
            Arguments.of(
                    columns(null, null, 12, null),
                    colspans(1, 1, 12, 1)),
            Arguments.of(
                    columns(null, null, null, null),
                    colspans(3, 3, 3, 3)),
            Arguments.of(
                    columns(3, null, null, null, 2),
                    colspans(3, 3, 2, 2, 2)),
            Arguments.of(
                    columns(null, 12),
                    colspans(1, 12)),
            Arguments.of(
                    columns(null, 15, null, null),
                    colspans(1, 15, 1, 1)),
            Arguments.of(
                    columns(null, 11, null, null),
                    colspans(1, 11, 1, 1)),
            Arguments.of(
                    columns(1, 2, 3, 4),
                    colspans(1, 2, 3, 4)),
            Arguments.of(
                    columns(20),
                    colspans(20))
    );
  }

  private static Stream<List<Integer>> inputColumns() {
    return inputColumnsAndExpectedComputedColspans()
            .map(args -> args.get()[0])
            .map(arg -> (List<Integer>) arg);
  }

  private static List<Integer> columns(Integer... columns) {
    return Arrays.asList(columns);
  }

  private static List<Integer> colspans(int... colspans) {
    return IntStream.of(colspans).boxed().toList();
  }

  @ParameterizedTest
  @MethodSource("inputColumnsAndExpectedComputedColspans")
  void shouldCalculateColspans(List<Integer> inputColumns, List<Integer> expectedColspans) {
    // validate input
    assertThat(inputColumns).hasSameSizeAs(expectedColspans);
    for (int i = 0; i < inputColumns.size(); i++) {
      Integer columns = inputColumns.get(i);
      if (columns != null) {
        int colspan = expectedColspans.get(i);
        assertThat(columns)
                .describedAs("If columns field was specified for a cell, " +
                        "then result colspan for that cell must never change")
                .isEqualTo(colspan);
      }
    }

    // given
    Resource tableRow = createSingleRowTable(inputColumns, false);

    // when
    List<Integer> actualColspans = retrieveColspansOfCells(tableRow);

    // then
    assertThat(actualColspans).isEqualTo(expectedColspans);
  }

  @ParameterizedTest
  @MethodSource("inputColumns")
  void shouldNotCalculateColspans_IfTableIsScrollable(List<Integer> inputColumns) {
    // given
    Resource tableRow = createSingleRowTable(inputColumns, true);

    // when
    List<Integer> actualColspans = retrieveColspansOfCells(tableRow);

    // then: expect colspan=1 for all cells that didn't have columns field specified
    List<Integer> expectedColspans = inputColumns.stream()
            .map(columns -> columns == null ? 1 : columns)
            .toList();
    assertThat(actualColspans).isEqualTo(expectedColspans);
  }

  /**
   * @return parent table row
   */
  private Resource createSingleRowTable(List<Integer> columnsValues, boolean isScrollable) {
    Resource table = context.create().resource("/content/table", Map.of("isScrollable", isScrollable));
    Resource tableHead = context.create().resource(table, "tablehead");
    Resource tableRow = context.create().resource(tableHead, "tablerow");
    for (Integer columns : columnsValues) {
        createCell(tableRow, columns);
    }
    return tableRow;
  }

  private void createCell(Resource parentRow, Integer columns) {
    String name = "cell_" + RandomStringUtils.randomAlphanumeric(10);
    Map<String, Object> properties = columns == null
            ? Collections.emptyMap()
            : Map.of("columns", columns);
    context.create().resource(parentRow, name, properties);
  }

  private List<Integer> retrieveColspansOfCells(Resource tableRow) {
    return StreamSupport
            .stream(tableRow.getChildren().spliterator(), false)
            .map(cell -> cell.adaptTo(TableCellComponent.class))
            .map(TableCellComponent::getColspan)
            .toList();
  }

}