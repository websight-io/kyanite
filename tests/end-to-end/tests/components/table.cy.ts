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

import {selectors} from "../../support/consts";

const paths = {
  table: 'ComponentOverlay_/content/kyanite-e2e-tests/pages/table/jcr:content/pagecontainer/table',
  cell: 'ComponentOverlay_/content/kyanite-e2e-tests/pages/table/jcr:content/pagecontainer/table/tablebody/tablerow2/tablecell2',
  columnAddedCell: 'ComponentOverlay_/content/kyanite-e2e-tests/pages/table/jcr:content/pagecontainer/table/tablebody/tablerow1/tablecell',
  rowAddedCell: 'ComponentOverlay_/content/kyanite-e2e-tests/pages/table/jcr:content/pagecontainer/table/tablebody/tablerow/tablecell1',
};

describe('Table component', function () {
  beforeEach(() => {
    cy.login();
  });

  (
      [
        {
          actionName: 'addTableColumnBefore',
          actionTestId: 'ToolbarItem_AddTableColumnBefore',
          deletionActionName: 'deleteTableColumn',
          deletionActionId: 'ToolbarItem_DeleteTableColumn',
          newComponentPath: paths.columnAddedCell
        },
        {
          actionName: 'addTableColumn',
          actionTestId: 'ToolbarItem_AddTableColumn',
          deletionActionName: 'deleteTableColumn',
          deletionActionId: 'ToolbarItem_DeleteTableColumn',
          newComponentPath: paths.columnAddedCell
        },
        {
          actionName: 'addTableRowBefore',
          actionTestId: 'ToolbarItem_AddTableRowBefore',
          deletionActionName: 'deleteTableRow',
          deletionActionId: 'ToolbarItem_DeleteTableRow',
          newComponentPath: paths.rowAddedCell
        },
        {
          actionName: 'addTableRow',
          actionTestId: 'ToolbarItem_AddTableRow',
          deletionActionName: 'deleteTableRow',
          deletionActionId: 'ToolbarItem_DeleteTableRow',
          newComponentPath: paths.rowAddedCell
        }
      ] as const
  ).forEach(
      ({actionName, actionTestId, deletionActionName, deletionActionId, newComponentPath}) => {
        it(`should execute ${actionName} action with cleanup`, () => {

          cy.visit(
              '/apps/websight/index.html/content/kyanite-e2e-tests/pages/table::editor'
          );
          waitForActionsRegistration(paths.cell, [actionName])

          cy.getByTestId(paths.cell)
          .click()
          .find(selectors.overlayName)
          .should('contain.text', 'Table cell');

          cy.getByTestId(actionTestId)
          .click({force: true});

          cy.getByTestId(newComponentPath)
          .click({force: true})
          .find(selectors.overlayName)
          .should('contain.text', 'Table cell');

          waitForActionsRegistration(newComponentPath, [deletionActionName])

          cy.getByTestId(deletionActionId)
          .click({force: true});

        });
      }
  );

  const waitForActionsRegistration = (
      overlayPath: string,
      actions: string[]
  ) => {
    cy.intercept(
        '/apps/websight-ui-framework-actions/bin/evaluate-conditions.action'
    ).as('evaluateConditions');
    cy.waitUntil(() =>
        cy.getByTestId(overlayPath)
        .click({force: true})
        .then(() =>
            cy
            .wait('@evaluateConditions')
            .then((intercepted) =>
                actions.every((action) =>
                    intercepted.response.body.entity.includes(action)
                )
            )
        )
    );
  };
});
