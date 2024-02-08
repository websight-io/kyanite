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

package pl.ds.kyanite.common.components.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.json.Json;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import pl.ds.kyanite.common.components.services.ContactFormConfigurationService;
import pl.ds.kyanite.common.components.services.RecaptchaConfigStore;
import pl.ds.kyanite.common.components.services.RecaptchaConfiguration;
import pl.ds.kyanite.common.components.utils.PagesSpaceUtil;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContactFormComponent {

  @SlingObject
  private Resource resource;

  @OSGiService
  private ContactFormConfigurationService contactFormConfigurationService;
  @OSGiService
  private RecaptchaConfigStore recaptchaConfigStore;

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

  private String spaceName;

  @PostConstruct
  private void init() {
    spaceName = PagesSpaceUtil.getWsPagesSpaceName(resource.getPath(),
        resource.getResourceResolver());
  }

  public String getCaptchaPublicKey() {
    RecaptchaConfiguration config = recaptchaConfigStore.get(spaceName);
    if (config != null) {
      return config.getCaptchaPublicKey();
    }
    return null;
  }

  public Map<String, String> getTypeOfInquiryValue() {
    return types.stream().collect(Collectors.toMap(
        TypeOfInquiry::getLabel,
        elem -> Json.createObjectBuilder().add("subject", elem.getLabel())
            .add("email", elem.getEmail())
            .build()
            .toString()));
  }

  public String getConfigEndpoint() {
    return contactFormConfigurationService.getConfigEndpoint();
  }

}
