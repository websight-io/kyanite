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

import javax.jcr.Session

dryRun = true
rootPaths = ['/content']
propertiesChanged = 0

def findTitleComponents(String rootPath) {
    return resourceResolver.findResources("SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE([$rootPath]) AND" +
            "[sling:resourceType]='kyanite/common/components/title'", "JCR-SQL2")
}

def findContentComponents(String rootPath) {
    return resourceResolver.findResources("SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE([$rootPath]) AND" +
            "[sling:resourceType]='kyanite/common/components/content'", "JCR-SQL2")
}

def findTabComponents(String rootPath) {
    return resourceResolver.findResources("SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE([$rootPath]) AND" +
            "[sling:resourceType]='kyanite/common/components/tabs/tab'", "JCR-SQL2")
}

def changeColorProperty(Resource res, String colorProperty) {
    def vm = res.getValueMap()
    def color = vm.get(colorProperty)
    if (color != null) {
        def newColor = mapColor(color)
        if (newColor != color) {
            def modifiableValueMap = res.adaptTo(org.apache.sling.api.resource.ModifiableValueMap)
            modifiableValueMap.put(colorProperty, newColor)
            println(res.name + " " + colorProperty + " " + color + " changed to " + newColor)
            propertiesChanged++
        }
    }
}

def mapColor(String color) {
    def dashIndex = color.indexOf('_')
    if (dashIndex != -1) {
        return color.substring(dashIndex + 1)
    }
    return color
}

def removeShadeNodes(Resource res) {
    def shade = res.getChild("bw")
    if (shade != null){
        println("Remove " + shade.path)
        resourceResolver.delete(shade)
    }
    shade = res.getChild("rest")
    if (shade != null){
        println("Remove " + shade.path)
        resourceResolver.delete(shade)
    }
    shade = res.getChild("grey")
    if (shade != null){
        println("Remove " + shade.path)
        resourceResolver.delete(shade)
    }

}

def clearDefaultColor(Resource res, String colorProperty, String defaultColorValue) {
    def vm = res.getValueMap()
    def color = vm.get(colorProperty)
    if (color != null && color == defaultColorValue) {
        def modifiableValueMap = res.adaptTo(org.apache.sling.api.resource.ModifiableValueMap)
        modifiableValueMap.remove(colorProperty)
        println(res.name + " " + colorProperty + " " + color + " removed.")
        propertiesChanged++
    }
}

def verifyColor(Resource res, String colorProperty) {
    def vm = res.getValueMap()
    def color = vm.get(colorProperty)
    if (color != null && color != "") {
        if (!color.startsWith("has-text-") || color.substring(9).contains('-')) {
            println("Wrong color value for " + res.name + ": " + color)
        }
    }
}

rootPaths.each {
    rootPath ->
        findTitleComponents(rootPath).each {
            res ->
                try {
                    changeColorProperty(res, "color")
                    changeColorProperty(res, "subtitleColor")
                    clearDefaultColor(res, "color", "has-text-grey")
                    clearDefaultColor(res, "subtitleColor", "has-text-grey")
                    removeShadeNodes(res)
                } catch (Exception e) {
                    println("Error processing resource: " + res.path + " with error: " + e.getMessage())
                }
        }
        findContentComponents(rootPath).each {
            res ->
                try {
                    changeColorProperty(res, "color")
                    clearDefaultColor(res, "color", "has-text-grey")
                    removeShadeNodes(res)
                } catch (Exception e) {
                    println("Error processing resource: " + res.path + " with error: " + e.getMessage())
                }
        }
        findTabComponents(rootPath).each {
            res ->
                try {
                    changeColorProperty(res, "color")
                    clearDefaultColor(res, "color", "has-text-grey")
                    removeShadeNodes(res)
                } catch (Exception e) {
                    println("Error processing resource: " + res.path + " with error: " + e.getMessage())
                }
        }

        println("-------------------------------------")
        println("Verify text colors: ")
        findTitleComponents(rootPath).each {
            res ->
                try {
                    verifyColor(res, "color")
                    verifyColor(res, "subtitleColor")
                } catch (Exception e) {
                    println("Error processing resource: " + res.path + " with error: " + e.getMessage())
                }
        }
        findContentComponents(rootPath).each {
            res ->
                try {
                    verifyColor(res, "color")
                } catch (Exception e) {
                    println("Error processing resource: " + res.path + " with error: " + e.getMessage())
                }
        }
        findTabComponents(rootPath).each {
            res ->
                try {
                    verifyColor(res, "color")
                } catch (Exception e) {
                    println("Error processing resource: " + res.path + " with error: " + e.getMessage())
                }
        }
}

if (dryRun) {
    println("Script in dryRun mode. $propertiesChanged properties changed")
} else {
    println("$propertiesChanged properties changed.")
    resourceResolver.commit()
}