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

import {selectors, testIds} from "../../support/consts";

const paths = {
  buttons: 'ComponentOverlay_/content/bulma-tests/pages/buttons/jcr:content/pagecontainer/buttons'
};

describe('Button component', function () {
  beforeEach(() => {
    cy.login();
  });

  it('renders correctly in edit mode', function () {

    cy.intercept(
        'POST',
        '**/pagecontainer/buttons.websight-dialogs-service.save-properties.action'
    ).as('saveProperties');

    cy.visit(
        '/apps/websight/index.html/content/bulma-tests/pages/buttons::editor'
    );

    cy.getByTestId(paths.buttons)
    .click()
    .find(selectors.overlayName)
    .should('contain.text', 'Buttons');

    cy.getByTestId(testIds.editIcon).click({force: true});

    cy.getByTestId('ModalDialog_Buttons')
    .findByTestId('RadioElement_is-centered').click();

    cy.getByTestId('ModalDialog_Buttons')
    .findByTestId('Input_Addons(jointogether)').click();

    cy.getByTestId('ModalDialog_Buttons')
    .find('div[id^="size-uid"]').click();
    cy.contains('Medium').click({force: true});

    cy.getByTestId(testIds.dialogSubmitButton).click();

    cy.wait('@saveProperties');

    cy.request(
        '/content/bulma-tests/pages/buttons/jcr:content/pagecontainer/buttons.json'
    )
    .its('body')
    .should('deep.eq', {
      'jcr:primaryType': 'nt:unstructured',
      'hasAddons': 'true',
      'size': 'are-medium',
      'sling:resourceType': 'bulma/components/buttons',
      'alignment': 'is-centered'
    });
  });
});
