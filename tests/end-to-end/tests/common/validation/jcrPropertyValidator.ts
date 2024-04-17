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

/**
 * Is capable of comparing component properties from JCR with expected result
 */
export abstract class JcrPropertyValidator {

  protected componentPath: string;

  constructor(componentPath: string) {
    this.componentPath = componentPath;
  }

  /**
   * Get actual data from jCR repository
   */
  abstract refreshData();

  /**
   * compare jcr property value with expectation
   */
  abstract validate(propName: string, propValue: string);
}
