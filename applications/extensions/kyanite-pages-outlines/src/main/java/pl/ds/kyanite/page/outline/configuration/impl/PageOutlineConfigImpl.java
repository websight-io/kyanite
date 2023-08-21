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

package pl.ds.kyanite.page.outline.configuration.impl;

import lombok.Getter;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import pl.ds.kyanite.page.outline.configuration.PageOutlineConfig;
import pl.ds.kyanite.page.outline.configuration.PageOutlineConfigOcd;

@Component(service = PageOutlineConfig.class)
@Designate(ocd = PageOutlineConfigOcd.class, factory = true)
public class PageOutlineConfigImpl implements PageOutlineConfig {


  @Getter
  private String id;

  @Getter
  private String label;

  @Getter
  private String pageTemplate;

  @Getter
  private String outlineResourceType;

  @Activate
  @Modified
  protected void activate(final PageOutlineConfigOcd config) {
    this.id = config.id();
    this.label = config.label();
    this.pageTemplate = config.page_template();
    this.outlineResourceType = config.outline_resource_type();
  }

}
