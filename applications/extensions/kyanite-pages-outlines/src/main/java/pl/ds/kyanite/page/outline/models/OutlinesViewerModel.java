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

package pl.ds.kyanite.page.outline.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import pl.ds.kyanite.page.outline.configuration.PageOutlineConfigStore;
import pl.ds.websight.pages.core.api.Page;

@Model(
    adaptables = {Resource.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class OutlinesViewerModel {

  private static final int MAX_COLUMN_SIZE = 12;
  private static final String PN_TEMPLATE = "ws:template";
  private static final String PN_ELEMENTS_IN_ROW = "elementsInRow";
  private static final String NN_OUTLINES = "outlines/";

  @ValueMapValue
  private String outlineType;

  @ValueMapValue
  @Default(intValues = 0)
  private Integer elements;

  @ValueMapValue
  private String rootPath;

  @ChildResource
  private Resource desktop;

  @ChildResource
  private Resource tablet;

  @ChildResource
  private Resource mobile;

  @OSGiService
  private PageOutlineConfigStore pageOutlineConfigStore;

  @SlingObject
  private Resource resource;

  @SlingObject
  private ResourceResolver resourceResolver;

  @Getter
  private List<String> outlines;

  @Getter
  private Integer desktopColSize;

  @Getter
  private Integer tabletColSize;

  @Getter
  private Integer mobileColSize;

  public Integer getElements() {
    if (elements == null || elements <= 0) {
      return outlines.size();
    }
    return elements - 1;
  }

  @PostConstruct
  private void init() {
    outlines = findOutlines();
    desktopColSize = calculateDesktopColumnSize();
    tabletColSize = calculateTabletColumnSize();
    mobileColSize = calculateMobileColumnSize();
  }

  private List<String> findOutlines() {
    if (outlineType == null) {
      return new ArrayList<>();
    }

    List<String> templates = pageOutlineConfigStore.findTemplatesByOutlineType(outlineType);

    if (templates.isEmpty()) {
      return new ArrayList<>();
    }

    Stream<Page> rootPages = streamRootPages();

    return streamAllPages(rootPages)
        .filter(page -> isOneOfType(page, templates))
        .map(page -> getOutlineResource(page, outlineType))
        .filter(Objects::nonNull)
        .map(Resource::getPath)
        .toList();
  }

  private Stream<Page> streamRootPages() {
    Page rootPage = getRootPage();
    if (rootPage != null) {
      return Stream.of(rootPage);
    }

    Resource space = getSpace();
    if (space == null) {
      return Stream.empty();
    }

    return StreamSupport.stream(space.getChildren().spliterator(), false)
        .map(pageResource -> pageResource.adaptTo(Page.class))
        .filter(Objects::nonNull);
  }

  private Page getRootPage() {
    if (rootPath == null) {
      return null;
    }

    Resource rootResource = resourceResolver.getResource(rootPath);
    if (rootResource == null) {
      return null;
    }

    return rootResource.adaptTo(Page.class);
  }

  private Stream<Page> streamAllPages(Stream<Page> pages) {
    return pages.flatMap(page -> Stream.concat(Stream.of(page),
        streamAllPages(page.streamOfChildren())));
  }

  private boolean isOneOfType(Page page, List<String> templates) {
    String template = page.getContentProperty(PN_TEMPLATE, String.class);
    return templates.contains(template);
  }

  private Resource getOutlineResource(Page page, String outlineType) {
    return resourceResolver.getResource(page.getResource(), NN_OUTLINES + outlineType);
  }

  private Resource getSpace() {
    String spacePath = getCurrentSpace();
    if (spacePath == null) {
      return null;
    }

    return resourceResolver.getResource(spacePath);
  }

  private String getCurrentSpace() {
    String resourcePath = resource.getPath();
    String regex = "^((/content|/published)/[^/]*/pages/).*";
    if (StringUtils.isNotBlank(resourcePath)) {
      final Pattern pattern = Pattern.compile(regex);
      final Matcher matcher = pattern.matcher(resourcePath);

      if (matcher.find()) {
        return matcher.group(1);
      }
    }
    return null;
  }

  private Integer calculateDesktopColumnSize() {
    int elementsInRow = getElementsInRow(desktop, 4);

    return calculateColumnSize(elementsInRow);
  }

  private Integer calculateTabletColumnSize() {
    int elementsInRow = getElementsInRow(tablet, 2);

    return calculateColumnSize(elementsInRow);
  }

  private Integer calculateMobileColumnSize() {
    int elementsInRow = getElementsInRow(mobile, 1);

    return calculateColumnSize(elementsInRow);
  }

  private Integer getElementsInRow(Resource resource, Integer defaultValue) {
    if (resource != null) {
      return resource.getValueMap().get(PN_ELEMENTS_IN_ROW, defaultValue);
    }
    return defaultValue;
  }

  private Integer calculateColumnSize(Integer elementsInRow) {
    return MAX_COLUMN_SIZE / elementsInRow;
  }

}
