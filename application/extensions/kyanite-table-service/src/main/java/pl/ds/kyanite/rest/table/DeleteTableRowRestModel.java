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

package pl.ds.kyanite.rest.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

@Model(adaptables = SlingHttpServletRequest.class)
public class DeleteTableRowRestModel {

  @SlingObject
  private Resource cell;

  @SlingObject
  private ResourceResolver resourceResolver;

  private int rowspanStartPosition;
  private int rowspanEndPosition;
  private Resource nextRow;
  private Resource rowParent;

  private List<Resource> rowsToDelete;

  @PostConstruct
  private void init() {
    Resource row = cell.getParent();
    rowParent = row != null ? row.getParent() : null;
    initAttributes(row);
  }

  private void initAttributes(Resource row) {
    rowsToDelete = new ArrayList<>();
    Iterator<Resource> rows = rowParent.listChildren();
    for (int rowCount = 1; rows.hasNext(); rowCount++) {
      Resource r = rows.next();
      if (r.getPath().equals(row.getPath())) {
        rowspanStartPosition = rowCount;
        rowspanEndPosition = rowspanStartPosition + calculateRowspan() - 1;
      }
      if (rowCount == rowspanEndPosition && rows.hasNext()) {
        nextRow = rows.next();
      }
      if (rowCount >= rowspanStartPosition && rowCount <= rowspanEndPosition) {
        rowsToDelete.add(r);
      }
    }
  }

  private int calculateRowspan() {
    Integer rowspan = cell.getValueMap().get("rowspan", Integer.class);
    if (rowspan == null) {
      return 1;
    }
    return rowspan.intValue();
  }

  public int getRowspanStartPosition() {
    return rowspanStartPosition;
  }

  public int getRowspanEndPosition() {
    return rowspanEndPosition;
  }

  public Resource getNextRow() {
    return nextRow;
  }

  public Resource getRowParent() {
    return rowParent;
  }

  public List<Resource> getRowsToDelete() {
    return rowsToDelete;
  }

  public ResourceResolver getResourceResolver() {
    return resourceResolver;
  }

}
