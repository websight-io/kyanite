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
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.ds.kyanite.common.components.services.ContactFormConfigStore;
import pl.ds.kyanite.common.components.services.ContactFormConfiguration;
import pl.ds.kyanite.common.components.services.RecaptchaConfigStore;
import pl.ds.kyanite.common.components.services.RecaptchaConfiguration;
import pl.ds.kyanite.common.components.utils.PageSpace;
import pl.ds.kyanite.common.components.utils.PageUtil;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContactFormComponent {

  private static final Logger LOG = LoggerFactory.getLogger(ContactFormComponent.class);

  @SlingObject
  private Resource resource;

  @OSGiService
  private ContactFormConfigStore contactFormConfigStore;
  @OSGiService
  private RecaptchaConfigStore recaptchaConfigStore;

  @Inject
  @Getter
  private List<TypeOfInquiry> types = new ArrayList<>();

  @ValueMapValue
  @Getter
  @Default(values = "Send")
  private String submitLabel;

  @ValueMapValue
  @Getter
  private String formHeader;

  @ValueMapValue
  @Getter
  @Default(values = "Your message")
  private String messagePlaceholder;

  @ValueMapValue
  @Getter
  @Default(values = "<p>I agree to the terms and conditions</p>")
  private String consentText;

  @ValueMapValue
  @Getter
  private boolean showMessage;

  @ValueMapValue
  @Getter
  private boolean showAlternativeSubmitMessage;

  @ValueMapValue
  @Getter
  @Default(values = "A member of our team will be in contact with you shortly.")
  private String alternativeSuccessMessage;

  @ValueMapValue
  @Getter
  private String alternativeSuccessButtonLink;

  @ValueMapValue
  @Getter
  @Default(values = "Back to the form")
  private String alternativeSuccessButtonLabel;

  @ValueMapValue
  @Getter
  @Default(values = "Unfortunately, we were unable to complete your request "
      + "due to an unexpected error. Please attempt to submit your inquiry again.")
  private String alternativeFailureMessage;

  @ValueMapValue
  @Getter
  private String alternativeFailureButtonLink;

  @ValueMapValue
  @Getter
  @Default(values = "Back to the form")
  private String alternativeFailureButtonLabel;

  @Getter
  private String spaceName;

  @Getter
  private String pageName;

  @PostConstruct
  private void init() {
    PageSpace pageSpace = PageSpace.forResource(resource);
    if (pageSpace != null) {
      spaceName = pageSpace.getWsPagesSpaceName();
    }
    try {
      pageName = PageUtil.getPageProperty(resource, "jcr:title");
    } catch (Exception e) {
      LOG.warn("Contact form component is out of a PageSpace");
    }
  }

  public String getCaptchaPublicKey() {
    RecaptchaConfiguration config = recaptchaConfigStore.get(spaceName);
    if (config != null) {
      return config.getCaptchaPublicKey();
    }
    return null;
  }

  public String getConfigEndpoint() {
    ContactFormConfiguration config = contactFormConfigStore.get(spaceName);
    if (config != null) {
      return config.getConfigEndpoint();
    }
    return null;
  }

}
