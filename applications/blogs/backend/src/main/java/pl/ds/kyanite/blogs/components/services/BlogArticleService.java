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
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
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

  public static final String FEATURE_BLOG_ARTICLE_RESOURCE_TYPE =
      "kyanite/blogs/components/featuredblogarticle";

  public static final String JCR_SQL_2 = "JCR-SQL2";
  public static final String TEMPLATE_BLOGARTICLE_PAGE =
      "/libs/kyanite/blogs/templates/blogarticlepage";

  public List<Resource> getListBlogArticlePages(String path, ResourceResolver resourceResolver) {
    final String pagesQuery = """
        SELECT p.* FROM [ws:PageContent]
        AS p WHERE ISDESCENDANTNODE(p, '%s')
        AND p.[ws:template] = '/libs/kyanite/blogs/templates/blogarticlepage'
        ORDER BY p.[publicationDate] DESC""";
    final String formattedQuery = pagesQuery.formatted(path);
    final Iterator<Resource> iterator = resourceResolver.findResources(
        formattedQuery,
        JCR_SQL_2);
    return StreamSupport.stream(
        Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED),
        false
    ).toList();
  }

  public List<Resource> getListBlogArticlePages(Page page) {
    return page.streamOfChildrenRecursively()
        .map(Page::getContentResource)
        .filter(this::isBlogArticlePage)
        .sorted(Comparator.comparing(this::getBlogArticleCreationDate,
            Comparator.nullsLast(Comparator.reverseOrder())))
        .toList();
  }

  public List<Resource> findFeatureBlogsOnPage(Resource resource) {
    return this.getDescendants(resource).stream()
        .filter(
            res -> StringUtils.equals(FEATURE_BLOG_ARTICLE_RESOURCE_TYPE, res.getResourceType()))
        .toList();
  }

  public List<Resource> findFeatureBlogsOnPage(String path, ResourceResolver resourceResolver) {
    return getResourceStream(
        path, FEATURE_BLOG_ARTICLE_RESOURCE_TYPE, resourceResolver)
        .toList();
  }

  private Stream<Resource> getResourceStream(String path, String componentResourceType,
      ResourceResolver resourceResolver) {
    final String query = """
        SELECT p.* FROM [nt:unstructured] AS p
        WHERE ISDESCENDANTNODE(p, '%s')
        AND p.[sling:resourceType] = '%s'""";
    final String formattedQuery = query.formatted(path,
        componentResourceType);
    final Iterator<Resource> resources = resourceResolver.findResources(formattedQuery,
        JCR_SQL_2);
    return StreamSupport.stream(
        Spliterators.spliteratorUnknownSize(resources, Spliterator.ORDERED),
        false
    );
  }

  public boolean isBlogArticlePage(Resource resource) {
    return Optional.ofNullable(resource)
        .map(Resource::getValueMap)
        .stream().anyMatch(vm -> {
          String template = vm.get("ws:template", String.class);
          return TEMPLATE_BLOGARTICLE_PAGE.equals(template);
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

}