/*
 * Copyright (C) 2022 Dynamic Solutions
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


package pl.ds.bulma.rest.table;

import java.util.Iterator;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import org.apache.sling.api.resource.Resource;
import org.osgi.service.component.annotations.Component;
import pl.ds.websight.rest.framework.RestAction;
import pl.ds.websight.rest.framework.RestActionResult;
import pl.ds.websight.rest.framework.annotations.PrimaryTypes;
import pl.ds.websight.rest.framework.annotations.SlingAction;

@Component
@SlingAction
@PrimaryTypes("nt:base")
public class AddTableColumnRestAction implements RestAction<AddTableColumnRestModel, String> {

  private static final String SLING_RESOURCETYPE = "sling:resourceType";

  @Override
  public RestActionResult<String> perform(AddTableColumnRestModel addTableColumnRestModel) {

    Resource resource = addTableColumnRestModel.getResource();
    Resource tableRow = resource.getParent();
    Resource tableSection = tableRow.getParent();
    Resource table = tableSection.getParent();

    Iterator<Resource> cells = tableRow.listChildren();
    int pos = 0;
    while (cells.hasNext()) {
      Resource cell = cells.next();
      pos++;
      if (cell.getName().equals(resource.getName())) {
        break;
      }
    }

    Iterator<Resource> sections = table.listChildren();
    while (sections.hasNext()) {
      Resource section = sections.next();
      Iterator<Resource> rows = section.listChildren();
      while (rows.hasNext()) {
        Resource row = rows.next();
        Node rowNode = row.adaptTo(Node.class);
        try {
          Node newCell = rowNode.addNode("mytablecell");
          newCell.setProperty(SLING_RESOURCETYPE, resource.getResourceType());

          Iterator<Resource> rowCells = row.listChildren();
          Resource selectedCell = null;
          int cursor = 0;
          while (rowCells.hasNext() && cursor < pos) {
            selectedCell = rowCells.next();
            cursor++;
          }

          //if (addTableColumnRestModel.isInsertBefore()) {
          row.adaptTo(Node.class).orderBefore(newCell.getName(), selectedCell.getName());
          //}

        } catch (RepositoryException e) {
          return RestActionResult.failure("Cannot create column");
        }
      }
    }
    try {
      addTableColumnRestModel.getSession().save();
    } catch (RepositoryException e) {
      return RestActionResult.failure("Cannot create column");
    }
    return RestActionResult.success("Created row");
  }
}
