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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.ds.websight.rest.framework.RestAction;
import pl.ds.websight.rest.framework.RestActionResult;
import pl.ds.websight.rest.framework.annotations.PrimaryTypes;
import pl.ds.websight.rest.framework.annotations.SlingAction;

@Component
@SlingAction
@PrimaryTypes("nt:base")
public class AddTableRowRestAction implements RestAction<AddTableRowRestModel, String> {

  private static final Logger LOG = LoggerFactory.getLogger(AddTableRowRestAction.class);

  private static final String ROW_IDENTIFIER = "tablerow";

  @Override
  public RestActionResult<String> perform(AddTableRowRestModel addTableRowRestModel) {
    try {
      Resource selectedRow = addTableRowRestModel.getSelectedRow();
      Resource parent = selectedRow.getParent();

      Node parentNode = parent.adaptTo(Node.class);
      if (parentNode == null) {
        throw new RepositoryException("Failed to adapt parent resource to node");
      }

      String newRowName = ResourceUtil.createUniqueChildName(parent, ROW_IDENTIFIER);
      Node newRow = addRow(parentNode, newRowName, selectedRow.getResourceType());

      addCellsToRow(selectedRow, newRow);

      orderRow(parentNode, newRow, selectedRow, addTableRowRestModel.isInsertBefore());

      addTableRowRestModel.getResourceResolver().commit();
      return RestActionResult.success("Table row created");
    } catch (RepositoryException | PersistenceException e) {
      LOG.error("Failed to create row", e);
      return RestActionResult.failure("Cannot create row",
          "An error occurred during adding the row");
    }
  }

  private Node addRow(Node parent, String rowName, String resourceType) throws RepositoryException {
    Node row = parent.addNode(rowName);
    row.setProperty(ResourceResolver.PROPERTY_RESOURCE_TYPE, resourceType);
    return row;
  }

  private void addCellsToRow(Resource selectedRow, Node row) throws RepositoryException {
    for (Resource cell : selectedRow.getChildren()) {
      Node newCell = row.addNode(cell.getName());
      newCell.setProperty(ResourceResolver.PROPERTY_RESOURCE_TYPE, cell.getResourceType());
    }
  }

  private void orderRow(Node parent, Node newRow, Resource selectedRow, boolean isInsertBefore)
      throws RepositoryException {
    if (isInsertBefore) {
      parent.orderBefore(newRow.getName(),
          selectedRow.getName());
    } else {
      parent.orderBefore(newRow.getName(), nextSiblingName(selectedRow));
    }
  }

  private String nextSiblingName(Resource resource) {
    if (resource.getParent() == null) {
      return null;
    }
    Iterator<Resource> siblings = resource.getParent().getChildren().iterator();
    while (siblings.hasNext()) {
      if (siblings.next().getName().equals(resource.getName())) {
        return siblings.hasNext() ? siblings.next().getName() : null;
      }
    }
    return null;
  }
}
