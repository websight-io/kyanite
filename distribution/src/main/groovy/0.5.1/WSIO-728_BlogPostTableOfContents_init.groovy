import javax.swing.Spring

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

dryRun = true
rootPaths = ['/content']
componentsChangeCounter = 0
propertiesChangeCounter = 0


def findComponents(String rootPath) {
    return resourceResolver.findResources(
            " SELECT * " +
            " FROM [nt:base] AS s " +
            " WHERE ISDESCENDANTNODE([$rootPath]) " +
            "   AND [sling:resourceType]='kyanite/blogs/components/blogposttableofcontents'"
            , "JCR-SQL2")
}

def getValueMap(res) {
    return res.adaptTo(org.apache.sling.api.resource.ModifiableValueMap)
}

def initProperty(node, propName, value) {

    //  get target property
    def values = getValueMap(node)
    def curValue = values.get(propName)

    //  set or replace property
    if (!curValue) {
        values.put(propName, value)
        propertiesChangeCounter++
        println("init $propName for ${node.getPath()} with value $value")
    }
}

def updateTableOfContentsComponent(component) {
    println("processing ${component.getPath()}")
    initProperty(component, "maxHeadingLevel", 2)
}

rootPaths.each {
    rootPath ->
        findComponents(rootPath).each {
            res ->
                try {
                    updateTableOfContentsComponent(res)
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
