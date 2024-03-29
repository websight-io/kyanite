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

const paths = {
  modal: 'ComponentOverlay_/content/kyanite-e2e-tests/pages/modal/jcr:content/pagecontainer/modal'
};

describe('Modal component', function () {
  beforeEach(() => {
    cy.login();
  });

  it('renders correctly in preview mode', function () {
    cy.visit('/content/kyanite-e2e-tests/pages/modal.html');
    cy.contains('Open JS example modal').click();

    cy.contains('Nice header').should('be.visible');
  });

  it('renders correctly in edit mode', function () {
    cy.visit(
        '/apps/websight/index.html/content/kyanite-e2e-tests/pages/modal::editor'
    );
  });
});
