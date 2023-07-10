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
  menu: 'ComponentOverlay_/content/kyanite-tests/pages/menu/jcr:content/pagecontainer/menu'
};

describe('Menu component', function () {
  beforeEach(() => {
    cy.login();
  });

  it('renders correctly in edit mode', function () {

    cy.intercept(
        'POST',
        '**/pagecontainer/menu.websight-dialogs-service.save-properties.action'
    ).as('saveProperties');

    cy.visit(
        '/apps/websight/index.html/content/kyanite-tests/pages/menu::editor'
    );

    cy.getByTestId(paths.menu)
    .click()
    .find(selectors.overlayName)
    .should('contain.text', 'Menu');

    cy.getByTestId(testIds.editIcon).click({ force: true });

    cy.getByTestId('ModalDialog_Menu')
      .findByTestId('Button_Multifield_Add').click();

    cy.getByTestId('ModalDialog_Menu')
      .findByTestId('Input_menuSections/0/label').clear().type('Section 1');

    cy.getByTestId('ModalDialog_Menu')
      .findByTestId('Input_menuSections/0/menuListItemComponentList')
      .findByTestId('Button_Multifield_Add').click();

    cy.getByTestId('ModalDialog_Menu')
      .findByTestId('Input_menuSections/0/menuListItemComponentList/0/text')
      .clear()
      .type('Item 1');

    cy.getByTestId('ModalDialog_Menu')
      .findByTestId('Input_menuSections/0/menuListItemComponentList/0/url')
      .clear()
      .type('itemUrl');

    cy.getByTestId('ModalDialog_Menu')
      .findByTestId('Input_menuSections/0/menuListItemComponentList/0/secondLevelItems')
      .findByTestId('Button_Multifield_Add').click();

    cy.getByTestId('ModalDialog_Menu')
      .findByTestId('Input_menuSections/0/menuListItemComponentList/0/secondLevelItems/0/text')
      .clear()
      .type('Sub item 1');

    cy.getByTestId('ModalDialog_Menu')
      .findByTestId('Input_menuSections/0/menuListItemComponentList/0/secondLevelItems/0/url')
      .clear()
      .type('subItemUrl');

    cy.getByTestId(testIds.dialogSubmitButton).click();

    cy.wait('@saveProperties');

    cy.request(
        '/content/kyanite-tests/pages/menu/jcr:content/pagecontainer/menu.infinity.json'
    )
    .its('body')
    .should('deep.eq', {
      'jcr:primaryType': 'nt:unstructured',
      'sling:resourceType': 'kyanite/components/menu',
      'menuSections': {
        'jcr:primaryType': 'nt:unstructured',
        '0': {
          'jcr:primaryType': 'nt:unstructured',
          'label': 'Section 1',
          'menuListItemComponentList': {
            'jcr:primaryType': 'nt:unstructured',
            '0': {
              'jcr:primaryType': 'nt:unstructured',
              'url': 'itemUrl',
              'text': 'Item 1',
              'secondLevelItems': {
                'jcr:primaryType': 'nt:unstructured',
                '0': {
                  'jcr:primaryType': 'nt:unstructured',
                  'url': 'subItemUrl',
                  'text': 'Sub item 1'
                }
              }
            }
          }
        }
      }
    });


  });
});
