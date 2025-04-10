/*
 * Copyright (C) 2024 Dynamic Solutions
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

package pl.ds.kyanite.common.components.models.background;

import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

import java.util.Objects;
import java.util.stream.Stream;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import pl.ds.kyanite.common.components.models.HasLoadingMode;
import pl.ds.kyanite.common.components.models.ImageComponent;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class DefaultComponentWithBackground implements HasLoadingMode {

  @Inject
  private ImageComponent desktopBackgroundImage;

  @Inject
  private ImageComponent tabletBackgroundImage;

  @Inject
  private ImageComponent mobileBackgroundImage;

  @Inject
  @Getter
  private boolean asImg;

  @Inject
  @Getter
  private String alt;

  @ValueMapValue
  @Getter
  @Default(values = "auto")
  private String fetchPriority;

  @ValueMapValue
  @Getter
  private String width;

  @ValueMapValue
  @Getter
  private String height;

  public String getDesktopBackgroundImage() {
    return getBackgroundImage(desktopBackgroundImage);
  }

  public String getTabletBackgroundImage() {
    return getBackgroundImage(tabletBackgroundImage);
  }

  public String getMobileBackgroundImage() {
    return getBackgroundImage(mobileBackgroundImage);
  }

  public String getDesktopImage() {
    return getImage(desktopBackgroundImage);
  }

  public String getTabletImage() {
    return getImage(tabletBackgroundImage);
  }

  public String getMobileImage() {
    return getImage(mobileBackgroundImage);
  }

  public boolean getHasBackgroundImage() {
    return Stream.of(desktopBackgroundImage, tabletBackgroundImage, mobileBackgroundImage)
        .filter(Objects::nonNull)
        .map(ImageComponent::getAssetReference)
        .anyMatch(Objects::nonNull);
  }

  private String getBackgroundImage(ImageComponent image) {
    if (image == null || image.getAssetReference() == null) {
      return "none";
    }

    return String.format("url('%s')", image.getAssetReference());
  }

  private String getImage(ImageComponent image) {
    if (image == null || image.getAssetReference() == null) {
      return "none";
    }

    return image.getAssetReference();
  }
}
