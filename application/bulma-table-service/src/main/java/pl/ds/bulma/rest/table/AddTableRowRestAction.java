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
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.osgi.service.component.annotations.Component;
import pl.ds.websight.rest.framework.RestAction;
import pl.ds.websight.rest.framework.RestActionResult;
import pl.ds.websight.rest.framework.annotations.PrimaryTypes;
import pl.ds.websight.rest.framework.annotations.SlingAction;

@Component
@SlingAction
@PrimaryTypes("nt:base")
public class AddTableRowRestAction implements RestAction<AddTableRowRestModel, String> {

  private static final String ROW_IDENTIFIER = "tablerow";

  @Override
  public RestActionResult<String> perform(AddTableRowRestModel addTableRowRestModel) {
    try {
      Resource selectedRow = addTableRowRestModel.getSelectedRow();
      Resource parent = selectedRow.getParent();
      Node parentNode = parent.adaptTo(Node.class);

      // create and add new row to the end of the table section
      Node newRow = parentNode.addNode(ResourceUtil.createUniqueChildName(parent, ROW_IDENTIFIER));
      newRow.setProperty(ResourceResolver.PROPERTY_RESOURCE_TYPE, selectedRow.getResourceType());

      // add table cells to new row
      for (Resource cell : selectedRow.getChildren()) {
        Node newCell = newRow.addNode(cell.getName());
        newCell.setProperty(ResourceResolver.PROPERTY_RESOURCE_TYPE, cell.getResourceType());
      }

      // move new row before or after the selected row
      if (addTableRowRestModel.isInsertBefore()) {
        parentNode.orderBefore(newRow.getName(),
            selectedRow.getName());
      } else {
        parentNode.orderBefore(newRow.getName(), nextSiblingName(selectedRow));
      }
      addTableRowRestModel.getSession().save();
      return RestActionResult.success("Table row created",
          "New table row created at " + newRow.getPath(), newRow.getPath());
    } catch (RepositoryException | PersistenceException e) {
      return RestActionResult.failure("Cannot create row", e.getMessage());
    }
  }

  private String nextSiblingName(Resource resource) {
    Iterator<Resource> siblings = resource.getParent().getChildren().iterator();
    while (siblings.hasNext()) {
      if (siblings.next().getName().equals(resource.getName())) {
        return siblings.hasNext() ? siblings.next().getName() : null;
      }
    }
    return null;
  }
}
