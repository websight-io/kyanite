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
 * This script changes the content structure of the button components
 * what has two bunch for icons properties (right and left)
 * For every bunch properties will be created as separate nodes
 * and old properties will be removed.
 */

dryRun = true
rootPaths = ['/content']
changeCounter = 0

RT_BLOG_ARTICLE_PAGE = 'kyanite/blogs/components/blogarticlepage'

def findAuthorInfoComponents(String rootPath) {
    return resourceResolver.findResources(
            " SELECT * " +
            " FROM [nt:base] AS s " +
            " WHERE ISDESCENDANTNODE([$rootPath]) " +
            "   AND (   [sling:resourceType]='kyanite/blogs/components/blogarticleheader'       " +
            "        OR [sling:resourceType]='kyanite/blogs/components/blogarticleauthorbio'    " +
            "        OR [sling:resourceType]='${RT_BLOG_ARTICLE_PAGE}'    " +
            "       ) "
        , "JCR-SQL2")
}

def addPropertyIfNotExist(resource, propName, propValue) {
    if(resource != null) {
        def modifiableValueMap = resource.adaptTo(org.apache.sling.api.resource.ModifiableValueMap)
        if (modifiableValueMap) {
            def curVal = modifiableValueMap.get(propName)
            if (curVal == null) {
                modifiableValueMap.put(propName, propValue)
                println("VALUE SET: " + propName + "=" + propValue + "  in " + resource.path)
            } else {
                println("VALUE EXISTS: " + propName + "=" + curVal + "  in " + resource.path)
            }
        }
    }
}

def migrateAuthorInfoComponent(component) {
    addPropertyIfNotExist(
            component,
            'authorInfoSource',
            component.resourceType == RT_BLOG_ARTICLE_PAGE ? 'ownProperties' : 'parentPage')
}

rootPaths.each {
    rootPath ->
        findAuthorInfoComponents(rootPath).each {
            res ->
                try {
                    migrateAuthorInfoComponent(res)
                    changeCounter++
                } catch (Exception e) {
                    println("Error processing resource: " + res.path + " with error: " + e.getMessage())
                }
        }
}

if (dryRun) {
    println("Script in dryRun mode. $changeCounter node changes in the repository.")
} else {
    println("APPLIED $changeCounter node changes in the repository.")
    resourceResolver.commit()
}
