/*
 * Copyright (C) 2024 Dynamic Solutions
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

import { selectors } from '../../support/consts';
import { CypressTestUtils } from "../common/utils/cypressTestUtils";
import { AuthorInfoDialogConfigurationOwnProperties } from "../common/authorInfo/authorInfoDialogConfigurationOwnProperties";
import { AuthorInfoDialogConfigurationAuthorPage } from "../common/authorInfo/authorInfoDialogConfigurationAuthorPage";
import { AuthorInfoDialogConfiguration } from "../common/authorInfo/authorInfoDialogConfiguration";
import { ClickAction } from "../common/dialog/actions/clickAction";
import { AuthorInfoDialogConfigurationParentPage } from "../common/authorInfo/authorInfoDialogConfigurationParentPage";
import { TextUtils } from "../common/utils/textUtils";
import {AuthorInfoDialog} from "../common/authorInfo/authorInfoDialog";
import {CypressJcrValidator} from "../common/validation/cypressJcrValidator";


const testUtils = new CypressTestUtils();

const componentsToTest = [
  {
    label: 'Blog article author bio',
    name: 'blogarticleauthorbio',
    subContentPath: '/pagecontainer/section/container/blogarticleauthorbio',
    checkRender: (conf: AuthorInfoDialogConfiguration) => checkRenderArticleBio(conf)
  },
  {
    label: 'Blog article header',
    name: 'blogarticleheader',
    subContentPath: '/pagecontainer/section/container/blogarticleheader',
    checkRender: (conf: AuthorInfoDialogConfiguration) => checkRenderArticleHeader(conf)
  },
]

const articlePagePath   = '/content/kyanite-e2e-tests/pages/author-bio/blog-article-page';
const authorPagePath    = '/content/kyanite-e2e-tests/pages/author-bio/author-bio-page';
const authorPageDialogSelector = 'ModalDialog_AuthorBioPage';
const parentPageDialogSelector = 'ModalDialog_PageBlogArticle';

function visitArticlePage(){
  testUtils.visitPage(articlePagePath);
}

function visitAuthorPage() {
  testUtils.visitPage(authorPagePath);
}

/**
 * Actions to make author info fields in page dialog accessible
 * @param pageDialogTestId data-testid attribute for dialog
 */
function getPreActionsForPageDialog(pageDialogTestId: string) {
  return [
      new ClickAction('dialogTab_Metadata', pageDialogTestId)
  ]
}

function checkImageRender(imgSelector: string, sourceConfig: AuthorInfoDialogConfiguration) {
  testUtils.getIframeBody()
    .find(imgSelector)
    .invoke('attr', 'src')
      .should('eq',
              sourceConfig.getValue(AuthorInfoDialog.fields.AuthorPhoto)
                          .replace(/ /g, '%20'));
  testUtils.getIframeBody()
    .find(imgSelector)
    .invoke('attr', 'alt')
      .should('eq',
              sourceConfig.getValue(AuthorInfoDialog.fields.AuthorPhotoAlt));
}

/**
 * compare values in render result to values applied
 * @param sourceConfig configuration that author info is taken from (either ownProperties or
 * the one referenced via 'authorPage' or 'parentPage')
 */
function checkRenderArticleBio(sourceConfig: AuthorInfoDialogConfiguration) {
  const componentSelector = ' div.container.blog-article-author-bio ';
  const imgSelector =
      componentSelector +
      ' article.media ' +
      ' figure.photo ' +
      ' p.image ' +
      ' img ';
  checkImageRender(imgSelector, sourceConfig);

  const mediaSelector =
      componentSelector +
      ' article.media ' +
      ' .media-content ' +
      ' .content ';
  testUtils.getIframeBody()
    .find(mediaSelector + ' .name')
    .should('have.text', sourceConfig.getValue(AuthorInfoDialog.fields.AuthorName));
  testUtils.getIframeBody()
    .find(mediaSelector + ' .role')
    .should('have.text', sourceConfig.getValue(AuthorInfoDialog.fields.AuthorRole));

  testUtils.getIframeBody()
    .find(componentSelector + ' .description p')
    .should('have.text', sourceConfig.getValue(AuthorInfoDialog.fields.AuthorDescription));
}

function checkRenderArticleHeader(sourceConfig: AuthorInfoDialogConfiguration) {
  const componentSelector = ' div.container.blog-article-header ';
  const imgSelector =
      componentSelector +
      ' article.media ' +
      ' figure ' +
      ' p.image ' +
      ' img ';
  checkImageRender(imgSelector, sourceConfig);

  const mediaSelector =
      componentSelector +
      ' article.media ' +
      ' .media-content ' +
      ' .content ';
  testUtils.getIframeBody()
    .find(mediaSelector + ' p strong')
      .should('have.text', sourceConfig.getValue(AuthorInfoDialog.fields.AuthorName));
}

componentsToTest.forEach(component => {

  const componentFullPath = articlePagePath + '/jcr:content' + component.subContentPath;
  const componentDialogSelector = 'ModalDialog_' + TextUtils.toTitleCase(component.name);

  describe(component.label + ' component', function () {
    beforeEach(() => {
      cy.login();
    });

    it('overlay renders correctly in edit mode', function () {
      visitArticlePage();
      testUtils.selectComponent(componentFullPath)
      .find(selectors.overlayName)
      .should('contain.text', component.label);
      testUtils.deselect();
    });

    it('renders correctly with component own properties', function () {

      //  set own component properties
      visitArticlePage();
      const componentOwnProperties = new AuthorInfoDialogConfigurationOwnProperties(componentDialogSelector);
      testUtils.applyConfigurationToComponent(componentFullPath, componentOwnProperties);

      //  check the result
      componentOwnProperties.validateJcrResult(new CypressJcrValidator(componentFullPath));
      component.checkRender(componentOwnProperties);
    });

    it('renders correctly with author page reference', function () {

      //  set properties for the author page
      visitAuthorPage();
      const authorPageOwnProperties = new AuthorInfoDialogConfigurationOwnProperties(authorPageDialogSelector);
      testUtils.applyConfigurationToPage(authorPagePath, authorPageOwnProperties, getPreActionsForPageDialog(authorPageDialogSelector));

      //  refer to the author page
      visitArticlePage();
      const componentAuthorPageProperties = new AuthorInfoDialogConfigurationAuthorPage(componentDialogSelector);
      componentAuthorPageProperties.setValue(AuthorInfoDialog.fields.AuthorPageLink, authorPagePath);
      testUtils.applyConfigurationToComponent(componentFullPath, componentAuthorPageProperties);

      //  check the result
      componentAuthorPageProperties.validateJcrResult(new CypressJcrValidator(componentFullPath));
      component.checkRender(authorPageOwnProperties);
    });

    it('renders correctly with parent page reference', function () {

      //  refer to the parent page
      visitArticlePage();
      const parentPageOwnProperties = new AuthorInfoDialogConfigurationOwnProperties(parentPageDialogSelector);
      testUtils.applyConfigurationToPage(
          articlePagePath,
          parentPageOwnProperties,
          getPreActionsForPageDialog(parentPageDialogSelector));
      //  refer to the parent page
      const componentParentPageProperties = new AuthorInfoDialogConfigurationParentPage(componentDialogSelector);
      testUtils.applyConfigurationToComponent(componentFullPath, componentParentPageProperties);

      //  check the result
      componentParentPageProperties.validateJcrResult(new CypressJcrValidator(componentFullPath));
      component.checkRender(parentPageOwnProperties);
    });
  });
})
