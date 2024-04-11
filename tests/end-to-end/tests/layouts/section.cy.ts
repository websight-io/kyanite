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
  section: 'ComponentOverlay_/content/kyanite-e2e-tests/pages/section/jcr:content/pagecontainer/section'
};

describe('Section layout', function () {
  beforeEach(() => {
    cy.login();
  });

  it('renders correctly in edit mode', function () {
    cy.intercept(
        'POST',
        '**/pagecontainer/section.websight-dialogs-service.save-properties.action'
    ).as('saveProperties');

    cy.visit(
        '/apps/websight/index.html/content/kyanite-e2e-tests/pages/section::editor'
    );

    cy.getByTestId(paths.section)
    .click()
    .find(selectors.overlayName)
    .should('contain.text', 'Section');

    cy.getByTestId(testIds.componentEditIcon).click({ force: true });

    cy.getByTestId('ModalDialog_Section')
      .findByTestId('Input_Sectionid').clear().type('section1');

    cy.getByTestId('ModalDialog_Section')
      .find('div[id^="size-uid"]').click();
    cy.contains('Half height').click({ force: true });

    cy.getByTestId(testIds.dialogSubmitButton).click();

    cy.wait('@saveProperties');

    cy.request(
        '/content/kyanite-e2e-tests/pages/section/jcr:content/pagecontainer/section.json'
    )
    .its('body')
    .should('deep.eq', {
      id: 'section1',
      size: 'is-halfheight',
      'sling:resourceType': 'kyanite/common/components/section',
      'jcr:primaryType': 'nt:unstructured'
    });
  });
});
