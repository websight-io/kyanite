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
  tag: 'ComponentOverlay_/content/kyanite-tests/pages/tag/jcr:content/pagecontainer/tag'
};

describe('Tag component', function () {
  beforeEach(() => {
    cy.login();
  });

  it('renders correctly in edit mode', function () {

    cy.intercept(
        'POST',
        '**/pagecontainer/tag.websight-dialogs-service.save-properties.action'
    ).as('saveProperties');

    cy.visit(
        '/apps/websight/index.html/content/kyanite-tests/pages/tag::editor'
    );

    cy.getByTestId(paths.tag)
    .click({force: true})
    .find(selectors.overlayName)
    .should('contain.text', 'Tag');

    cy.getByTestId(testIds.editIcon).click({force: true});

    cy.getByTestId('ModalDialog_Tag')
    .findByTestId('Input_Label')
    .clear()
    .type('Label');

    cy.getByTestId('ModalDialog_Tag')
    .find('div[id^="size-uid"]').click();
    cy.contains('Medium').click({force: true});

    cy.getByTestId('ModalDialog_Tag')
    .find('div[id^="variant-uid"]').click();
    cy.contains('Success').click({force: true});

    cy.getByTestId('ModalDialog_Tag')
    .findByTestId('Input_Light')
    .click();

    cy.getByTestId('ModalDialog_Tag')
    .findByTestId('Input_Rounded')
    .click();

    cy.getByTestId(testIds.dialogSubmitButton).click();

    cy.wait('@saveProperties');

    cy.request(
        '/content/kyanite-tests/pages/tag/jcr:content/pagecontainer/tag.json'
    )
    .its('body')
    .should('deep.eq',
        {
          'jcr:primaryType': 'nt:unstructured',
          'size': 'is-medium',
          'label': 'Label',
          'variant': 'is-success',
          'isRounded': 'true',
          'sling:resourceType': 'kyanite/common/components/tag',
          'isLight': 'true'
        });
  });
});
