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
  button: 'ComponentOverlay_/content/kyanite-e2e-tests/pages/button/jcr:content/pagecontainer/button',
  anchor: 'ComponentOverlay_/content/kyanite-e2e-tests/pages/button/jcr:content/pagecontainer/anchor'
};

describe('Button component', function () {
  beforeEach(() => {
    cy.login();
  });

  it('button renders correctly in edit mode', function () {

    cy.intercept(
        'POST',
        '**/pagecontainer/button.websight-dialogs-service.save-properties.action'
    ).as('saveProperties');

    cy.visit(
        '/apps/websight/index.html/content/kyanite-e2e-tests/pages/button::editor'
    );

    cy.getByTestId(paths.button)
    .click()
    .find(selectors.overlayName)
    .should('contain.text', 'Button');

    cy.getByTestId(testIds.componentEditIcon).click({force: true});

    cy.getByTestId('ModalDialog_Button')
    .findByTestId('Input_Label')
    .clear()
    .type('Label');

    cy.getByTestId('ModalDialog_Button')
    .find('div[id^="type-uid"]').click();
    cy.get('.Input_Type__menu').contains('Button').click({force: true});

    cy.getByTestId('ModalDialog_Button')
    .find('div[id^="size-uid"]').click();
    cy.contains('Medium').click({force: true});

    cy.getByTestId('ModalDialog_Button')
    .find('div[id^="variant-uid"]').click();
    cy.contains('Success').click({force: true});

    cy.getByTestId('ModalDialog_Button')
    .findByTestId('dialogTab_Modifiers')
    .click();
    cy.getByTestId('ModalDialog_Button')
    .findByTestId('Input_Light')
    .click();
    cy.getByTestId('ModalDialog_Button')
    .findByTestId('Input_Outlined')
    .click();
    cy.getByTestId('ModalDialog_Button')
    .findByTestId('Input_Rounded')
    .click();
    cy.getByTestId('ModalDialog_Button')
    .findByTestId('Input_Fullwidth')
    .click();
    cy.getByTestId('ModalDialog_Button')
    .findByTestId('Input_Disabled')
    .click();


    cy.getByTestId('ModalDialog_Button')
    .findByTestId('dialogTab_Icon')
    .click();
    cy.getByTestId('ModalDialog_Button')
    .findByTestId('Input_Showlefticon?--toggle-check-icon')
    .click();
    cy.getByTestId('ModalDialog_Button')
    .findByTestId('RadioElement_select')
    .click();
    cy.getByTestId('ModalDialog_Button')
    .find('div[id^="leftIcon/iconLibType-uid"]')
    .click();
    cy.contains('.Input_LeftIconLibrary__option', 'Material Design')
    .click();
    cy.getByTestId('ModalDialog_Button')
    .find('div[id^="leftIcon/icon-uid"]').click();
    cy.contains('Cube').click({force: true});

    cy.getByTestId('ModalDialog_Button')
    .findByTestId('dialogTab_Action')
    .click();
    cy.getByTestId('ModalDialog_Button')
    .find('div[id^="actionType-uid"]').click();
    cy.get('.Input_Action__menu').contains('Open modal').click({force: true});
    cy.getByTestId('ModalDialog_Button')
    .findByTestId('Input_ModalID')
    .clear()
    .type('Modal ID');

    cy.getByTestId(testIds.dialogSubmitButton).click();

    cy.wait('@saveProperties');

    cy.request(
        '/content/kyanite-e2e-tests/pages/button/jcr:content/pagecontainer/button.infinity.json'
    )
    .its('body')
    .should('deep.eq',
        {
          'jcr:primaryType': 'nt:unstructured',
          'showIconLeft': 'true',
          'isOutlined': 'true',
          'isDisabled': 'true',
          'isFullWidth': 'true',
          'size': 'is-medium',
          'type': 'button',
          'label': 'Label',
          'isMailto': 'false',
          'variant': 'is-success',
          'isRounded': 'true',
          'modalId': 'Modal ID',
          'sling:resourceType': 'kyanite/common/components/button',
          'actionType': 'open-modal',
          'isLight': 'true',
          'leftIcon': {
              'jcr:primaryType': 'nt:unstructured',
              'iconLibType': 'mdi',
              'icon': 'mdi-cube-outline',
              'selectOrType': 'select'
          },
          'buttonId': ''
        });
  });

  it('anchor renders correctly in edit mode', function () {
    cy.intercept(
        'POST',
        '**/pagecontainer/anchor.websight-dialogs-service.save-properties.action'
    ).as('saveProperties');

    cy.visit(
        '/apps/websight/index.html/content/kyanite-e2e-tests/pages/button::editor'
    );

    cy.getByTestId(paths.anchor)
    .click()
    .find(selectors.overlayName)
    .should('contain.text', 'Button');

    cy.getByTestId(testIds.componentEditIcon).click({force: true});

    cy.getByTestId('ModalDialog_Button')
    .findByTestId('Input_Label')
    .clear()
    .type('Label');

    cy.getByTestId('ModalDialog_Button')
    .find('div[id^="type-uid"]').click();
    cy.get('.Input_Type__menu').contains('Anchor').click({force: true});

    cy.getByTestId('ModalDialog_Button')
    .findByTestId('Input_URL')
    .clear()
    .type('/content');

    cy.getByTestId('ModalDialog_Button')
    .findByTestId('Input_Openlinkinanewtab')
    .click({force: true});

    cy.getByTestId(testIds.dialogSubmitButton).click();

    cy.wait('@saveProperties');

    cy.request(
        '/content/kyanite-e2e-tests/pages/button/jcr:content/pagecontainer/anchor.json'
    )
    .its('body')
    .should('deep.eq',
        {
          'jcr:primaryType': 'nt:unstructured',
          'url': '/content',
          'type': 'a',
          'label': 'Label',
          'sling:resourceType': 'kyanite/common/components/button',
          'openInNewTab': 'true',
          'isMailto': 'false',
          'buttonId': ''
        });
  });
});
