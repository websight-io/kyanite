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

  private static String rowNodeName = "tablerow%d";
  private static final String SLING_RESOURCETYPE = "sling:resourceType";

  @Override
  public RestActionResult<String> perform(AddTableRowRestModel addTableRowRestModel) {

    try {
      Resource tableRow = addTableRowRestModel.getResource();
      Resource parent = tableRow.getParent();
      Node parentNode = parent.adaptTo(Node.class);
      long rowSize = parentNode.getNodes().getSize();

      Node newTableRow = parentNode.addNode(String.format(rowNodeName, rowSize + 1));
      newTableRow.setProperty(SLING_RESOURCETYPE, tableRow.getResourceType());

      Iterator<Resource> iterator = tableRow.listChildren();
      while (iterator.hasNext()) {
        Resource tableCell = iterator.next();
        Node newTableCell = newTableRow.addNode(tableCell.getName());
        newTableCell.setProperty(SLING_RESOURCETYPE, tableCell.getResourceType());
      }

      if (addTableRowRestModel.isInsertBefore()) {
        parentNode.orderBefore(newTableRow.getName(),
            addTableRowRestModel.getResource().getName());
      } else {
        Iterator<Resource> tableRowsIterator = parent.getChildren().iterator();
        while (tableRowsIterator.hasNext()) {
          Resource currentRow = tableRowsIterator.next();
          if (currentRow.getName().equals(addTableRowRestModel.getResource().getName())
              && tableRowsIterator.hasNext()) {
            parentNode.orderBefore(newTableRow.getName(),
                tableRowsIterator.next().getName());
          }
        }
      }
      addTableRowRestModel.getSession().save();
      return RestActionResult.success("Table row created",
          "New table row created at " + newTableRow.getPath(), newTableRow.getPath());
    } catch (RepositoryException e) {
      return RestActionResult.failure("Cannot create row", e.getMessage());
    }
  }
}
