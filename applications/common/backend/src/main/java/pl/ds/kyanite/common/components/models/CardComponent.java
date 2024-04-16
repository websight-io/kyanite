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
import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import pl.ds.kyanite.common.components.models.background.ComponentWithBackground;
import pl.ds.kyanite.common.components.models.background.DefaultComponentWithBackground;
import pl.ds.kyanite.common.components.utils.LinkUtil;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class CardComponent implements ComponentWithBackground {

  public static final String CARD_CONTENT_TYPE = "kyanite/common/components/card/cardcontent";

  @Inject
  @Getter
  @Default(values = StringUtils.EMPTY)
  private String header;

  @Inject
  @Getter
  private ImageComponent image;

  @ValueMapValue
  @Getter
  @Default(values = "top")
  private String imagePosition;

  @ValueMapValue
  @Getter
  private String mobileAssetReference;

  @ValueMapValue
  @Getter
  private String tabletAssetReference;

  @Inject
  @Getter
  private List<UrlComponent> urls;

  @Inject
  @Getter
  private boolean content;

  @Inject
  @Getter
  @Default(values = StringUtils.EMPTY)
  private String heightVariant;

  @Inject
  @Getter
  @Default(values = "card")
  private String type;

  @Inject
  @Default(values = StringUtils.EMPTY)
  private String anchorUrl;

  @Inject
  @Getter
  private boolean openInNewTab;

  @SlingObject
  private Resource resource;

  @SlingObject
  private ResourceResolver resourceResolver;

  @Self
  @Delegate
  private DefaultComponentWithBackground componentWithBackground;

  @PostConstruct
  void init() {
    for (Resource child : resource.getChildren()) {
      String resourceType = child.getResourceType();
      if (CARD_CONTENT_TYPE.equals(resourceType)) {
        content = true;
        break;
      }
    }
  }

  public boolean isAnyMediaAsset() {
    return isNotEmpty(mobileAssetReference) || isNotEmpty(tabletAssetReference);
  }

  public String getTabletAssetReference() {
    return LinkUtil.handleLink(tabletAssetReference, resourceResolver);
  }

  public String getMobileAssetReference() {
    return LinkUtil.handleLink(mobileAssetReference, resourceResolver);
  }

  public String getAnchorUrl() {
    return LinkUtil.handleLink(anchorUrl, resource.getResourceResolver());
  }
}
