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
  message: 'ComponentOverlay_/content/kyanite-e2e-tests/pages/message/jcr:content/pagecontainer/message'
};

describe('Message component', function () {
  beforeEach(() => {
    cy.login();
  });

  it('renders correctly in edit mode', function () {

    cy.intercept(
        'POST',
        '**/pagecontainer/message.websight-dialogs-service.save-properties.action'
    ).as('saveProperties');

    cy.visit(
        '/apps/websight/index.html/content/kyanite-e2e-tests/pages/message::editor'
    );

    cy.getByTestId(paths.message)
    .click()
    .find(selectors.overlayName)
    .should('contain.text', 'Message');

    cy.getByTestId(testIds.editIcon).click({force: true});

    cy.getByTestId('ModalDialog_Message')
    .findByTestId('Input_Content')
    .clear()
    .type('Content');

    cy.getByTestId('ModalDialog_Message')
    .find('div[id^="size-uid"]').click();
    cy.contains('Medium').click({force: true});

    cy.getByTestId('ModalDialog_Message')
    .find('div[id^="variant-uid"]').click();
    cy.contains('Success').click({force: true});

    cy.getByTestId('ModalDialog_Message')
    .findByTestId('dialogTab_Header')
    .click();

    cy.getByTestId('ModalDialog_Message')
    .findByTestId('Input_Createheader').click();

    cy.getByTestId('ModalDialog_Message')
    .findByTestId('Input_Headercontent')
    .clear()
    .type('Header content');

    cy.getByTestId('ModalDialog_Message')
    .findByTestId('Input_Hasdeletebutton--checkbox-label').click();

    cy.getByTestId(testIds.dialogSubmitButton).click();

    cy.wait('@saveProperties');

    cy.request(
        '/content/kyanite-e2e-tests/pages/message/jcr:content/pagecontainer/message.json'
    )
    .its('body')
    .should('deep.eq',
        {
          'jcr:primaryType': 'nt:unstructured',
          'header': 'Header content',
          'size': 'is-medium',
          'variant': 'is-success',
          'showButton': 'true',
          'showHeader': 'true',
          'content': '<p>Content</p>',
          'sling:resourceType': 'kyanite/common/components/message'
        });
  });
});
