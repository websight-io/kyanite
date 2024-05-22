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
 * This script changes property names of existing Table Cell component instances.
 * It changes colspan to columns, and rowspan to rows.
 */

import org.apache.sling.api.resource.ModifiableValueMap
import org.apache.sling.api.resource.Resource

dryRun = true
rootPath = '/content'
colspanProperty = 'colspan'
columnsProperty = 'columns'
rowspanProperty = 'rowspan'
rowsProperty = 'rows'
propertiesChanged = 0

def findTableCellComponents() {
    return resourceResolver.findResources("SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE([$rootPath]) AND" +
            "[sling:resourceType]='kyanite/common/components/table/tablecell'", "JCR-SQL2")
}

def renameColspanAndRowspanProperties(Resource res) {
    def valueMap = res.getValueMap()
    def colspan = valueMap.get(colspanProperty)
    def rowspan = valueMap.get(rowspanProperty)
    if (colspan != null || rowspan != null) {
        def modifiableValueMap = res.adaptTo(ModifiableValueMap)
        if (colspan != null) {
            replaceKey(modifiableValueMap, colspan, colspanProperty, columnsProperty)
            propertiesChanged++
        }
        if (rowspan != null) {
            replaceKey(modifiableValueMap, rowspan, rowspanProperty, rowsProperty)
            propertiesChanged++
        }
    }
}

def replaceKey(ModifiableValueMap map, Object value, String oldKey, String newKey) {
    map.put(newKey, value)
    map.remove(oldKey)
}

findTableCellComponents().each {
    try {
        renameColspanAndRowspanProperties(it)
    } catch (Exception e) {
        println("Error processing resource: " + it.path + " with error: " + e.getMessage())
    }
}

if (dryRun) {
    println("Script in dryRun mode. $propertiesChanged properties changed")
} else {
    println("$propertiesChanged properties changed.")
    resourceResolver.commit()
}