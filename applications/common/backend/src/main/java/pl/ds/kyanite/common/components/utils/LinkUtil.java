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

package pl.ds.kyanite.common.components.utils;

import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.jetbrains.annotations.Nullable;
import pl.ds.websight.assets.core.api.Asset;
import pl.ds.websight.assets.core.api.AssetsConstants;

public class LinkUtil {

  public static final String PUBLISHED = "/published";
  public static final String CONTENT = "/content";
  private static final String ANCHOR_LINK_PREFIX = "#";
  private static final String KYANITE_WEB_RESOURCE_ROOT_PATH = "/libs/kyanite/webroot/";


  private LinkUtil() {
    // no instance
  }

  public static String handleLink(String link, ResourceResolver resourceResolver) {
    if (StringUtils.isNotEmpty(link)) {
      if (isInternal(link, resourceResolver)) {
        return handleInternalLink(link, resourceResolver);
      } else if (isAnchorLink(link)) {
        return link;
      }
    }

    return link;
  }

  private static String handleInternalLink(String link, ResourceResolver resourceResolver) {
    if (isAsset(link, resourceResolver)) {
      Asset asset = getAssetForProvidedLink(link, resourceResolver);
      if (asset != null && asset.getOriginalRendition() != null) {
        return isPublished(link) ? StringUtils.replaceFirst(asset.getOriginalRendition().getPath(),
            CONTENT,
            PUBLISHED) : asset.getOriginalRendition().getPath();
      }

      return null;
    }

    return addHtmlExtensionSuffixToLink(link);
  }

  public static boolean isAnchorLink(String link) {
    return StringUtils.startsWith(link, ANCHOR_LINK_PREFIX);
  }

  public static boolean isInternal(String link, ResourceResolver resourceResolver) {
    return Objects.nonNull(getResource(link, resourceResolver)) && !isWebResource(link);
  }

  private static boolean isAsset(String link, ResourceResolver resourceResolver) {
    Resource resource = getResource(link, resourceResolver);
    return AssetsConstants.NT_ASSET.equals(getPrimaryType(resource));
  }

  private static String addHtmlExtensionSuffixToLink(String link) {
    return StringUtils.removeEnd(link, "/") + ".html";
  }

  @Nullable
  private static Asset getAssetForProvidedLink(String link, ResourceResolver resourceResolver) {
    Resource assetResource = getResource(link, resourceResolver);
    Asset asset = null;
    if (assetResource != null) {
      asset = assetResource.adaptTo(Asset.class);
    }
    return asset;
  }

  public static Resource getResource(String link, ResourceResolver resourceResolver) {
    if (StringUtils.startsWith(link, PUBLISHED)) {
      return resourceResolver.getResource(link.replaceFirst(PUBLISHED, CONTENT));
    }
    return resourceResolver.getResource(link);
  }

  public static boolean isPublished(String link) {
    return StringUtils.startsWith(link, PUBLISHED);
  }

  private static String getPrimaryType(Resource resource) {
    if (resource == null) {
      return null;
    }
    return resource.getValueMap().get("jcr:primaryType", String.class);
  }

  private static boolean isWebResource(final String link) {
    return StringUtils.startsWith(link, KYANITE_WEB_RESOURCE_ROOT_PATH);
  }
}
