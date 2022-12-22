/*
 * Copyright (C) 2022 Dynamic Solutions
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

export default class APIService {
    static instance: APIService;
    private contactFormEntity: string | null = null;

    static getInstance() {
        if (!APIService.instance) {
            APIService.instance = new APIService();
        }
        return APIService.instance;
    }

    private setContactFormEntity(entityName: string) {
        this.contactFormEntity = entityName;
    }
    private getContactFormEntity(): string | boolean {
        if (this.contactFormEntity !== null) {
            return this.contactFormEntity;
        } else {
            return false;
        }
    }
}
