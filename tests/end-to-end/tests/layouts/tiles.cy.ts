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

import { selectors, testIds } from '../../support/consts';

const paths = {
  tiles: 'ComponentOverlay_pagecontainer/tiles',
  tileParent: 'ComponentOverlay_pagecontainer/tiles/tile1/tile1/tile2',
  tileChild: 'ComponentOverlay_pagecontainer/tiles/tile1/tile1/tile2/tile1'
};

describe('Tiles layout', function () {
  beforeEach(() => {
    cy.login();
  });

  it('renders correctly in edit mode', function () {

    cy.visit(
        '/apps/websight/index.html/content/bulma-tests/pages/tiles::editor'
    );

   testTileParent();
   testTileChild();
  });

  function testTileParent() {
    cy.intercept(
        'POST',
        '**/pagecontainer/tiles/tile1/tile1/tile2.websight-dialogs-service.save-properties.action'
    ).as('saveProperties');

    cy.getByTestId(paths.tileParent)
    .click({ force: true })
    .find(selectors.overlayName)
    .should('contain.text', 'Parent tile');

    cy.getByTestId(testIds.editIcon).click({ force: true });

    cy.get('div[id^="size-uid"]').click();
    cy.contains('Is 7').click({ force: true });

    cy.getByTestId('Input_Vertical').click();

    cy.getByTestId(testIds.dialogSubmitButton).click();

    cy.wait('@saveProperties');

    cy.request(
        '/content/bulma-tests/pages/tiles/jcr:content/pagecontainer/tiles/tile1/tile1/tile2.json'
    )
    .its('body')
    .should('deep.eq', {
      size: 'is-7',
      isVertical: 'true',
      'sling:resourceType': 'bulma/components/tiles/tileparent',
      'jcr:primaryType': 'nt:unstructured'
    });
  }

  function testTileChild() {
    cy.intercept(
        'POST',
        '**/pagecontainer/tiles/tile1/tile1/tile2/tile1.websight-dialogs-service.save-properties.action'
    ).as('saveProperties');

    cy.getByTestId(paths.tileChild)
    .click({ force: true })
    .find(selectors.overlayName)
    .should('contain.text', 'Child tile');

    cy.getByTestId(testIds.editIcon).click({ force: true });

    cy.get('div[id^="type-uid"]').click();
    cy.contains('Notification').click({ force: true });

    cy.get('div[id^="variant-uid"]').click();
    cy.contains('Warning').click({ force: true });

    cy.getByTestId(testIds.dialogSubmitButton).click();

    cy.wait('@saveProperties');

    cy.request(
        '/content/bulma-tests/pages/tiles/jcr:content/pagecontainer/tiles/tile1/tile1/tile2/tile1.json'
    )
    .its('body')
    .should('deep.eq', {
      type: 'notification',
      variant: 'is-warning',
      'sling:resourceType': 'bulma/components/tiles/tilechild',
      'jcr:primaryType': 'nt:unstructured'
    });
  }
});
