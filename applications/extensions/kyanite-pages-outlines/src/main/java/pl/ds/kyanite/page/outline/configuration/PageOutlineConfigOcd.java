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

package pl.ds.kyanite.page.outline.configuration;

import org.apache.commons.lang3.StringUtils;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "OCD for Page Outline Configuration")
public @interface PageOutlineConfigOcd {

  @AttributeDefinition(
      name = "Outline id",
      type = AttributeType.STRING)
  String id() default StringUtils.EMPTY;

  @AttributeDefinition(
      name = "Outline name",
      type = AttributeType.STRING)
  String label() default StringUtils.EMPTY;

  @AttributeDefinition(
      name = "Page template",
      description = "Page template for which outline is available",
      type = AttributeType.STRING
  )
  String page_template() default StringUtils.EMPTY;

  @AttributeDefinition(
      name = "Outline resource type",
      description = "Outline resource type",
      type = AttributeType.STRING
  )
  String outline_resource_type() default StringUtils.EMPTY;

}