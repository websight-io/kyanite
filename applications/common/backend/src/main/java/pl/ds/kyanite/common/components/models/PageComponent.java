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

package pl.ds.kyanite.common.components.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.kyanite.common.components.utils.PageUtil;

@Model(adaptables = Resource.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PageComponent {

  @SlingObject
  private Resource resource;

  public String getPageName() {
    return PageUtil.getPageProperty(resource, "jcr:title")
        .replaceAll("[^A-Za-z0-9-_]", "-")
        .toLowerCase();
  }

  public String getTemplateName() {
    String pageProperty = PageUtil.getPageProperty(resource, "ws:template");
    return pageProperty == null ? ""
        : pageProperty.substring(pageProperty.lastIndexOf("/") + 1);
  }
}
