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

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
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
import pl.ds.kyanite.common.components.services.SvgImageService;
import pl.ds.kyanite.common.components.utils.LinkUtil;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ImageComponent {

  private static final String NONE_STYLES_TYPE = "none";
  private static final String SVG_MIME_TYPE = "image/svg+xml";

  @ValueMapValue
  private String assetReference;

  @ValueMapValue
  @Getter
  private String type;

  @ValueMapValue
  @Default(values = NONE_STYLES_TYPE)
  private String style;

  @ValueMapValue
  @Getter
  private String isRounded;

  @ValueMapValue
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

  @ValueMapValue
  private String mobileAssetReference;

  @ValueMapValue
  private String tabletAssetReference;

  @SlingObject
  private ResourceResolver resourceResolver;

  @OSGiService
  private SvgImageService svgImageService;

  private final Map<String, Boolean> parameters = new HashMap<>();

  @Getter
  private Map<String, String> attributes;

  @Getter
  private boolean isSvg;

  @Getter
  @ValueMapValue
  private boolean isCodeSvg;

  @Getter
  private boolean isVideo;

  private boolean isInternal;

  @PostConstruct
  private void init() {
    if (Objects.nonNull(assetReference)) {
      this.processLink(assetReference, resourceResolver);
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

  private void processLink(String link, ResourceResolver resourceResolver) {
    isInternal = LinkUtil.isInternal(link, resourceResolver);
    isSvg = SVG_MIME_TYPE.equals(svgImageService.getMimeType(link, resourceResolver, isInternal));
    isVideo = this.isVideo(link);
  }

  public String getThumbnail() {
    return LinkUtil.handleLink(thumbnail, resourceResolver);
  }

  public String getAssetReference() {
    return LinkUtil.handleLink(assetReference, resourceResolver);
  }

  public String getTabletAssetReference() {
    return LinkUtil.handleLink(tabletAssetReference, resourceResolver);
  }

  public String getMobileAssetReference() {
    return LinkUtil.handleLink(mobileAssetReference, resourceResolver);
  }

  public String getSvgDocument() {
    if (isSvg && !isAnyMediaAsset()) {
      if (isInternal) {
        return svgImageService.getSvgFromResource(assetReference, resourceResolver);
      } else {
        return svgImageService.getSvgFromExternalUrl(assetReference);
      }
    }
    return null;
  }

  public String getStyle() {
    if (NONE_STYLES_TYPE.equals(type)) {
      return "";
    }
    return style;
  }

  public boolean isAnyMediaAsset() {
    return isNotEmpty(mobileAssetReference) || isNotEmpty(tabletAssetReference);
  }

  private boolean isVideo(String link) {
    String mimeType = URLConnection.guessContentTypeFromName(link);
    return mimeType != null && mimeType.startsWith("video");
  }
}
