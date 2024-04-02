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

@ObjectClassDefinition(name = "Google Analytics Configuration",
    description = "Google Analytics Google")
public @interface GoogleAnalyticsConfigurationOcd {

  @AttributeDefinition(
      name = "Space name",
      description = "Enter space name for Google Analytics",
      type = AttributeType.STRING)
  String spaceName() default StringUtils.EMPTY;

  @AttributeDefinition(name = "Google Analytics ID",
      description = "Enter a unique tracking ID provided by Google Analytics")
  String gaTrackingId() default StringUtils.EMPTY;

  @AttributeDefinition(name = "Google Analytics url",
      description = "Enter url provided by Google Analytics")
  String url() default StringUtils.EMPTY;

  @AttributeDefinition(name = "Google Analytics script url",
      description = "Enter script url provided by Google Analytics")
  String scriptUrl() default StringUtils.EMPTY;

}
