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
  breadcrumb: 'ComponentOverlay_/content/kyanite-tests/pages/breadcrumb/jcr:content/pagecontainer/breadcrumb'
};

describe('Breadcrumb component', function () {
  beforeEach(() => {
    cy.login();
  });

  it('renders correctly in edit mode', function () {
    cy.intercept(
        'POST',
        '**/pagecontainer/breadcrumb.websight-dialogs-service.save-properties.action'
    ).as('saveProperties');

    cy.visit(
        '/apps/websight/index.html/content/kyanite-tests/pages/breadcrumb::editor'
    );

    cy.getByTestId(paths.breadcrumb)
    .click()
    .find(selectors.overlayName)
    .should('contain.text', 'Breadcrumb');

    cy.getByTestId(testIds.editIcon).click({ force: true });

    cy.getByTestId('ModalDialog_Breadcrumb')
      .find('div[id^="separator-uid"]').click();
    cy.contains('Bullet').click({ force: true });

    cy.getByTestId('ModalDialog_Breadcrumb')
      .find('div[id^="size-uid"]').click();
    cy.contains('Medium').click({ force: true });

    cy.getByTestId('ModalDialog_Breadcrumb')
      .findByTestId('RadioElement_is-right').click();

    cy.getByTestId('ModalDialog_Breadcrumb')
      .findByTestId('Button_Multifield_Add').click({ force: true });
    cy.getByTestId('ModalDialog_Breadcrumb')
      .findByTestId('Input_elements/0/label').clear().type('Label');

    cy.getByTestId(testIds.dialogSubmitButton).click();

    cy.wait('@saveProperties');

    cy.request(
        '/content/kyanite-tests/pages/breadcrumb/jcr:content/pagecontainer/breadcrumb.infinity.json'
    )
    .its('body')
    .should('deep.eq', {
      'jcr:primaryType': 'nt:unstructured',
      size: 'is-medium',
      'sling:resourceType': 'kyanite/components/breadcrumb',
      separator: 'has-bullet-separator',
      alignment: 'is-right',
      'elements': {
        'jcr:primaryType':'nt:unstructured',
        '0': {
          'jcr:primaryType':'nt:unstructured',
          icon: 'fa-home',
          'label':'Label'
        }
      }
    });
  });
});
