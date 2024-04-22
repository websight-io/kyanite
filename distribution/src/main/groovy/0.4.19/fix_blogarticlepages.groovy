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
 * This script handles the already existing instances of blogarticlepages
 * after the changes in KYAN-223. In these changes, the majority of
 * components in the template becomes fixed to prevent deleting them
 * and thus breaking the page. Also fixes the structure of already
 * broken instances.
 */

dryRun = true
rootPaths = ['/content']
resourceTypeProperty = 'sling:resourceType'
fixedSectionPath = 'kyanite/common/components/section/fixedsection'
fixedContainerPath = 'kyanite/common/components/container/fixedcontainer'
fixedColumnsPath = 'kyanite/common/components/columns/fixedcolumns'
fixedColumnPath = 'kyanite/common/components/columns/column/fixedcolumn'
newColumnProperties = ['jcr:primaryType': 'nt:unstructured', 'sling:resourceType': fixedColumnPath, 'customClass': 'sticky-container']
backButtonProperties = ['jcr:primaryType': 'nt:unstructured', 'sling:resourceType': 'kyanite/blogs/components/blogpostbackbutton', 'isHidden': 'true']
tableOfContentsProperties = ['jcr:primaryType': 'nt:unstructured', 'sling:resourceType': 'kyanite/blogs/components/blogposttableofcontents', 'maxHeadingLevel': '2', 'isHidden': 'true']
updatedPagesCount = 0

def findBlogArticlePageInstances(String rootPath) {
    return resourceResolver.findResources("SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE([$rootPath]) " +
            "AND [sling:resourceType]='kyanite/blogs/components/blogarticlepage'", "JCR-SQL2")
}

def hasChild(Resource parentResource, String childName) {
    parentResource.getChild(childName) != null
}

def createColumnWithContent(Resource columnsResource, String columnName, LinkedHashMap contentProperties, String contentNodeName, String followingColumnName) {
    resourceResolver.create(columnsResource, columnName, newColumnProperties)
    def newColumnResource = columnsResource.getChild(columnName)
    resourceResolver.create(newColumnResource, contentNodeName, contentProperties)
    def columnsNode = columnsResource.adaptTo(javax.jcr.Node)
    columnsNode.orderBefore(columnName, followingColumnName)
}

def modifyResourceTypes(Resource parentResource, String childResourceName, String newResourceType) {
    def childResource = parentResource.getChild(childResourceName)
    childResource.adaptTo(org.apache.sling.api.resource.Resource)
    def childResourceMVM = childResource.adaptTo(org.apache.sling.api.resource.ModifiableValueMap)
    childResourceMVM.replace(resourceTypeProperty, newResourceType)
    return childResource
}

def addAndModifyContent(Resource resource) {
    def pageContainerResource = resource.getChild('pagecontainer')
    def sectionResource = modifyResourceTypes(pageContainerResource, 'section', fixedSectionPath)
    def containerResource = modifyResourceTypes(sectionResource, 'container', fixedContainerPath)
    def columnsResource = modifyResourceTypes(containerResource, 'columns1', fixedColumnsPath)

    if(hasChild(columnsResource, 'column1')) {
        def column1Resource = modifyResourceTypes(columnsResource, 'column1', fixedColumnPath)
        if (!hasChild(column1Resource, 'backbutton')) {
            def columnNode = column1Resource.adaptTo(javax.jcr.Node)
            columnNode.remove()
            createColumnWithContent(columnsResource, 'column1', backButtonProperties, 'backbutton', 'column2')
        }
    } else {
        createColumnWithContent(columnsResource, 'column1', backButtonProperties, 'backbutton', 'column2')
    }

    modifyResourceTypes(columnsResource, 'column2', fixedColumnPath)

    if(hasChild(columnsResource, 'column3')) {
        def column3Resource = modifyResourceTypes(columnsResource, 'column3', fixedColumnPath)
        if (!hasChild(column3Resource, 'blogposttableofcontents')) {
            def columnNode = column3Resource.adaptTo(javax.jcr.Node)
            columnNode.remove()
            createColumnWithContent(columnsResource, 'column3', tableOfContentsProperties, 'blogposttableofcontents', 'column4')
        }
    } else {
        createColumnWithContent(columnsResource, 'column3', tableOfContentsProperties, 'blogposttableofcontents', 'column4')
    }
}

rootPaths.each {
    rootPath ->
        findBlogArticlePageInstances(rootPath).each {
            res ->
                try {
                    addAndModifyContent(res)
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