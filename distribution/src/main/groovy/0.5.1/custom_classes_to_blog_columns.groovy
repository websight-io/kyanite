dryRun = true
rootPaths = ['/content']
newPropertyName = 'customClass'
newPropertyValue = 'blogArticleContent'
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