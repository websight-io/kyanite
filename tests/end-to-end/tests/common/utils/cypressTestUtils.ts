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

import {Const} from "../const";
import {testIds} from "../../../support/consts";
import {DialogConfiguration} from "../dialog/dialogConfiguration";
import {DialogAction} from "../dialog/actions/dialogAction";
import Chainable = Cypress.Chainable;

export class CypressTestUtils {

  public visitPage(pagePath: string) {
    cy.login()
    const getPropertiesAlias = 'pageGetProperties';
    cy.intercept(
        'GET',
        '**' + pagePath + Const.SlingSelectors.PageGetProperties
    ).as(getPropertiesAlias);
    cy.visit('/apps/websight/index.html' + pagePath + '::editor');
    cy.wait('@' + getPropertiesAlias);
  }

  /**
   * Click a component if is not active (second click forces dialog to open so we avoid it
   * to open the dialog explicitly)
   * @param componentPath - exact path to component's overlay
   */
  public selectComponent(componentPath: string): Chainable<JQuery> {
    const overlayPath = 'ComponentOverlay_' + componentPath;
    return cy.getByTestId(overlayPath)
             .click();
  }

  /**
   * Remove focus from element.
   * @param alternativeElementSelector selector for element to take focus instead,
   * by default it will be the right sidebar, because clicking on it doesn't have any effect
   */
  public deselect(alternativeElementSelector='RightSidebar_Content') {
    cy.getByTestId(alternativeElementSelector)
      .click();
  }

  /**
   * Open the  dialog, apply changes and close the dialog
   * @param path component/page content path
   * @param conf DialogConfiguration to be applied
   * @param preActions additional actions to be performed before applying the configuration
   *    e.g. clicking the proper tab in dialog
   */
  private applyConfiguration(path: string, conf: DialogConfiguration, preActions: DialogAction[]=[], isPage) {
    isPage ? this.openPageDialog() : this.openComponentDialog(path);
    const saveAlias = isPage ? 'savePageProperties' : 'saveProperties';
    this.interceptSaveProperties(path + (isPage ? '/jcr:content' : ''), saveAlias);
    preActions.forEach(a => a.execute());
    conf.execute();
    this.submitDialog(saveAlias);
    if (!isPage) this.deselect();
  }

  public applyConfigurationToComponent(componentPath: string, conf: DialogConfiguration, preActions: DialogAction[]=[])  {
    this.applyConfiguration(componentPath, conf, preActions, false);
  }

  public applyConfigurationToPage(pagePath: string, conf: DialogConfiguration, preActions: DialogAction[]=[]) {
    this.applyConfiguration(pagePath, conf, preActions, true);
  }

  private openComponentDialog(componentFullPath: string) {
    this.selectComponent(componentFullPath);
    cy.getByTestId(testIds.componentEditIcon).click({ force: true });
  }

  public openPageDialog() {
    return cy.getByTestId(testIds.pageEditIcon).click({ force: true });
  }

  private interceptSaveProperties(componentPath: string, alias = 'saveProperties') {
    cy.intercept(
        'POST',
        '**' + componentPath + Const.SlingSelectors.SaveProperties
    ).as(alias);
  }

  private submitDialog(saveAlias: string) {
    cy.getByTestId(testIds.dialogSubmitButton).click();
    cy.wait('@' + saveAlias);
  }

  public getComponentJson(componentFullPath: string): Chainable<Cypress.Response<string>> {
    return cy.request(componentFullPath + Const.SlingSelectors.ComponentGetJson).its('body');
  }

  /**
   * Get Iframe body for testing actual component's content
   */
  public getIframeBody = () => {
    // get the document
    return this.getIframeDocument()
      // automatically retries until body is loaded
      .its('body').should('not.be.undefined')
      // wraps "body" DOM element to allow chaining more Cypress commands
      .then(cy.wrap)
  }

  /**
   * Cypress yields jQuery element, which has the real DOM element under property "0".
   * From the real DOM iframe element we can get the "document" element stored in "contentDocument" property
   * Cypress "its" command can access deep properties using dot notation
   */
  private getIframeDocument = () => {
    return cy
      .get('iframe[title="Page edit"]')
      .its('0.contentDocument')
        .should('exist')
  }

}
