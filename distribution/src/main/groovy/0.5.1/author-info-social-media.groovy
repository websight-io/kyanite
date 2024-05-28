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

rootPaths = ['/content']
dryRun = true
pagesCounter = 0
nodesCreatedCounter = 0
propertiesChangedCounter = 0

resourceTypeProperty = "sling:resourceType"
blogArticlePageRT = 'kyanite/blogs/components/blogarticlepage'
authorNodeName = 'author'
socialMediaNodeName = 'socialMedia'

authorNodePageProperties = [
        'jcr:primaryType' : 'nt:unstructured',
        'authorInfoSource': 'ownProperties'
]
socialMediaProperties = [
        'jcr:primaryType'   : 'nt:unstructured',
        'sling:resourceType': 'kyanite/blogs/components/blogarticleauthorbio/socialmedia'
]


def findRequiredAuthorNodeBearers(String rootPath) {
    return resourceResolver.findResources(
            " SELECT * " +
                    " FROM [nt:base] AS s " +
                    " WHERE ISDESCENDANTNODE([$rootPath]) " +
                    "   AND (   [$resourceTypeProperty]='$blogArticlePageRT' " +
                    "       ) "
            , "JCR-SQL2")
}

def createNode(Resource node, String nodeName, properties) {
    println("Creating new node $nodeName under $node.path with properties $properties")
    def newNode = resourceResolver.create(node, nodeName, properties)
    nodesCreatedCounter++
    return newNode
}

def getValueMap(node) {
    return node.adaptTo(org.apache.sling.api.resource.ModifiableValueMap)
}

def modifyProperty(node, String propName, newValue) {
    def mvm = getValueMap(node)
    println("Setting $node.path[$propName] = '$newValue'")
    if (mvm.get(propName)) {
        mvm.replace(propName, newValue)
    } else {
        mvm.put(propName, newValue);
    }
    propertiesChangedCounter++
}

def migrateAuthorNodeBearer(Resource wrappingComponent) {
    Resource author = wrappingComponent.getChild(authorNodeName)
    author = author ?: createNode(wrappingComponent, authorNodeName, authorNodePageProperties)
    if (author.getChild(socialMediaNodeName) == null) {
        createNode(author, socialMediaNodeName, socialMediaProperties)
    }
}

rootPaths.each {
    rootPath ->
        findRequiredAuthorNodeBearers(rootPath).each {
            res ->
                try {
                    migrateAuthorNodeBearer(res)
                    pagesCounter++
                } catch (Exception e) {
                    println("Error processing resource: $res.path with error: $e")
                }
        }
}

println("${dryRun ? 'WOULD BE APPLIED' : 'APPLIED ARE'}" +
        ": $pagesCounter pages processed" +
        ", $nodesCreatedCounter nodes created" +
        ", $propertiesChangedCounter properties changed.")
if (!dryRun) {
    resourceResolver.commit()
}
