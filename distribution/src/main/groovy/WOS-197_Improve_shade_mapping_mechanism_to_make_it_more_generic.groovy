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

dryRun = false
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
        if (vm['subtitleColor']) {
            def subtitleShadeGrey = vm['subtitleShadeGrey']
            def subtitleShadeBw = vm['subtitleShadeBw']
            def subtitleShadeRest = vm['subtitleShadeRest']
            changeShadeNode(grey, subtitleShadeGrey)
            changeShadeNode(bw, subtitleShadeBw)
            changeShadeNode(rest, subtitleShadeRest)
            println(" The shade structure of subtitle was changed path: " + res.path)
        }
        println(" The shade structure of title was changed path: " + res.path)
        println("--------------------------")
    }
}

def changeShadeNode(resource, shadeValue) {
    def modifiableValueMap = resource.adaptTo(org.apache.sling.api.resource.ModifiableValueMap)
    if (modifiableValueMap && shadeValue) {
        modifiableValueMap.put("subtitleShade", shadeValue)
    }
}

def createShadeNode(resource, nodeName, shadeValue) {
    def properties = ['jcr:primaryType': 'nt:unstructured']
    if (shadeValue) {
        properties = ['jcr:primaryType': 'nt:unstructured', 'shade': shadeValue]
    }
    resourceResolver.create(resource, nodeName, properties)
}

rootPaths.each {
    rootPath ->
        findTitleAndContentComponents(rootPath).each {
            res ->
                processColorShadeChanges(res)
                changeCounter++
        }
}

if (dryRun) {
    println("Script in dryRun mode. $changeCounter node changes in the repository.")
} else {
    println("Script in dryRun mode. $changeCounter node changes in the repository.")
    resourceResolver.commit()
}

