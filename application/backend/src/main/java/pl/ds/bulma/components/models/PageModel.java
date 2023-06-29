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

package pl.ds.bulma.components.models;

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
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import pl.ds.bulma.components.services.LibraryIconFactoryConfig;

@Model(adaptables = SlingHttpServletRequest.class)
public class PageModel {

  @OSGiService
  private LibraryIconFactoryConfig configuration;

  @Getter
  private List<IconLibraryConfig> iconLibraryConfigs = new ArrayList<>();

  @PostConstruct
  public void init() {
    List<LibraryIconFactoryConfig> allConfigs = configuration.getAllConfigs();
    iconLibraryConfigs = allConfigs
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

  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @Getter
  public static class IconLibraryConfig {

    private String libraryUrl;

    private Map<String, String> attributes;
  }

}
