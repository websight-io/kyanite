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
componentsChangeCounter = 0
propertiesChangeCounter = 0

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

def getOrCreateNode(resource, nodeName) {
    def childNode = resource.getChild(nodeName)
    if (!childNode) {
        def properties = ['jcr:primaryType': 'nt:unstructured']
        res = resourceResolver.create(resource, nodeName, properties)
        println("Created 'author' node under " + resource.path)
        propertiesChangeCounter++
        return res
    } else {
        return childNode
    }
}

def getValueMap(res) {
    return res.adaptTo(org.apache.sling.api.resource.ModifiableValueMap)
}

def removeProperty(node, propName) {
    def values = getValueMap(node)
    values.remove(propName);
}

/* properties migration functions start */
/*
 * MOVE means:
 * 1)   try to move the property in case it doesn't exist in the destination,
 * 2)   REMOVE THE SOURCE PROPERTY (if there was one) in ANY case
 */

def moveOrSetPropertyUnderNewName(srcNode, srcPropName, destNode, destPropName, replace, defaultValue) {

    println("${replace ? 'replace' : 'move'} $srcNode.path[$srcPropName] to $destNode.path[$destPropName] ${defaultValue ? " or set as $defaultValue" : ''}")

    //  get target property
    def values = getValueMap(srcNode)
    def srcValue = values.get(srcPropName)
    def targetValue = srcValue ? srcValue : defaultValue
    def destValues = getValueMap(destNode)
    def destValue = destValues.get(destPropName)

    def msgSrc  = "SRC:  $srcNode.path[$srcPropName]=$srcValue, default=$defaultValue".toString()
    def msgDest = "DEST: $destNode.path[$destPropName]=$destValue".toString()
    def action = "none"

    //  set or replace property
    if (targetValue) {
        if (!destValue || replace) {
            action = !destValue ? "set value" : "replace value"
            destValues.put(destPropName, targetValue)
            propertiesChangeCounter++
        }
    }

    //  remove source property
    if (srcValue) {
        removeProperty(srcNode, srcPropName)
        propertiesChangeCounter++
    }

    println(msgSrc)
    println(msgDest)
    println("ACTION: $action")

}

def moveOrSetPropertyUnderNewName(srcNode, srcPropName, destNode, destPropName, replace) {
    moveOrSetPropertyUnderNewName(srcNode, srcPropName, destNode, destPropName, replace,null)
}

def moveOrSetPropertyUnderNewName(srcNode, srcPropName, destNode, destPropName) {
    moveOrSetPropertyUnderNewName(srcNode, srcPropName, destNode, destPropName, false)
}

def moveOrSetProperty(srcNode, destNode, propName, replace, defaultValue) {
    moveOrSetPropertyUnderNewName(srcNode, propName, destNode, propName, replace, defaultValue)
}

def moveOrSetProperty(srcNode, destNode, propName, replace) {
    moveOrSetProperty(srcNode, destNode, propName, replace, null)
}

def movePropertyIfExists(srcNode, destNode, srcPropName) {
    moveOrSetProperty(srcNode, destNode, srcPropName, false)
}

/* properties migration functions end */

def migrateAuthorInfoComponent(component) {
    def authorNode = getOrCreateNode(component, 'author')
    def infoSourceDefault = component.resourceType == RT_BLOG_ARTICLE_PAGE \
            ? 'ownProperties' \
            : 'parentPage'
    moveOrSetProperty(component, authorNode, 'authorInfoSource', false, infoSourceDefault)
    moveOrSetPropertyUnderNewName(component, 'link', authorNode, 'authorPageLink')
    movePropertyIfExists(component, authorNode, 'authorName')
    movePropertyIfExists(component, authorNode, 'authorPhoto')
    movePropertyIfExists(component, authorNode, 'authorPhotoAlt')
    movePropertyIfExists(component, authorNode, 'authorRole')
    movePropertyIfExists(component, authorNode, 'authorDescription')
}

rootPaths.each {
    rootPath ->
        findAuthorInfoComponents(rootPath).each {
            res ->
                try {
                    migrateAuthorInfoComponent(res)
                    componentsChangeCounter++
                } catch (Exception e) {
                    println("Error processing resource: " + res.path + " with error: " + e.getMessage())
                }
        }
}

if (dryRun) {
    println("Script in dryRun mode. " +
            "$propertiesChangeCounter node changes, " +
            "$componentsChangeCounter node changed in the repository.")
} else {
    println("APPLIED " +
            "$propertiesChangeCounter node changes, " +
            "$componentsChangeCounter node have been changed in the repository.")
    resourceResolver.commit()
}
