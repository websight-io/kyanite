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
  card: 'ComponentOverlay_pagecontainer/card'
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
        '/apps/websight/index.html/content/bulma-tests/pages/card::editor'
    );

    cy.getByTestId(paths.card)
    .click({force: true})
    .find(selectors.overlayName)
    .should('contain.text', 'Card');

    cy.getByTestId(testIds.editIcon).click({ force: true });

    cy.getByTestId('Input_Cardheader').clear().type('Card header');

    cy.getByTestId('dialogTab_Image').click();
    cy.getByTestId('Input_Alttext').clear().type('Alt text');
    cy.getByTestId('Input_Usethisifyouwantalinktoimagefromweb').clear().type('/content/bulma/assets/images/personal/PortfolioForum.png');
    cy.get('div[class^="Input_Imageratio__control"]').click();
    cy.contains('16 by 9').click({ force: true });

    cy.getByTestId('dialogTab_Footer').click();
    cy.getByTestId('Button_Multifield_Add').click({ force: true });
    cy.getByTestId('Input_Label').clear().type('Card footer label');

    cy.getByTestId(testIds.dialogSubmitButton).click();

    cy.wait('@saveProperties');

    cy.request(
        '/content/bulma-tests/pages/card/jcr:content/pagecontainer/card.infinity.json'
    )
    .its('body')
    .should('deep.eq', {
      header: 'Card header',
      'jcr:primaryType': 'nt:unstructured',
      'sling:resourceType': 'bulma/components/card',
      'cardcontent': {
        'jcr:primaryType': 'nt:unstructured',
        'sling:resourceType': 'bulma/components/card/cardcontent'
      },
      'image': {
        alt: 'Alt text',
        src: '/content/bulma/assets/images/personal/PortfolioForum.png',
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
