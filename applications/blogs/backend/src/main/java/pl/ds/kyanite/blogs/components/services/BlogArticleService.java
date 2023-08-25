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

package pl.ds.kyanite.blogs.components.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import pl.ds.websight.pages.core.api.Page;


@Component(service = {BlogArticleService.class})
public class BlogArticleService {

  private static final String FEATURE_BLOG_ARTICLE_RESOURCE_TYPE =
      "kyanite/blogs/components/featuredblogarticle";
  private static final String TEMPLATE_BLOGARTICLE_PAGE =
      "/libs/kyanite/blogs/templates/blogarticlepage";

  public List<Resource> getListBlogArticlePages(String resourcePath,
      ResourceResolver resourceResolver) {
    Stream<Page> rootPages = streamRootPages(resourcePath, resourceResolver);

    return rootPages.map(this::getListBlogArticlePages)
        .flatMap(List::stream)
        .sorted(creationDateComparator())
        .toList();
  }

  public List<Resource> getListBlogArticlePages(Page page) {
    return streamBlogArticles(page)
        .sorted(creationDateComparator())
        .toList();
  }

  private Comparator<Resource> creationDateComparator() {
    return Comparator.comparing(this::getBlogArticleCreationDate,
        Comparator.nullsLast(Comparator.reverseOrder()));
  }

  private Stream<Resource> streamBlogArticles(Page page) {
    return Stream.concat(Stream.of(page), page.streamOfChildrenRecursively())
        .map(Page::getContentResource)
        .filter(this::isBlogArticlePage);
  }

  public List<Resource> findFeatureBlogsOnPage(Resource resource) {
    return this.getDescendants(resource).stream()
        .filter(
            res -> StringUtils.equals(FEATURE_BLOG_ARTICLE_RESOURCE_TYPE, res.getResourceType()))
        .toList();
  }

  public boolean isBlogArticlePage(Resource resource) {
    return Optional.ofNullable(resource)
        .map(Resource::getValueMap)
        .stream().anyMatch(vm -> {
          String template = vm.get("ws:template", String.class);
          if (template == null) {
            return false;
          }
          if (TEMPLATE_BLOGARTICLE_PAGE.equals(template)) {
            return true;
          }
          Resource templateResource = resource.getResourceResolver().getResource(template);
          return templateResource != null
              && templateResource.isResourceType(TEMPLATE_BLOGARTICLE_PAGE);
        });
  }

  private LocalDate getBlogArticleCreationDate(Resource resource) {
    return Optional.ofNullable(resource)
        .map(Resource::getValueMap)
        .map(vm -> vm.get("publicationDate", String.class))
        .map(date -> LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        .orElse(null);
  }

  private Collection<Resource> getDescendants(Resource resource) {
    final Collection<Resource> children = Optional.ofNullable(resource)
        .map(res -> StreamSupport.stream(res.getChildren().spliterator(), false).collect(
            Collectors.toSet()))
        .orElse(Collections.emptySet());
    return children.isEmpty() ? Collections.emptySet()
        : Stream.concat(children.stream(), children.stream()
                .map(this::getDescendants)
                .flatMap(Collection::stream))
            .collect(Collectors.toSet());
  }

  private Stream<Page> streamRootPages(String resourcePath, ResourceResolver resourceResolver) {
    Resource space = getSpace(resourcePath, resourceResolver);
    if (space == null) {
      return Stream.empty();
    }

    return StreamSupport.stream(space.getChildren().spliterator(), false)
        .map(pageResource -> pageResource.adaptTo(Page.class))
        .filter(Objects::nonNull);
  }

  private Resource getSpace(String resourcePath, ResourceResolver resourceResolver) {
    String spacePath = getCurrentSpace(resourcePath);
    if (spacePath == null) {
      return null;
    }

    return resourceResolver.getResource(spacePath);
  }

  private String getCurrentSpace(String resourcePath) {
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

}