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

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import lombok.Getter;
import org.apache.sling.api.resource.ResourceResolver;

public class CellInfo {

  private static final String COLSPAN_CELL_PROPERTY = "colspan";

  @Getter
  private int startAbsolutePosition;

  @Getter
  private int endAbsolutePosition;

  @Getter
  private final String name;

  @Getter
  private final String resourceType;

  public CellInfo(Node selectedCellNode) throws RepositoryException {
    this.resourceType = selectedCellNode.getProperty(ResourceResolver.PROPERTY_RESOURCE_TYPE)
        .getString();
    this.name = selectedCellNode.getName();
    int colspan = getColspan(selectedCellNode);
    int position = 0;
    NodeIterator siblings = selectedCellNode.getParent().getNodes();
    while (siblings.hasNext()) {
      Node nextCell = siblings.nextNode();
      if (name.equals(nextCell.getName())) {
        this.startAbsolutePosition = position + 1;
        this.endAbsolutePosition = position + colspan;
        break;
      } else {
        position += getColspan(nextCell);
      }
    }
  }

  private int getColspan(Node cellNode) throws RepositoryException {
    return cellNode.hasProperty(COLSPAN_CELL_PROPERTY)
        ? Integer.parseInt(cellNode.getProperty(COLSPAN_CELL_PROPERTY).getString()) : 1;
  }
}
