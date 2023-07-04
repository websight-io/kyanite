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

/*
 * This script changes the content structure of the title and content
 * components. Shade and subtitleShade properties will be created
 *  for every shade in the node. Old ones will be removed.
 */

dryRun = true
rootPaths = ['/content']
changeCounter = 0

def findButtonComponents(String rootPath) {
    return resourceResolver.findResources("SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE([$rootPath]) AND" +
            "[sling:resourceType]='bulma/components/button'", "JCR-SQL2")
}

def processIconChanges(Resource res) {
    def vm = res.getValueMap()
    def modifiableValueMap = res.adaptTo(org.apache.sling.api.resource.ModifiableValueMap)
    if (vm['iconLeft']) {
        def selectOrTypeLeft = vm['selectOrTypeLeft']
        def iconLibTypeLeft = vm['iconLibTypeLeft']
        def iconLeft = vm['iconLeft']
        def iconSizeLeft = vm['iconSizeLeft']
        createIconNode(res, 'leftIcon', selectOrTypeLeft, iconLibTypeLeft, iconLeft, iconSizeLeft)
        modifiableValueMap.remove('selectOrTypeLeft')
        modifiableValueMap.remove('iconLibTypeLeft')
        modifiableValueMap.remove('iconLeft')
        modifiableValueMap.remove('iconSizeLeft')
        println("--------------------------")
    }
    if (vm['iconLeft']) {
        def selectOrTypeRight = vm['selectOrTypeRight']
        def iconLibTypeRight = vm['iconLibTypeRight']
        def iconRight = vm['iconRight']
        def iconSizeRight = vm['iconSizeRight']
        createIconNode(res, 'rightIcon', selectOrTypeRight, iconLibTypeRight, iconRight, iconSizeRight)
        modifiableValueMap.remove('selectOrTypeRight')
        modifiableValueMap.remove('iconLibTypeRight')
        modifiableValueMap.remove('iconRight')
        modifiableValueMap.remove('iconSizeRight')
        println("--------------------------")
    }

}

def createIconNode(resource, nodeName, selectOrType, iconLibType, icon, iconSize) {
    def res = null;
    def properties = ['jcr:primaryType': 'nt:unstructured']
    if (icon) {
        properties = ['jcr:primaryType': 'nt:unstructured', 'selectOrType': selectOrType, 'iconLibType': iconLibType, 'icon': icon, 'iconSize': iconSize]
    }

    if (!resource.getChild(nodeName)) {
        res = resourceResolver.create(resource, nodeName, properties)
        println(" The icon structure of button was changed path: " + resource.path)
        changeCounter++
    }
    return res;
}

rootPaths.each {
    rootPath ->
        findButtonComponents(rootPath).each {
            res ->
                try {
                    processIconChanges(res)
                } catch (Exception e) {
                    println("Error processing resource: " + res.path + " with error: " + e.getMessage())
                }
        }
}

if (dryRun) {
    println("Script in dryRun mode. $changeCounter node changes in the repository.")
} else {
    println("Script in dryRun mode. $changeCounter node changes in the repository.")
    resourceResolver.commit()
}