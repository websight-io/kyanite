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
import pl.ds.kyanite.common.components.configurations.GoogleAnalyticsConfigurationOcd;
import pl.ds.kyanite.common.components.services.GoogleAnalyticsConfiguration;

@Component(service = GoogleAnalyticsConfiguration.class)
@Designate(ocd = GoogleAnalyticsConfigurationOcd.class, factory = true)
public class GoogleAnalyticsConfigurationImpl implements GoogleAnalyticsConfiguration {

  private GoogleAnalyticsConfigurationOcd config;

  @Activate
  @Modified
  protected void activate(final GoogleAnalyticsConfigurationOcd config) {
    this.config = config;
  }

  @Override
  public String getSpaceName() {
    return Objects.nonNull(config.spaceName()) ? config.spaceName() : StringUtils.EMPTY;
  }

  @Override
  public String getGoogleAnalyticsUrl() {
    String trackingId = config.gaTrackingId();
    String url = config.url();
    if (StringUtils.isBlank(trackingId) && StringUtils.isBlank(url)) {
      return StringUtils.EMPTY;
    }
    if (url.contains("?")) {
      url += "&id=" + trackingId;
    } else {
      url += "?id=" + trackingId;
    }
    return url;
  }

  @Override
  public String getGoogleAnalyticsTrackingId() {
    return Objects.nonNull(config.gaTrackingId()) ? config.gaTrackingId() : StringUtils.EMPTY;
  }

  @Override
  public String getGoogleAnalyticsScriptUrl() {
    return Objects.nonNull(config.scriptUrl()) ? config.scriptUrl() : StringUtils.EMPTY;
  }
}
