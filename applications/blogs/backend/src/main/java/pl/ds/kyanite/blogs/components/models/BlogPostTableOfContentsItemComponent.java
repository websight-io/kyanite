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

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Getter
public class BlogPostTableOfContentsItemComponent {

  private final String title;
  private final String url;
  private final String headingLevel;

  private final List<BlogPostTableOfContentsItemComponent> subTitles = new ArrayList<>();

  @Nullable
  @Setter
  private BlogPostTableOfContentsItemComponent parent;

  BlogPostTableOfContentsItemComponent(String title, String url, String headingLevel) {
    this.title        = title;
    this.url          = url;
    this.headingLevel = headingLevel;
  }

  BlogPostTableOfContentsItemComponent(
      String title, String url, String headingLevel,
      @Nullable BlogPostTableOfContentsItemComponent parent) {
    this(title, url, headingLevel);
    this.parent = parent;
  }
}
