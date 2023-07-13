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



/**
 * @see https://docs.websight.io/cms/developers/page-editor/extending-editor/#registering-extension
 */
export default {
    /**
     * @param editor {MockTypeEditor}
     */
    init: (editor) => {
        "use strict";

        editor.addEventListener('component-dom-updated', (event) => {
            onContentDomUpdated(event, editor);
        });
    }
};

/**
 * @param event {MockTypeEditorEventTarget}
 * @param editor {MockTypeEditor}
 */
function onContentDomUpdated (event, editor) {
    const clientIframe = getClientIframe(editor);

    if (!clientIframe) {
        return;
    }

    const { name, domNodes } = event.target;

    /**
     * @type {OnDomContentUpdatedEvent}
     */
    const eventInformClient = new CustomEvent('editor-component-dom-updated', {
        detail: {
            name,
            domNodes,
        },
    });

    clientIframe.contentDocument.dispatchEvent(eventInformClient);
}

/**
 * @returns {HTMLIFrameElement[]}
 */
function getEditorIframes () {
    return Array.from(document.body.querySelectorAll('iframe'));
}

/**
 * @returns {HTMLIFrameElement}
 */
function getClientIframe (editor) {
    return getEditorIframes().find(iframe => iframe.src.indexOf(editor.editedPage.path));
}
