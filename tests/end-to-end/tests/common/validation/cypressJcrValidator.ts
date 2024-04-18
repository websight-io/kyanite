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

import {JcrPropertyValidator} from "./jcrPropertyValidator";
import {CypressTestUtils} from "../utils/cypressTestUtils";

export class CypressJcrValidator extends JcrPropertyValidator {

  private cyJsonBody;
  private testUtils;

  constructor(componentPath: string) {
    super(componentPath);
  }


  public refreshData() {
    if (!this.testUtils) this.testUtils = new CypressTestUtils();
    this.cyJsonBody = this.testUtils.getComponentJson(this.componentPath);
  }

  public validate(propName: string, propValue: string) {
    if (!this.cyJsonBody) this.refreshData();
    const jsonName = propName.replace('/', '.');
    this.cyJsonBody.should('nested.include',
                           { [jsonName]: propValue });
  }

}
