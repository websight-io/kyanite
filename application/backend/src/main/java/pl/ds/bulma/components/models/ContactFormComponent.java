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

package pl.ds.bulma.components.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import pl.ds.bulma.components.services.ContactFormConfigurationService;
import pl.ds.bulma.components.services.RecaptchaConfigurationService;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContactFormComponent {

  @OSGiService
  private ContactFormConfigurationService contactFormConfigurationService;
  @OSGiService
  private RecaptchaConfigurationService recaptchaConfigurationService;

  @Inject
  private List<TypeOfInquiry> types = new ArrayList<>();

  @ValueMapValue
  @Getter
  @Default(values = "Send")
  private String submitLabel;

  @ValueMapValue
  @Getter
  @Default(values = "<p>Contact Us</p>")
  private String consentText;

  public String getCaptchaPublicKey() {
    return recaptchaConfigurationService.getCaptchaPublicKey();
  }

  public Map<String, String> getTypeOfInquiryValue() {
    String value = """
              {"subject":"%s",
               "email":"%s"}""";
    return types.stream().collect(Collectors.toMap(
        TypeOfInquiry::getLabel,
        elem -> value.formatted(elem.getLabel(), elem.getEmail())));
  }

  public String getConfigEndpoint() {
    return contactFormConfigurationService.getConfigEndpoint();
  }

}
