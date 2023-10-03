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

dryRun = true
rootPaths = ['/content']
imageSourceTypeCount = 0
assetReferenceCount = 0

def findImageComponents(String rootPath) {
    return resourceResolver.findResources("SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE([$rootPath]) AND" +
            "[sling:resourceType]='kyanite/common/components/image'", "JCR-SQL2")
}

def findNavbarItemsComponents(String rootPath) {
    return resourceResolver.findResources("SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE([$rootPath]) AND" +
            "[sling:resourceType]='kyanite/common/components/navbar/navbaritem'", "JCR-SQL2")
}

def findCardComponents(String rootPath) {
    return resourceResolver.findResources("SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE([$rootPath]) AND" +
            "[sling:resourceType]='kyanite/common/components/card'", "JCR-SQL2")
}

def setImageSourceType(Resource res) {
    def vm = res.getValueMap()
    def modifiableValueMap = res.adaptTo(org.apache.sling.api.resource.ModifiableValueMap)
    if (vm['imageSrcType'] == null) {
        modifiableValueMap.put('imageSrcType', 'asset')
        imageSourceTypeCount++
    }
}

def setImageAssetReference(Resource res) {
    def vm = res.getValueMap()
    def modifiableValueMap = res.adaptTo(org.apache.sling.api.resource.ModifiableValueMap)
    if (vm['assetReference'] == null && vm['src'] != null) {
        modifiableValueMap.put('assetReference', vm['src'])
        assetReferenceCount++
    }
}

def setImageAssetForChildImageResource(Resource res) {
    def imageResource = res.getChild("image")
    if (imageResource != null) {
        setImageAssetReference(imageResource)
    }
}

rootPaths.each {
    rootPath ->
        findImageComponents(rootPath).each {
            res ->
                try {
                    setImageSourceType(res)
                    setImageAssetReference(res)
                } catch (Exception e) {
                    println("Error processing resource: " + res.path + " with error: " + e.getMessage())
                }
        }
        findNavbarItemsComponents(rootPath).each {
            res ->
                try {
                    setImageSourceType(res)
                    setImageAssetForChildImageResource(res)
                } catch (Exception e) {
                    println("Error processing resource: " + res.path + " with error: " + e.getMessage())
                }
        }
        findCardComponents(rootPath).each {
            res ->
                try {
                    setImageSourceType(res)
                    setImageAssetForChildImageResource(res)
                } catch (Exception e) {
                    println("Error processing resource: " + res.path + " with error: " + e.getMessage())
                }
        }
}

if (dryRun) {
    println("Script in dryRun mode. $imageSourceTypeCount image source types set. $assetReferenceCount image sources set.")
} else {
    println("$imageSourceTypeCount image source types set. $assetReferenceCount image sources set.")
    resourceResolver.commit()
}