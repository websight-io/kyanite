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

package pl.ds.kyanite.common.components.configurations;

import org.apache.commons.lang3.StringUtils;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Icon library - OSGi Factory Configuration")
public @interface IconLibraryFactoryConfigiguration {

  @AttributeDefinition(
      name = "Library name",
      description = "Enter library label for dropdown.",
      type = AttributeType.STRING)
  String label() default StringUtils.EMPTY;

  @AttributeDefinition(
      name = "Library ID",
      description = "Enter library value for dropdown.",
      type = AttributeType.STRING)
  String id() default StringUtils.EMPTY;

  @AttributeDefinition(
      name = "Library URL",
      description = "Add Library URL.",
      type = AttributeType.STRING
  )
  String libraryUrl() default StringUtils.EMPTY;

  @AttributeDefinition(
      name = "Library attributes",
      description = "Add attributes.",
      type = AttributeType.STRING
  )
  String[] attributes() default {};
}