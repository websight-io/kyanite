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

import { selectors, testIds } from '../support/consts';

const paths = {
  hero: 'ComponentOverlay_pagecontainer/hero'
};

describe('Hero component', function () {
  beforeEach(() => {
    cy.login();
  });

  it('renders correctly in preview mode', function () {
    cy.visit('/content/bulma-tests/pages/hero.html');
    cy.percySnapshotPreview('Hero preview');

    cy.contains('Default title').click();
    cy.contains('Warning').should('be.visible');
  });

  it('renders correctly in edit mode', function () {
    cy.intercept(
      'POST',
      '**/pagecontainer/hero.websight-dialogs-service.save-properties.action'
    ).as('saveProperties');

    cy.visit(
      '/apps/websight/index.html/content/bulma-tests/pages/hero::editor'
    );

    cy.getByTestId(paths.hero)
      .click()
      .find(selectors.overlayName)
      .should('have.text', 'Hero');

    cy.percySnapshotPageEditor('Hero editor');

    cy.getByTestId(testIds.editIcon).click();

    cy.getByTestId('Input_Title-container').clear().type('Title');
    cy.getByTestId('Input_Subtitle-container').clear().type('Subtitle');

    cy.get('div[id^="size-uid"]').click();
    cy.contains('Large').click({ force: true });

    cy.get('div[id^="variant-uid"]').click();
    cy.contains('Link').click({ force: true });

    cy.percySnapshotDialog('Hero dialog');

    cy.getByTestId(testIds.dialogSubmitButton).click();

    cy.wait('@saveProperties');

    cy.request(
      '/content/bulma-tests/pages/hero/jcr:content/pagecontainer/hero.json'
    )
      .its('body')
      .should('deep.eq', {
        subTitle: 'Subtitle',
        size: 'is-large',
        'sling:resourceType': 'bulma/components/hero',
        'jcr:primaryType': 'nt:unstructured',
        title: 'Title',
        variant: 'is-link'
      });
  });
});
