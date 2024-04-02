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

package pl.ds.kyanite.common.components.services.impl;

import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import pl.ds.kyanite.common.components.configurations.ContactFormConfigurationOcd;
import pl.ds.kyanite.common.components.services.ContactFormConfiguration;

@Component(service = ContactFormConfiguration.class)
@Designate(ocd = ContactFormConfigurationOcd.class, factory = true)
public class ContactFormConfigurationImpl implements ContactFormConfiguration {

  private ContactFormConfigurationOcd config;

  @Activate
  @Modified
  protected void activate(final ContactFormConfigurationOcd config) {
    this.config = config;
  }

  @Override
  public String getSpaceName() {
    return Objects.nonNull(config.spaceName()) ? config.spaceName() : StringUtils.EMPTY;
  }

  @Override
  public String getConfigEndpoint() {
    return getHost() + getPath();
  }

  private String getHost() {
    if (config.host() == null) {
      return "";
    }

    if (config.host().endsWith("/")) {
      return config.host().substring(0, config.host().length() - 1);
    }

    return config.host();
  }

  private String getPath() {
    if (config.path() == null) {
      return "";
    }

    if (!config.path().startsWith("/")) {
      return "/" + config.path();
    }

    return config.path();
  }

}
