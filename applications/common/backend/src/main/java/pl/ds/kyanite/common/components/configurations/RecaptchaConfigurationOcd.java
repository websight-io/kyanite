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

@ObjectClassDefinition(name = "Recaptcha Configuration",
    description = "Reads the data for recaptcha config")
public @interface RecaptchaConfigurationOcd {

  @AttributeDefinition(
      name = "Space name",
      description = "Enter space name for the recaptcha",
      type = AttributeType.STRING)
  String spaceName() default StringUtils.EMPTY;

  @AttributeDefinition(name = "Recaptcha public key",
      description = "Enter the recaptcha public key to be used for the google recaptcha service")
  String captchaPublicKey();
}
