/*
 * Copyright (C) 2022 Dynamic Solutions
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

package pl.ds.bulma.components.services;


import javax.inject.Inject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;
import pl.ds.bulma.components.configurations.ContactFormConfiguration;

@Component(service = ContactFormConfigurationService.class)
@Designate(ocd = ContactFormConfiguration.class)
public class ContactFormConfigurationService {

  @Inject
  private ContactFormConfiguration config;

  public String getConfigEndpoint() {
    return config.getEndpoint();
  }

  @Activate
  protected void activate(ContactFormConfiguration config) {
    this.config = config;
  }
}
