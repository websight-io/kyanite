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

/*
 * This script changes the content structure of the button components
 * what has two bunch for icons properties (right and left)
 * For every bunch properties will be created as separate nodes
 * and old properties will be removed.
 */

dryRun = true
rootPaths = ['/content']
newPropertyName = 'customClass'
newPropertyValue = 'previewImageContainer'
contentColumnPath = 'pagecontainer/section/container/columns1/column4'
updatedPagesCount = 0

def findBlogArticlePageInstances(String rootPath) {
    return resourceResolver.findResources("SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE([$rootPath]) " +
            "AND ([sling:resourceType]='kyanite/blogs/components/blogarticlepage' " +
            "OR [sling:resourceType]='streamx-dev/components/blogarticlepage')", "JCR-SQL2")
}

def addNewProperty(Resource resource) {
    def contentColumnResource = resource.getChild(contentColumnPath)
    def resourceMVM = contentColumnResource.adaptTo(org.apache.sling.api.resource.ModifiableValueMap)
    resourceMVM.put(newPropertyName, newPropertyValue)
}

rootPaths.each {
    rootPath ->
        findBlogArticlePageInstances(rootPath).each {
            res ->
                try {
                    addNewProperty(res)
                    updatedPagesCount++
                } catch (Exception e) {
                    println("Error processing resource: " + res.path + " with error: " + e.getMessage())
                }
        }
}

if (dryRun) {
    println("Script in dryRun mode. Updated $updatedPagesCount 'BlogArticlePage' instance(s).")
} else {
    println("Updated $updatedPagesCount 'BlogArticlePage' instance(s).")
    resourceResolver.commit()
}