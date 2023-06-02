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
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
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

  @SlingObject
  private Resource resource;

  @Getter
  private boolean isSvg;

  @Getter
  private String assetLink;

  private static final String SVG_MIME_TYPE = "image/svg+xml";

  @OSGiService
  private SvgImageService svgImageService;

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
  }


  public String getSrc() {
    return LinkUtil.handleLink(src, resource.getResourceResolver());
  }

  public String getAssetReference() {
    return LinkUtil.handleLink(assetReference, resource.getResourceResolver());
  }

  public boolean getIsVideo() {
    String mimeType = URLConnection.guessContentTypeFromName(getAssetLink());
    return mimeType != null && mimeType.startsWith("video");
  }
}
