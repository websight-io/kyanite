const paths = {
  modal: 'ComponentOverlay_pagecontainer/modal'
};

describe('Modal component', function () {
  beforeEach(() => {
    cy.login();
  });

  it('renders correctly in preview mode', function () {
    cy.visit('/content/bulma-tests/pages/modal.html');
    cy.contains('Open JS example modal').click();

    cy.percySnapshotPreview('Modal preview');
    cy.contains('Very big title').should('be.visible');
  });

  it('renders correctly in edit mode', function () {

    cy.visit(
        '/apps/websight/index.html/content/bulma-tests/pages/modal::editor'
    );

    cy.percySnapshotPageEditor('Hero editor');

  });
});
