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

package pl.ds.kyanite.common.components.utils;

import java.util.Collections;
import java.util.List;
import java.util.stream.StreamSupport;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;

public class ContainerComponentUtil {

  public static List<Resource> listVisibleChildren(final Resource resource) {
    if (resource == null) {
      return Collections.emptyList();
    } else {
      return StreamSupport.stream(resource.getChildren().spliterator(), false)
          //E.g. Image is defined on dialog level and should not be displayed as separate component
          .filter(res -> !StringUtils.equals("nt:unstructured", res.getResourceType()))
          .toList();
    }
  }

  private ContainerComponentUtil() {
    // no instance
  }
}

