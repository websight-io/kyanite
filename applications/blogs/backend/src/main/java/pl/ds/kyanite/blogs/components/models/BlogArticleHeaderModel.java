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

package pl.ds.kyanite.blogs.components.models;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
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
import org.apache.sling.models.factory.ModelFactory;
import pl.ds.kyanite.common.components.utils.DateFormatterUtil;
import pl.ds.kyanite.common.components.utils.LinkUtil;

@Model(
    adaptables = {Resource.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class BlogArticleHeaderModel {

  @ValueMapValue
  @Getter
  @Default(values = "Title")
  private String title;

  @ValueMapValue
  @Getter
  @Default(values = StringUtils.EMPTY)
  private String description;

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

  @ValueMapValue
  @Getter
  private String publicationDate;

  @ValueMapValue
  @Getter
  @Default(values = "0")
  private String readTime;

  @ValueMapValue
  private String heroImage;

  @ValueMapValue
  @Getter
  @Default(values = StringUtils.EMPTY)
  private String heroImageAlt;

  @ChildResource
  private List<Resource> tags;

  private final ResourceResolver resourceResolver;

  private final ModelFactory modelFactory;

  @Inject
  public BlogArticleHeaderModel(@SlingObject ResourceResolver resourceResolver,
      @OSGiService ModelFactory modelFactory) {
    this.resourceResolver = resourceResolver;
    this.modelFactory = modelFactory;
  }

  public String getAuthorPhoto() {
    return LinkUtil.handleLink(authorPhoto, resourceResolver);
  }

  public String getHeroImage() {
    return LinkUtil.handleLink(heroImage, resourceResolver);
  }

  public String getFormattedPublicationDate() {
    return DateFormatterUtil.formatDate(this.publicationDate);
  }

  public List<TagModel> getTags() {
    return Optional.ofNullable(tags)
        .orElseGet(Collections::emptyList)
        .stream()
        .map(resource -> modelFactory.createModel(resource, TagModel.class))
        .toList();
  }
}

