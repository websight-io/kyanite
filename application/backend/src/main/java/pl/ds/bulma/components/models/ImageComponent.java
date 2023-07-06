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

import java.io.IOException;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import pl.ds.bulma.components.services.SvgImageService;
import pl.ds.bulma.components.utils.LinkUtil;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ImageComponent {

  @Inject
  private String src;

  @Inject
  private String assetReference;

  @Inject
  @Getter
  private String type;

  @Inject
  @Getter
  private String style;

  @Inject
  @Getter
  private String isRounded;

  @Inject
  @Getter
  private String alt;

  @ValueMapValue
  private boolean isMuted;

  @ValueMapValue
  private boolean isLooped;

  @ValueMapValue
  private boolean hasControls;

  @ValueMapValue
  private boolean autoplay;

  @ValueMapValue
  @Getter
  private boolean hasVideoOptions;

  @ValueMapValue
  @Getter
  private boolean hasShadow;

  @ValueMapValue
  @Default(values = StringUtils.EMPTY)
  private String thumbnail;

  @ValueMapValue
  @Getter
  private String width;

  @ValueMapValue
  @Getter
  private String height;

  private final Map<String, Boolean> parameters = new HashMap<>();

  @SlingObject
  private Resource resource;

  @Getter
  private boolean isSvg;

  @Getter
  private String assetLink;

  private static final String SVG_MIME_TYPE = "image/svg+xml";

  @OSGiService
  private SvgImageService svgImageService;

  @Getter
  private boolean isVideo;

  @Getter
  private Map<String, String> attributes;

  @PostConstruct
  private void init() throws IOException {
    final ResourceResolver resourceResolver = resource.getResourceResolver();
    if (Objects.nonNull(assetReference)) {
      this.processLink(assetReference, resourceResolver);
    }
    if (Objects.isNull(assetReference) && Objects.nonNull(src)) {
      this.processLink(src, resourceResolver);
      assetReference = src;
    }
    this.parameters.put("controls", hasControls);
    this.parameters.put("autoplay", autoplay);
    this.parameters.put("loop", isLooped);
    this.parameters.put("muted", isMuted);
    this.attributes = parameters.entrySet()
        .stream()
        .filter(Entry::getValue)
        .collect(Collectors.toMap(Entry::getKey, elem -> String.valueOf(true)));
    if (StringUtils.isNotBlank(this.getThumbnail())) {
      this.attributes.put("poster", this.getThumbnail());
    }
  }

  private void processLink(String link, ResourceResolver resourceResolver) throws IOException {
    boolean isInternal = LinkUtil.isInternal(link, resourceResolver);
    this.isSvg = SVG_MIME_TYPE.equals(
        svgImageService.getMimeType(link, resourceResolver, isInternal));
    if (this.isSvg) {
      if (isInternal) {
        this.assetLink = svgImageService.getSvgFromResource(link, resourceResolver);
      } else {
        this.assetLink = svgImageService.getSvgFromExternalUrl(link);
      }
    }
    this.isVideo = this.isVideo(link);
  }

  public String getThumbnail() {
    return LinkUtil.handleLink(thumbnail, resource.getResourceResolver());
  }

  public String getSrc() {
    return LinkUtil.handleLink(src, resource.getResourceResolver());
  }

  public String getAssetReference() {
    return LinkUtil.handleLink(assetReference, resource.getResourceResolver());
  }

  private boolean isVideo(String link) {
    String mimeType = URLConnection.guessContentTypeFromName(link);
    return mimeType != null && mimeType.startsWith("video");
  }
}
