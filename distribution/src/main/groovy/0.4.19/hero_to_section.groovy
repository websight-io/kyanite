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
 * This script changes the content structure of the link component
 * what has two bunch for icons properties (right and left)
 * For every bunch properties will be created as separate nodes
 * and old properties will be removed.
 */

dryRun = true
final PN_HERO_SWITCH = "renderAsHero"
rootPaths = ['/content']
updatedSectionsCount = 0
convertedHeroesCount = 0

def findSectionComponents(String rootPath) {
    return resourceResolver.findResources("SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE([$rootPath]) AND" +
            "[sling:resourceType]='kyanite/common/components/section'", "JCR-SQL2")
}

def findHeroComponents(String rootPath) {
    return resourceResolver.findResources("SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE([$rootPath]) AND" +
            "[sling:resourceType]='kyanite/common/components/hero'", "JCR-SQL2")
}

def hasProperty(Resource res, String propertyName) {
    res.getValueMap().containsKey(propertyName)
}

def setProperty(Resource res, String propertyName, String propertyValue) {
    def vm = res.getValueMap()
    def modifiableValueMap = res.adaptTo(org.apache.sling.api.resource.ModifiableValueMap)
    modifiableValueMap.put(propertyName, propertyValue)
}

rootPaths.each {
    rootPath ->
        findSectionComponents(rootPath).each {
            res ->
                try {
                    // only update 'Section' components if they don't have the new property,
                    // because subsequent executions of this script would also reconfigure 'Section' components which were a 'Hero' before
                    if (!hasProperty(res, PN_HERO_SWITCH)) {
                        setProperty(res, PN_HERO_SWITCH, "false")
                        updatedSectionsCount++
                    }
                } catch (Exception e) {
                    println("Error processing resource: " + res.path + " with error: " + e.getMessage())
                }
        }

        findHeroComponents(rootPath).each {
            res ->
                try {
                    setProperty(res, "sling:resourceType", "kyanite/common/components/section")
                    setProperty(res, PN_HERO_SWITCH, "true")
                    convertedHeroesCount++
                } catch (Exception e) {
                    println("Error processing resource: " + res.path + " with error: " + e.getMessage())
                }
        }
}

if (dryRun) {
    println("Script in dryRun mode. Updated $updatedSectionsCount 'Section' component(s). Converted $convertedHeroesCount 'Hero' component(s) to 'Section' component(s).")
} else {
    println("Updated $updatedSectionsCount 'Section' component(s). Converted $convertedHeroesCount 'Hero' component(s) to 'Section' component(s).")
    resourceResolver.commit()
}