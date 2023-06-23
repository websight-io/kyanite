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

def findTitleAndContentComponents(String rootPath) {
    return resourceResolver.findResources("SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE([$rootPath]) AND" +
            " ([sling:resourceType]='bulma/components/title' OR [sling:resourceType]='bulma/components/content')", "JCR-SQL2")
}

def processColorShadeChanges(Resource res) {
    def vm = res.getValueMap()
    if (vm['color']) {
        def shadeGrey = vm['shadeGrey']
        def shadeBw = vm['shadeBw']
        def shadeRest = vm['shadeRest']
        def grey = createShadeNode(res, 'grey', shadeGrey);
        def bw = createShadeNode(res, 'bw', shadeBw);
        def rest = createShadeNode(res, 'rest', shadeRest);
        def modifiableValueMap = res.adaptTo(org.apache.sling.api.resource.ModifiableValueMap)
        modifiableValueMap.remove('shadeGrey')
        modifiableValueMap.remove('shadeBw')
        modifiableValueMap.remove('shadeRest')
        if (vm['subtitleColor']) {
            def subtitleShadeGrey = vm['subtitleShadeGrey']
            def subtitleShadeBw = vm['subtitleShadeBw']
            def subtitleShadeRest = vm['subtitleShadeRest']
            changeShadeNode(grey, subtitleShadeGrey)
            changeShadeNode(bw, subtitleShadeBw)
            changeShadeNode(rest, subtitleShadeRest)
            modifiableValueMap.remove('subtitleShadeGrey')
            modifiableValueMap.remove('subtitleShadeBw')
            modifiableValueMap.remove('subtitleShadeRest')
        }

        println("--------------------------")
    }
}

def changeShadeNode(resource, shadeValue) {
    if(resource != null) {
        def modifiableValueMap = resource.adaptTo(org.apache.sling.api.resource.ModifiableValueMap)
        if (modifiableValueMap && shadeValue) {
            modifiableValueMap.put("subtitleShade", shadeValue)
            println(" The shade structure of subtitle was changed path: " + resource.path)
            changeCounter++
        }
    }
}

def createShadeNode(resource, nodeName, shadeValue) {
    def res = null;
    def properties = ['jcr:primaryType': 'nt:unstructured']
    if (shadeValue) {
        properties = ['jcr:primaryType': 'nt:unstructured', 'shade': shadeValue]
    }

    if (!resource.getChild(nodeName)) {
        res = resourceResolver.create(resource, nodeName, properties)
        println(" The shade structure of title was changed path: " + resource.path)
        changeCounter++
    }
    return res;
}

rootPaths.each {
    rootPath ->
        findTitleAndContentComponents(rootPath).each {
            res ->
                try {
                    processColorShadeChanges(res)
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

