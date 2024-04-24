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

package pl.ds.kyanite.common.components.services.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import pl.ds.kyanite.common.components.configurations.RecaptchaConfigurationOcd;
import pl.ds.kyanite.common.components.services.RecaptchaConfiguration;
import pl.ds.kyanite.common.components.services.config.BaseSpaceDependentConfiguration;

@Component(service = RecaptchaConfiguration.class)
@Designate(ocd = RecaptchaConfigurationOcd.class, factory = true)
public class RecaptchaConfigurationImpl
    extends BaseSpaceDependentConfiguration
    implements RecaptchaConfiguration {

  private String captchaPublicKey;

  @Activate
  @Modified
  protected void activate(final RecaptchaConfigurationOcd config) {
    this.spaceName = config.spaceName();
    this.captchaPublicKey = config.captchaPublicKey();
  }

  @Override
  public String getCaptchaPublicKey() {
    return captchaPublicKey;
  }
}
