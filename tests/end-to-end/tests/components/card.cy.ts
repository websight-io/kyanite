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
  card: 'ComponentOverlay_/content/kyanite-e2e-tests/pages/card/jcr:content/pagecontainer/card'
};

describe('Card component', function () {
  beforeEach(() => {
    cy.login();
  });

  it('renders correctly in edit mode', function () {
    cy.intercept(
        'POST',
        '**/pagecontainer/card.websight-dialogs-service.save-properties.action'
    ).as('saveProperties');

    cy.visit(
        '/apps/websight/index.html/content/kyanite-e2e-tests/pages/card::editor'
    );

    cy.getByTestId(paths.card)
    .click({force: true})
    .find(selectors.overlayName)
    .should('contain.text', 'Card');

    cy.getByTestId(testIds.editIcon).click({ force: true });

    cy.getByTestId('ModalDialog_Card')
      .findByTestId('Input_Cardheader').clear().type('Card header');

    cy.getByTestId('ModalDialog_Card')
      .findByTestId('dialogTab_Image').click();
    cy.getByTestId('ModalDialog_Card')
    .findByTestId('Input_Alttext').clear().type('Alt text');
    cy.getByTestId('ModalDialog_Card')
      .findByTestId('RadioElement_link').click();
    cy.getByTestId('ModalDialog_Card')
      .findByTestId("Input_Chooseimage")
      .clear().type('/content/kyanite-e2e-tests/assets/images/personal/PortfolioForum.png');
    cy.getByTestId('ModalDialog_Card')
      .find('div[class^="Input_Imageratio__control"]').click();
    cy.contains('16 by 9').click({ force: true });

    cy.getByTestId('ModalDialog_Card')
      .findByTestId('dialogTab_Footer').click();
    cy.getByTestId('ModalDialog_Card')
      .findByTestId('Button_Multifield_Add').click({ force: true });
    cy.getByTestId('ModalDialog_Card')
      .findByTestId('Input_urls/0/label').clear().type('Card footer label');

    cy.getByTestId(testIds.dialogSubmitButton).click();

    cy.wait('@saveProperties');

    cy.request(
        '/content/kyanite-e2e-tests/pages/card/jcr:content/pagecontainer/card.infinity.json'
    )
    .its('body')
    .should('deep.eq', {
      header: 'Card header',
      'jcr:primaryType': 'nt:unstructured',
      'sling:resourceType': 'kyanite/common/components/card',
      'cardcontent': {
        'jcr:primaryType': 'nt:unstructured',
        'sling:resourceType': 'kyanite/common/components/card/cardcontent'
      },
      imageSrcType: 'link',
      'image': {
        alt: 'Alt text',
        assetReference: '/content/kyanite-e2e-tests/assets/images/personal/PortfolioForum.png',
        style: 'is-16by9',
        'jcr:primaryType': 'nt:unstructured',
      },
      'urls': {
        'jcr:primaryType' : 'nt:unstructured',
        '0': {
          label: 'Card footer label',
          'jcr:primaryType': 'nt:unstructured'
        }
      }
    });
  });
});
