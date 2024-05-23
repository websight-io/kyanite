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
 * <p>
 * Use cases:
 * <ul>
 *    <li>you are using/extending an existing component and you want to change its layout
 *    or add a new one</li>
 *    <li>you are developing a component that can have different layouts by design
 *    (e.g. horizontal and vertical tabs)</li>
 * </ul>
 * </p>
 *
 * <p>Component must follow this rules:
 * <ol>
 *    <li>component has a default template under templates/template-default.html</li>
 *    <li>component's model implements MultiTemplateComponent interface</li>
 *    <li>[component].html imports template by relative path retrieved via interface method
 *      <p>&lt;sly data-sly-use.template=&quot;${model.templatePath}&quot;</p>
 *      <p>       data-sly-call=&quot;${template.template @ model=model}&quot;&gt;&lt;/sly&gt;</p>
 *    </li>
 * </ol>
 * </p>
 *
 * <p>Then, you may want to customize component's layout in your project:
 * <ol>
 *    <li>override default layout:
 *      <ul>
 *        <li>just create your own ./templates/template-default.html for that component</li>
 *      </ul>
 *    </li>
 *    <li>add one or more variants of component's layout:
 *      <ul>
 *        <li>add additional templates under ./templates</li>
 *        <li>include /libs/kyanite/common/components/common/template into component's dialog</li>
 *        <li>add options representing your templates to included select</li>
 *        <li>initialize the templatePath property, because now you actually store it in JCR</li>
 *      </ul>
 *    </li>
 * </ol>
 * </p>
 */
public interface TemplateHandler {

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
