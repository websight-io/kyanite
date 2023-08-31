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

import { selectors, testIds } from '../../support/consts';

const paths = {
  hero: 'ComponentOverlay_/content/kyanite-e2e-tests/pages/hero/jcr:content/pagecontainer/hero'
};

describe('Hero component', function () {
  beforeEach(() => {
    cy.login();
  });

  it('renders correctly in preview mode', function () {
    cy.visit('/content/kyanite-e2e-tests/pages/hero.html');
  });

  it('renders correctly in edit mode', function () {
    cy.intercept(
      'POST',
      '**/pagecontainer/hero.websight-dialogs-service.save-properties.action'
    ).as('saveProperties');

    cy.visit(
      '/apps/websight/index.html/content/kyanite-e2e-tests/pages/hero::editor'
    );

    cy.getByTestId(paths.hero)
      .click()
      .find(selectors.overlayName)
      .should('contain.text', 'Hero');

    cy.getByTestId(testIds.editIcon).click({ force: true });

    cy.getByTestId('ModalDialog_Hero')
      .find('div[id^="size-uid"]').click();
    cy.contains('Large').click({ force: true });

    cy.getByTestId('ModalDialog_Hero')
      .find('div[id^="variant-uid"]').click();

    cy.get('div.Input_Variant__menu')
      .contains('Link')
      .click({ force: true });

    cy.getByTestId(testIds.dialogSubmitButton).click();

    cy.wait('@saveProperties');

    cy.request(
      '/content/kyanite-e2e-tests/pages/hero/jcr:content/pagecontainer/hero.json'
    )
      .its('body')
      .should('deep.eq', {
        size: 'is-large',
        'sling:resourceType': 'kyanite/common/components/hero',
        'jcr:primaryType': 'nt:unstructured',
        variant: 'is-link'
      });
  });
});
