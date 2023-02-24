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
  section: 'ComponentOverlay_pagecontainer/section'
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
        '/apps/websight/index.html/content/bulma-tests/pages/section::editor'
    );

    cy.getByTestId(paths.section)
    .click()
    .find(selectors.overlayName)
    .should('contain.text', 'Section');

    cy.getByTestId(testIds.editIcon).click({ force: true });

    cy.getByTestId('Input_Sectionid').clear().type('section1');

    cy.get('div[id^="size-uid"]').click();
    cy.contains('Half height').click({ force: true });

    cy.getByTestId(testIds.dialogSubmitButton).click();

    cy.wait('@saveProperties');

    cy.request(
        '/content/bulma-tests/pages/section/jcr:content/pagecontainer/section.json'
    )
    .its('body')
    .should('deep.eq', {
      id: 'section1',
      size: 'is-halfheight',
      'sling:resourceType': 'bulma/components/section',
      'jcr:primaryType': 'nt:unstructured'
    });
  });
});
