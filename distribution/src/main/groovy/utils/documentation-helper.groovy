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

rootPaths = [
    '/libs/kyanite/common',
    '/libs/kyanite/blogs',
    '/libs/kyanite/fragments'
]
RESOURCE_TYPE = 'ws:Component'
SEP=';'
FIELDNAME_ROOT_PATH = 'root'

def findComponents(rootPath) {

    components = []

    // Fetch the root resource
    Resource rootResource = resourceResolver.getResource(rootPath)

    if (rootResource != null) {
        rootResource.listChildren().each { Resource resource ->
            findMatchingComponents(resource, components)
        }
    } else {
        println "Root path not found: ${rootPath}"
    }

    return components
}

def findMatchingComponents(Resource resource, components) {
    if (resource.getValueMap().get("sling:resourceType") == RESOURCE_TYPE) {
        components.add(resource)
    }

    // Recursively check child resources
    resource.listChildren().each { Resource childResource ->
        findMatchingComponents(childResource, components)
    }
}

def collectComponentMetadata(res) {
    def comp = [:]
    def vm = res.getValueMap()
    comp.path = res.path
    comp.title              = vm.get('title')
    comp.group              = vm.get('group')
    comp.allowedChildren    = vm.get('allowedComponents') ?: []
    comp.isContainer        = vm.get('isContainer') ? 'x' : ''
    comp.isLayout           = vm.get('isLayout')    ? 'x' : ''
    return comp
}

def formatComponentMetadata(meta) {
    return meta.title \
    + SEP + meta.group \
    + SEP + meta.allowedChildren.join(',') \
    + SEP + meta.isContainer \
    + SEP + meta.isLayout \
    + SEP + meta.path \
    + SEP + meta[FIELDNAME_ROOT_PATH]
}

components = []
componentsMetadata = []

rootPaths.each {
    rootPath ->
        findComponents(rootPath).each {
            comp ->
                def meta = collectComponentMetadata(comp)
                meta[FIELDNAME_ROOT_PATH] = rootPath
                componentsMetadata.add(meta)
        }
}

componentsMetadata.each {
    metadata ->
        println(formatComponentMetadata(metadata))
}
