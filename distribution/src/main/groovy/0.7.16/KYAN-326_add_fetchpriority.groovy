/*
 * Copyright (C) 2025 Dynamic Solutions
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
 * This script handles the already existing instances of images, sections and card
 * after the changes in KYAN-326. In these changes, a new attribute
 * was added to provide authors the option to prioritize image loading
 * on pages. This script adds the default value of 'auto' to already
 * existing instances where it is relevant.
 */

dryRun = true
rootPaths = ['/content']
sectionPath = 'kyanite/common/components/section'
imagePath = 'kyanite/common/components/image'
cardPath = 'kyanite/common/components/card'
fetchPriorityAttribute = 'fetchPriority'
updatedInstancesCount = 0

def findInstances(String rootPath) {
    return resourceResolver.findResources("SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE([$rootPath]) " +
            "AND ([sling:resourceType]='$sectionPath' " +
            "OR [sling:resourceType]='$imagePath' OR [sling:resourceType]='$cardPath')", "JCR-SQL2")
}

def addAttribute(Resource resource) {
    def resourceMap = resource.adaptTo(org.apache.sling.api.resource.ModifiableValueMap)
    if (!resourceMap.containsKey(fetchPriorityAttribute)) {
        resourceMap.put(fetchPriorityAttribute, 'auto')
        updatedInstancesCount++
    }
}

rootPaths.each {
    rootPath ->
        findInstances(rootPath).each {
            res ->
                try {
                    addAttribute(res)
                } catch (Exception e) {
                    println("Error processing resource: " + res.path + " with error: " + e.getMessage())
                }
        }
}

if (dryRun) {
    println("Script in dryRun mode. Updated $updatedInstancesCount instance(s).")
} else {
    println("Updated $updatedInstancesCount instance(s).")
    resourceResolver.commit()
}