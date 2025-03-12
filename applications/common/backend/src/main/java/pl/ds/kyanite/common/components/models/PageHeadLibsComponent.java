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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.ds.kyanite.common.components.services.GoogleAnalyticsConfigStore;
import pl.ds.kyanite.common.components.services.GoogleAnalyticsConfiguration;
import pl.ds.kyanite.common.components.services.LibraryIconConfig;
import pl.ds.kyanite.common.components.services.LibraryIconConfigStore;
import pl.ds.kyanite.common.components.utils.PageSpace;

@Model(adaptables = SlingHttpServletRequest.class)
public class PageHeadLibsComponent {

  private static final Logger LOG = LoggerFactory.getLogger(PageHeadLibsComponent.class);

  private static final String VERSIONED_RESOURCES_MANIFEST =
      "/libs/kyanite/webroot/versioned-resources-manifest.json";

  @SlingObject
  private Resource resource;

  @SlingObject
  private ResourceResolver resourceResolver;

  private final LibraryIconConfigStore libraryIconConfigStore;

  private final GoogleAnalyticsConfigStore googleAnalyticsConfigStore;

  private GoogleAnalyticsConfiguration googleAnalyticsConfig;

  @Getter
  private List<IconLibraryConfig> iconLibraryConfigs = new ArrayList<>();

  @Getter
  private String stylesPath;

  @Getter
  private Map<String, String> versionedResources = new HashMap<>();

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
    Resource manifestResource = resourceResolver.getResource(VERSIONED_RESOURCES_MANIFEST);
    if (manifestResource != null) {
      try (InputStream inputStream = manifestResource.adaptTo(InputStream.class);
          JsonReader jsonReader = Json.createReader(inputStream)) {
        JsonObject jsonObject = jsonReader.readObject();
        jsonObject.forEach((key, value) ->
            versionedResources.put(key, ((JsonString) value).getString()));
      } catch (IOException e) {
        LOG.error("Could not parse Resource Manifest JSON", e);
      }
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
