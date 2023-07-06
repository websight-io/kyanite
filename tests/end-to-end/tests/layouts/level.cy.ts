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
  level: 'ComponentOverlay_/content/kyanite-tests/pages/level/jcr:content/pagecontainer/level_3'
};

describe('Level component', function () {
  beforeEach(() => {
    cy.login();
  });

  it('renders correctly in preview mode', function () {
    cy.visit('/content/kyanite-tests/pages/level.html');
    cy.percySnapshotPreview('Level preview');

    cy.contains('Search').click();
    cy.contains('Bulma').should('be.visible');
  });

  it('renders correctly in edit mode', function () {
    cy.intercept(
      'POST',
      '**/pagecontainer/level.websight-dialogs-service.save-properties.action'
    ).as('saveProperties');

    cy.visit(
      '/apps/websight/index.html/content/kyanite-tests/pages/level::editor'
    );


    cy.percySnapshotPageEditor('Level editor');

    cy.request(
      '/content/kyanite-tests/pages/level/jcr:content/pagecontainer/level_2.json'
    )
      .its('body')
      .should('deep.eq', {
        'sling:resourceType': 'kyanite/components/level',
        'jcr:primaryType': 'nt:unstructured'
      });

    cy.request(
        '/content/kyanite-tests/pages/level/jcr:content/pagecontainer/level_3_1.json'
    )
    .its('body')
    .should('deep.eq', {
      isVertical: 'false',
      'jcr:primaryType': 'nt:unstructured',
      'sling:resourceType': 'kyanite/components/level',
    });
  });
});
