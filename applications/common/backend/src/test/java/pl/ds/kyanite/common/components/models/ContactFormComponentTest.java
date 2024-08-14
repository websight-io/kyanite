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

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pl.ds.kyanite.common.components.services.ContactFormConfigStore;
import pl.ds.kyanite.common.components.services.ContactFormConfiguration;
import pl.ds.kyanite.common.components.services.RecaptchaConfiguration;
import pl.ds.kyanite.common.components.services.impl.ContactFormConfigStoreImpl;
import pl.ds.kyanite.common.components.services.impl.ContactFormConfigurationImpl;
import pl.ds.kyanite.common.components.services.impl.RecaptchaConfigStoreImpl;
import pl.ds.kyanite.common.components.services.impl.RecaptchaConfigurationImpl;

@ExtendWith(SlingContextExtension.class)
public class ContactFormComponentTest {

  private static final String PATH = "/content";
  public static final String TEST_KEY = "test_key";
  private final SlingContext context = new SlingContext(ResourceResolverType.JCR_MOCK);


  @BeforeEach
  public void init() {
    context.addModelsForClasses(ContactFormComponent.class);
    context.load().json(requireNonNull(
            Thread.currentThread().getContextClassLoader().getResourceAsStream("contactform.json")),
        PATH);
  }

  @Test
  void defaultContactFormComponentTest() {
    ContactFormComponent model = context.resourceResolver().getResource(PATH + "/default")
        .adaptTo(ContactFormComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getSubmitLabel()).isEqualTo("Send");
    assertThat(model.getConsentText()).isEqualTo("<p>I agree to the terms and conditions</p>");
  }

  @Test
  void contactFormComponentTest() {
    ContactFormComponent model = context.resourceResolver().getResource(PATH + "/complex")
        .adaptTo(ContactFormComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getSubmitLabel()).isEqualTo("Send Message");
    assertThat(model.getConsentText()).isEqualTo("I consent");
  }

  @Test
  void shouldHaveCorrectCaptchaKey() throws IOException {
    //given
    RecaptchaConfiguration recaptchaConfiguration = createRecaptchaConfiguration("darkModeSpace", TEST_KEY);
    RecaptchaConfiguration secondRecaptcha = createRecaptchaConfiguration("base", "baseKey");
    RecaptchaConfigStoreImpl recaptchaConfigStore = new RecaptchaConfigStoreImpl();
    recaptchaConfigStore.bind(recaptchaConfiguration);
    recaptchaConfigStore.bind(secondRecaptcha);
    context.registerInjectActivateService(recaptchaConfigStore);

    ContactFormComponent model = context.resourceResolver().getResource(PATH + "/darkModeSpace/pages/test/jcr:content/pagecontainer/contactform")
        .adaptTo(ContactFormComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getSubmitLabel()).isEqualTo("Send Message");
    assertThat(model.getConsentText()).isEqualTo("I consent");
    assertThat(model.getCaptchaPublicKey()).isEqualTo(TEST_KEY);
  }

  @Test
  void shouldNotHaveRecaptcha() {

    context.registerInjectActivateService(new RecaptchaConfigStoreImpl());
    ContactFormComponent model = context.resourceResolver().getResource(PATH + "/darkModeSpace/pages/test/jcr:content/pagecontainer/contactform")
        .adaptTo(ContactFormComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getSubmitLabel()).isEqualTo("Send Message");
    assertThat(model.getConsentText()).isEqualTo("I consent");
    assertThat(model.getCaptchaPublicKey()).isNull();
  }

  @Test
  void shouldHaveFormConfiguration() {
    setupContactFormConfigStore("darkModeSpace", "host1", "path1");
    ContactFormComponent model = context.resourceResolver().getResource(PATH + "/darkModeSpace/pages/test/jcr:content/pagecontainer/contactform")
        .adaptTo(ContactFormComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getConfigEndpoint()).isEqualTo("host1/path1");
  }

  @Test
  void shouldNotHaveFormConfiguration() {
    setupContactFormConfigStore("lightModeSpace", "host2", "path2");
    ContactFormComponent model = context.resourceResolver().getResource(PATH + "/otherSpace/pages/test/jcr:content/pagecontainer/contactform")
        .adaptTo(ContactFormComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getConfigEndpoint()).isNull();
  }

  @NotNull
  private RecaptchaConfiguration createRecaptchaConfiguration(String spaceName, String key) {
    Map<String, Object> props = new HashMap<>();
    props.put("spaceName", spaceName);
    props.put("captchaPublicKey", key);
    RecaptchaConfiguration recaptchaConfiguration = new RecaptchaConfigurationImpl();
    context.registerInjectActivateService(recaptchaConfiguration,props);
    return recaptchaConfiguration;
  }

  @NotNull
  private ContactFormConfiguration createContactFormConfiguration(String spaceName, String host, String path) {
    Map<String, Object> properties = new HashMap<>();
    properties.put("spaceName", spaceName);
    properties.put("host", host);
    properties.put("path", path);
    ContactFormConfiguration contactFormConfiguration = new ContactFormConfigurationImpl();
    context.registerInjectActivateService(contactFormConfiguration, properties);
    return contactFormConfiguration;
  }

  private void setupContactFormConfigStore(String space, String host, String path) {
    ContactFormConfiguration contactFormConfig = createContactFormConfiguration(space, host, path);
    ContactFormConfigStoreImpl contactFormConfigStore = new ContactFormConfigStoreImpl();
    contactFormConfigStore.bind(contactFormConfig);
    context.registerInjectActivateService(contactFormConfigStore);
  }

}
