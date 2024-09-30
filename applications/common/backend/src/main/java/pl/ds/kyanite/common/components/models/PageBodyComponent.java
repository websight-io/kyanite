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

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import pl.ds.kyanite.common.components.services.CookieModalConfigStore;
import pl.ds.kyanite.common.components.services.CookieModalConfiguration;
import pl.ds.kyanite.common.components.services.GoogleAnalyticsConfigStore;
import pl.ds.kyanite.common.components.services.GoogleAnalyticsConfiguration;
import pl.ds.kyanite.common.components.services.RecaptchaConfigStore;
import pl.ds.kyanite.common.components.services.RecaptchaConfiguration;
import pl.ds.kyanite.common.components.utils.PageSpace;
import pl.ds.kyanite.common.components.utils.PageUtil;


@Model(adaptables = Resource.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PageBodyComponent {

  public static final String DEFAULT_PRIVACY_POLICY_PATH = "/privacy-policy.html";
  public static final String DEFAULT_CONTACT_US_PATH = "/about/contact-us.html";
  private final Resource resource;
  private final RecaptchaConfigStore recaptchaConfigStore;
  private final CookieModalConfigStore cookieModalConfigStore;
  private final GoogleAnalyticsConfigStore googleAnalyticsConfigStore;

  @Getter
  @ValueMapValue
  private boolean noNavbarPadding;

  @Getter
  @ValueMapValue
  private boolean addCaptcha;

  @Getter
  @ValueMapValue
  private boolean createPreviewImagesCarousel;

  private String spaceName;

  @Getter
  private String cookieData;

  private GoogleAnalyticsConfiguration googleAnalyticsConfig;

  @Inject
  public PageBodyComponent(@SlingObject Resource resource,
      @OSGiService CookieModalConfigStore cookieModalConfigStore,
      @OSGiService GoogleAnalyticsConfigStore googleAnalyticsConfigStore,
      @OSGiService RecaptchaConfigStore recaptchaConfigStore) {
    this.resource = resource;
    this.cookieModalConfigStore = cookieModalConfigStore;
    this.googleAnalyticsConfigStore = googleAnalyticsConfigStore;
    this.recaptchaConfigStore = recaptchaConfigStore;
  }

  @PostConstruct
  private void init() {
    PageSpace pageSpace = PageSpace.forResource(resource);
    if (pageSpace != null) {
      spaceName = pageSpace.getWsPagesSpaceName();
      googleAnalyticsConfig = googleAnalyticsConfigStore.get(pageSpace.getWsPagesSpaceName());
    }
    initCookieData();
  }

  private void initCookieData() {
    JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
    CookieModalConfiguration cookieModalConfiguration = cookieModalConfigStore.get(spaceName);
    String privacyPolicyPath = DEFAULT_PRIVACY_POLICY_PATH;
    String contactUsPath = DEFAULT_CONTACT_US_PATH;
    if (cookieModalConfiguration != null) {
      privacyPolicyPath = StringUtils.isNotBlank(cookieModalConfiguration.getPrivacyPolicyPath())
          ? cookieModalConfiguration.getPrivacyPolicyPath() : privacyPolicyPath;
      contactUsPath = StringUtils.isNotBlank(cookieModalConfiguration.getContactUsPath())
          ? cookieModalConfiguration.getContactUsPath() : contactUsPath;
    }
    jsonObjectBuilder.add("privacyPolicyPath", privacyPolicyPath);
    jsonObjectBuilder.add("contactUsPath", contactUsPath);
    cookieData = jsonObjectBuilder.build().toString();
  }

  public String getCaptchaPublicKey() {
    RecaptchaConfiguration config = recaptchaConfigStore.get(spaceName);
    if (config != null) {
      return config.getCaptchaPublicKey();
    }
    return null;
  }

  public String getPageName() {
    return PageUtil.getPageProperty(resource, "jcr:title")
        .replaceAll("[^A-Za-z0-9-_]", "-")
        .toLowerCase();
  }

  public String getTemplateName() {
    String pageProperty = PageUtil.getPageProperty(resource, "ws:template");
    return pageProperty == null ? ""
        : pageProperty.substring(pageProperty.lastIndexOf("/") + 1);
  }

  public String getGoogleAnalyticsTrackingId() {
    if (googleAnalyticsConfig != null) {
      return googleAnalyticsConfig.getGoogleAnalyticsTrackingId();
    }
    return null;
  }

  public boolean hasAnalyticsUrl() {
    if (googleAnalyticsConfig != null) {
      return StringUtils.isNotBlank(
          googleAnalyticsConfig.getGoogleAnalyticsTrackingId())
          && StringUtils.isNotBlank(
          googleAnalyticsConfig.getGoogleAnalyticsScriptUrl());
    }
    return false;
  }
}
