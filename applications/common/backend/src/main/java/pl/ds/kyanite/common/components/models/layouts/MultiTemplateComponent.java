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

package pl.ds.kyanite.common.components.models.layouts;

/**
 * This interface provides a common way of adding/overriding layouts for components.
 * It would be nice to have in any component if there is a chance for its layout to be customized -
 * consider it a contract.
 *
 * Use cases:
 *  - you are using/extending an existing component and you want to change its layout/add a new one
 *  - you are developing a component that can have different layouts by design
 *    (e.g. horizontal and vertical tabs)
 *
 * Component must follow this rules:
 *  1)  component has a default template under template/template-default.html
 *  2)  component's model implements MultiTemplateComponent interface
 *  3)  [component].html imports template by relative path which is retrieved via interface method:
 *      <code>
 *        <sly data-sly-use.template="${model.templatePath}"
 *             data-sly-call="${template.template @ model=model}"></sly>
 *      </code>
 *
 * Then, you may want to customize component's layout in your project:
 * 1) override default layout:
 *    - just create your own ./templates/template-default.html for that component
 * 2) add one or more variants of component's layout:
 *    - add additional templates under ./templates
 *    - include /libs/kyanite/common/components/common/template field into component's dialog
 *    - add options representing your templates to included select
 *    - don't forget to initialize your component with default template
 */
public interface MultiTemplateComponent {

  /**
   * Return layout template relative path. Returns default template path by default,
   * so there is no need to override the method and add a property to you component
   * if you will only have one layout - just place the layout in the template-default.html file
   *
   * @return layout template's relative path
   */
  default String getTemplatePath() {
    return "./templates/template-default.html";
  }

}
