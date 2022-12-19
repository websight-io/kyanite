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

package pl.ds.bulma.components.rest;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;
import pl.ds.bulma.components.configurations.ContactFormConfiguration;
import pl.ds.websight.rest.framework.RestAction;
import pl.ds.websight.rest.framework.RestActionResult;
import pl.ds.websight.rest.framework.annotations.SlingAction;
import pl.ds.websight.rest.framework.annotations.SlingAction.HttpMethod;

@Component
@SlingAction(HttpMethod.GET)
@Designate(ocd = ContactFormConfiguration.class)
public class ReadContactFormConfigRestAction implements RestAction<Void, ContactFormConfiguration> {

  private ContactFormConfiguration config;

  @Activate
  protected void activate(ContactFormConfiguration config) {
    this.config = config;
  }

  @Override
  public RestActionResult<ContactFormConfiguration> perform(Void noModel) {
    return RestActionResult.success(config);
  }

}
