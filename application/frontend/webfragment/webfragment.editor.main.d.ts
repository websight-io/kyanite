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

export interface MockTypeEditorPage {
    [key: string]: any;
    path: string;
}

export interface MockTypeEditorComponent {
    [key: string]: any;
    domNodes: Node[];
    name: string;
}

export interface MockTypeEditorEventTarget {
    target: MockTypeEditorComponent;
}

/**
 * @see `interface Editor extends EditorEventTarget` in `website-pages` repository
 *
 * @todo how we can import types from `website-pages` repository? Maybe https://teamds.atlassian.net/browse/WOS-218
 */
export interface MockTypeEditor {
    actionManager: any,
    context: any,
    componentsActions: any,
    activeComponents: any,
    componentTree: any,
    areas: any,
    eventTarget: any,
    editedPage: MockTypeEditorPage,
    addEventListener(type: string, listener: (event: MockTypeEditorEventTarget) => void): void,
    removeEventListener(type: string, listener: ([...any]) => void): void;
}

export type OnDomContentUpdatedEvent = CustomEvent<Pick<MockTypeEditorComponent, 'name' | 'domNodes'>>
