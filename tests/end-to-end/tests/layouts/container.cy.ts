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
  container: 'ComponentOverlay_/content/kyanite-e2e-tests/pages/container/jcr:content/pagecontainer/container'
};

describe('Container layout', function () {
  beforeEach(() => {
    cy.login();
  });

  it('renders correctly in edit mode', function () {
    cy.intercept(
        'POST',
        '**/pagecontainer/container.websight-dialogs-service.save-properties.action'
    ).as('saveProperties');

    cy.visit(
        '/apps/websight/index.html/content/kyanite-e2e-tests/pages/container::editor'
    );

    cy.getByTestId(paths.container)
    .click()
    .find(selectors.overlayName)
    .should('contain.text', 'Container');

    cy.getByTestId(testIds.editIcon).click({ force: true });

    cy.getByTestId('ModalDialog_Container')
      .find('div[id^="containerStyle-uid"]').click();
    cy.contains('Full hd').click({ force: true });

    cy.getByTestId(testIds.dialogSubmitButton).click();

    cy.wait('@saveProperties');

    cy.request(
        '/content/kyanite-e2e-tests/pages/container/jcr:content/pagecontainer/container.json'
    )
    .its('body')
    .should('deep.eq', {
      containerStyle: 'is-fullhd',
      'sling:resourceType': 'kyanite/common/components/container',
      'jcr:primaryType': 'nt:unstructured'
    });
  });
});
