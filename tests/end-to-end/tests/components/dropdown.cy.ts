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
  dropdown: 'ComponentOverlay_/content/bulma-tests/pages/dropdown/jcr:content/pagecontainer/dropdown'
};

describe('Dropdown component', function () {
  beforeEach(() => {
    cy.login();
  });

  it('renders correctly in edit mode', function () {
    cy.intercept(
        'POST',
        '**/pagecontainer/dropdown.websight-dialogs-service.save-properties.action'
    ).as('saveProperties');

    cy.visit(
        '/apps/websight/index.html/content/bulma-tests/pages/dropdown::editor'
    );

    cy.getByTestId(paths.dropdown)
    .should('be.visible')
    .click()
    .find(selectors.overlayName)
    .should('contain.text', 'Dropdown');

    cy.getByTestId(testIds.editIcon).click({ force: true });

    cy.getByTestId('Input_Labelofadropdown').clear().type('Label');

    cy.getByTestId('Input_Hoverable--checkbox-label').click();
    cy.getByTestId('Input_RightAligned--checkbox-label').click();
    cy.getByTestId('Input_Revertexpandingdirection--checkbox-label').click();


    cy.getByTestId('Button_Multifield_Add').click({ force: true });
    cy.getByTestId('Input_Itemlabel').clear().type('Item 1');
    cy.getByTestId(testIds.dialogSubmitButton).click();

    cy.wait('@saveProperties');

    cy.request(
        '/content/bulma-tests/pages/dropdown/jcr:content/pagecontainer/dropdown.infinity.json'
    )
    .its('body')
    .should('deep.eq', {
      label: 'Label',
      isHoverable: 'true',
      isRight: 'true',
      isUp: 'true',
      'jcr:primaryType': 'nt:unstructured',
      'sling:resourceType': 'bulma/components/dropdown',
      items: {
        'jcr:primaryType': 'nt:unstructured',
        '0': {
          label: 'Item 1',
          hasDivider: 'false',
          'jcr:primaryType': 'nt:unstructured',
        },
      }
    });
  });
});
