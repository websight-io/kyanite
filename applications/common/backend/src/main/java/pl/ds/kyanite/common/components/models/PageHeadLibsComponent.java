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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.kyanite.common.components.services.GoogleAnalyticsConfigStore;
import pl.ds.kyanite.common.components.services.GoogleAnalyticsConfiguration;
import pl.ds.kyanite.common.components.services.LibraryIconConfig;
import pl.ds.kyanite.common.components.services.LibraryIconConfigStore;
import pl.ds.kyanite.common.components.utils.PageSpace;

@Model(adaptables = SlingHttpServletRequest.class)
public class PageHeadLibsComponent {

  @SlingObject
  private Resource resource;

  private final LibraryIconConfigStore libraryIconConfigStore;

  private final GoogleAnalyticsConfigStore googleAnalyticsConfigStore;

  private GoogleAnalyticsConfiguration googleAnalyticsConfig;

  @Getter
  private List<IconLibraryConfig> iconLibraryConfigs = new ArrayList<>();

  @Getter
  private String stylesPath;

  @Inject
  public PageHeadLibsComponent(@OSGiService LibraryIconConfigStore libraryIconConfigStore,
      @OSGiService GoogleAnalyticsConfigStore googleAnalyticsConfigStore) {
    this.libraryIconConfigStore = libraryIconConfigStore;
    this.googleAnalyticsConfigStore = googleAnalyticsConfigStore;
  }

  @PostConstruct
  public void init() {
    iconLibraryConfigs = initIconLibraryConfigs();
    PageSpace pageSpace = PageSpace.forResource(resource);
    if (pageSpace != null) {
      googleAnalyticsConfig = googleAnalyticsConfigStore.get(pageSpace.getWsPagesSpaceName());
      stylesPath = initStylesPath(pageSpace);
    }
  }

  public String getGoogleAnalyticsUrl() {
    if (googleAnalyticsConfig != null) {
      return googleAnalyticsConfig.getGoogleAnalyticsUrl();
    }
    return null;
  }

  public String getGoogleAnalyticsScriptUrl() {
    if (googleAnalyticsConfig != null) {
      return googleAnalyticsConfig.getGoogleAnalyticsScriptUrl();
    }
    return null;
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

  private List<IconLibraryConfig> initIconLibraryConfigs() {
    List<LibraryIconConfig> allConfigs = libraryIconConfigStore.getAllConfigs();
    return allConfigs
        .stream().map(config -> {
          List<String> attrs = List.of(config.getAttributes());
          Map<String, String> attributes = attrs.stream()
              .collect(Collectors.toMap(attribute -> attribute.split("=")[0],
                  attribute -> attribute.split("=")[1]));
          return IconLibraryConfig.builder()
              .attributes(attributes)
              .libraryUrl(config.getLibraryUrl())
              .build();
        })
        .toList();
  }

  private String initStylesPath(PageSpace pageSpace) {
    return pageSpace.getPageSpaceTemplateProperty("stylePath", StringUtils.EMPTY);
  }

  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @Getter
  public static class IconLibraryConfig {

    private String libraryUrl;

    private Map<String, String> attributes;
  }

}
