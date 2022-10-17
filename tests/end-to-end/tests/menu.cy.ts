/*
 * <!--
 *     Copyright (C) 2022 Dynamic Solutions
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 * -->
 */

import {selectors, testIds} from '../support/consts';

const paths = {
  menu: 'ComponentOverlay_pagecontainer/menu'
};

describe('Menu component', function () {
  beforeEach(() => {
    cy.login();
  });

  it('renders correctly in preview mode', function () {
    cy.visit('/content/bulma-tests/pages/menu.html');
    cy.percySnapshotPreview('Menu preview');

    cy.contains('Second item').click();
    cy.contains('First label').click();

  });

  it('renders correctly in edit mode', function () {
    cy.intercept(
        'POST',
        '**/pagecontainer/menu.websight-dialogs-service.save-properties.action'
    ).as('saveProperties');

    cy.visit(
        '/apps/websight/index.html/content/bulma-tests/pages/menu::editor'
    );

    cy.getByTestId(paths.menu)
    .click()
    .find(selectors.overlayName)
    .should('have.text', 'Menu');

    cy.percySnapshotPageEditor('Menu editor');

    cy.getByTestId(testIds.editIcon).click();

    cy.getByTestId('Input_Sectionlabel').clear().type('Edited section');
    cy.getByTestId('Input_Itemtext').clear().type('Subtitle');

    cy.percySnapshotDialog('Menu dialog');

    cy.getByTestId(testIds.dialogSubmitButton).click();

    cy.wait('@saveProperties');

    cy.request(
        '/content/bulma-tests/pages/menu/jcr:content/pagecontainer/menu.json'
    )
    .its('body')
    .should('deep.eq', {
      'sling:resourceType': 'bulma/components/menu',
      'jcr:primaryType': 'nt:unstructured'
    });
  });
});
