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
 * This script changes the content structure of the link component
 * what has two bunch for icons properties (right and left)
 * For every bunch properties will be created as separate nodes
 * and old properties will be removed.
 */

import javax.jcr.Session

dryRun = true
rootPaths = ['/content']
resourcesChangesCount = 0
resorcesMovedCount = 0

def findModelCardComponents(String rootPath) {
    return resourceResolver.findResources("SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE([$rootPath]) AND" +
            "[sling:resourceType]='kyanite/common/components/modelcard'", "JCR-SQL2")
}

def findModelCardFootersComponents(String rootPath) {
    return resourceResolver.findResources("SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE([$rootPath]) AND" +
            "[sling:resourceType]='kyanite/common/components/modelcard/modelcardfooter'", "JCR-SQL2")
}

def findModelCardContentsComponents(String rootPath) {
    return resourceResolver.findResources("SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE([$rootPath]) AND" +
            "[sling:resourceType]='kyanite/common/components/modelcard/modelcardcontent'", "JCR-SQL2")
}

def setResourceType(Resource res, String resourceType) {
    def vm = res.getValueMap()
    def modifiableValueMap = res.adaptTo(org.apache.sling.api.resource.ModifiableValueMap)
    modifiableValueMap.put('sling:resourceType', resourceType)
    resourcesChangesCount++
}

def renameResource(Resource res, String resourceName) {
    def resourceResolver = res.getResourceResolver()
    def session = resourceResolver.adaptTo(Session.class)
    def newResourcePath = res.getPath().replaceAll(res.getName(), resourceName)


    session.move(res.getPath(), newResourcePath)
    resorcesMovedCount++
}

rootPaths.each {
    rootPath ->
        findModelCardContentsComponents(rootPath).each {
            res ->
                try {
                    setResourceType(res, "kyanite/common/components/modalcard/modalcardcontent")
                    renameResource(res, "modalcardcontent")
                } catch (Exception e) {
                    println("Error processing resource: " + res.path + " with error: " + e.getMessage())
                }
        }
        findModelCardFootersComponents(rootPath).each {
            res ->
                try {
                    setResourceType(res, "kyanite/common/components/modalcard/modalcardfooter")
                    renameResource(res, "modalcardfooter")
                } catch (Exception e) {
                    println("Error processing resource: " + res.path + " with error: " + e.getMessage())
                }
        }
        findModelCardComponents(rootPath).each {
            res ->
                try {
                    setResourceType(res, "kyanite/common/components/modalcard")
                    renameResource(res, "modalcard")
                } catch (Exception e) {
                    println("Error processing resource: " + res.path + " with error: " + e.getMessage())
                }
        }
}

if (dryRun) {
    println("Script in dryRun mode. $resourcesChangesCount resource types changed. $resorcesMovedCount resources names changed.")
} else {
    println("$resourcesChangesCount resource types changed. $resorcesMovedCount resources names changed.")
    resourceResolver.commit()
}