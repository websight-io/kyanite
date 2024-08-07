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

package pl.ds.kyanite.blogs.components.models;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import pl.ds.kyanite.common.components.utils.LinkUtil;

@Model(
    adaptables = {Resource.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class AuthorInfoModel {

  @ValueMapValue
  @Getter
  @Default(values = StringUtils.EMPTY)
  private String authorName;

  @ValueMapValue
  @Getter
  @Default(values = StringUtils.EMPTY)
  private String authorDescription;

  @ValueMapValue
  @Getter
  @Default(values = StringUtils.EMPTY)
  private String authorRole;

  @ValueMapValue
  private String authorPhoto;

  @ValueMapValue
  @Getter
  @Default(values = StringUtils.EMPTY)
  private String authorPhotoAlt;

  @Getter
  @Setter
  private String authorOwnerPath;

  @SlingObject
  private ResourceResolver resourceResolver;

  @Getter
  @ChildResource
  private Resource socialMedia;

  public String getAuthorPhoto() {
    return LinkUtil.handleLink(authorPhoto, resourceResolver);
  }

  public String getAuthorOwnerUrl() {
    return LinkUtil.handleLink(authorOwnerPath, resourceResolver);
  }
}
