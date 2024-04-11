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

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static pl.ds.kyanite.blogs.components.exceptions.AuthorInfoResolvingExceptionTemplates.AUTHOR_INFO_SOURCE_IS_NULL;
import static pl.ds.kyanite.blogs.components.exceptions.AuthorInfoResolvingExceptionTemplates.AUTHOR_INFO_SOURCE_NOT_SET;
import static pl.ds.kyanite.blogs.components.exceptions.AuthorInfoResolvingExceptionTemplates.AUTHOR_INFO_SOURCE_PATH_NOT_SET;
import static pl.ds.kyanite.blogs.components.exceptions.AuthorInfoResolvingExceptionTemplates.AUTHOR_INFO_SOURCE_TYPE_UNKNOWN;
import static pl.ds.kyanite.blogs.components.exceptions.AuthorInfoResolvingExceptionTemplates.AUTHOR_NODE_MISSING_IN_CONSUMER;
import static pl.ds.kyanite.blogs.components.exceptions.AuthorInfoResolvingExceptionTemplates.AUTHOR_NODE_MISSING_IN_REFERENCE;
import static pl.ds.kyanite.blogs.components.exceptions.AuthorInfoResolvingExceptionTemplates.CIRCULAR_REFERENCE;
import static pl.ds.kyanite.blogs.components.exceptions.AuthorInfoResolvingExceptionTemplates.COUNSUMER_IS_NULL;

import java.util.List;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.factory.ModelFactory;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pl.ds.kyanite.blogs.components.exceptions.AuthorInfoCircularReferenceException;
import pl.ds.kyanite.blogs.components.exceptions.AuthorInfoConfigurationException;
import pl.ds.kyanite.blogs.components.models.AuthorInfoModel;
import pl.ds.kyanite.blogs.components.services.impl.AuthorInfoResolverServiceImpl;

@ExtendWith(SlingContextExtension.class)
public class AuthorInfoResolverServiceTest {

  private static final String PATH = "/content/author-info";
  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  private AuthorInfoResolverService authorInfoResolver;
  
  //  nodes allowed to be passed into authorInfoResolver
  private final List<String> componentAllowedNodes = List.of("", "/author");
  private final List<String> pageAllowedNodes = List.of("", "/jcr:content", "/jcr:content/author");

  @BeforeEach
  public void reset() {
    context.addModelsForClasses(AuthorInfoModel.class);
    AuthorInfoResolverServiceImpl authorInfoResolverServiceImpl = new AuthorInfoResolverServiceImpl();
    authorInfoResolverServiceImpl.setModelFactory(context.getService(ModelFactory.class));
    authorInfoResolver = authorInfoResolverServiceImpl;
    authorInfoResolver = context.registerService(AuthorInfoResolverService.class, authorInfoResolver);
    context.load().json(
        requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream("author-info.json")),
        PATH);
  }

  private Resource getResource(String relativePath) {
    return context.resourceResolver().getResource(PATH + relativePath);
  }

  /*  Check routing START   */

  /*  In every node in sample data all the properties have the same unique prefix.
      By comparing that prefix with expected one we check if reference chain (defined by
      'authorInfoSource' and, in case of 'authorPage' reference, 'authorPageLink' field)
      resolves to correct AuthorInfo node */

  private void validateStringPropertyValue(
      String propertyValue, String expectedPrefix, String expectedSuffix) {
    assertNotNull(propertyValue);
    String[] tokens = propertyValue.split(" ");
    assertEquals(tokens[0], expectedPrefix);
    assertEquals(tokens.length, 2);
    assertEquals(tokens[1], expectedSuffix);
  }

  private void validateModel(AuthorInfoModel model, String expectedPrefix) {
    assertNotNull(model);
    validateStringPropertyValue(model.getAuthorName(),        expectedPrefix, "authorName");
    validateStringPropertyValue(model.getAuthorRole(),        expectedPrefix, "authorRole");
    validateStringPropertyValue(model.getAuthorDescription(), expectedPrefix, "authorDescription");
    validateStringPropertyValue(model.getAuthorPhoto(),       expectedPrefix, "authorPhoto");
    validateStringPropertyValue(model.getAuthorPhotoAlt(),    expectedPrefix, "authorPhotoAlt");
  }

  private void retrieveAndValidateModel(
      String authorNodeRelativePath,
      String expectedPropertyValuePrefix
  ) {
    Resource resource = getResource(authorNodeRelativePath);
    AuthorInfoModel model = authorInfoResolver.retrieveAuthorInfo(resource, context.resourceResolver());
    validateModel(model, expectedPropertyValuePrefix);
  }

  @Test
  public void routingToOwnPropertiesTest() {
    componentAllowedNodes.forEach(subPath -> retrieveAndValidateModel(
        "/pageWithOwnProperties/jcr:content/componentWithOwnProperties" + subPath,
        "componentWithOwnProperties"));
    pageAllowedNodes.forEach(subPath -> retrieveAndValidateModel(
        "/pageWithOwnProperties" + subPath,
        "pageWithOwnProperties"));
  }

  @Test
  public void routingToParentPageTest() {
    componentAllowedNodes.forEach(subPath -> retrieveAndValidateModel(
        "/pageWithOwnProperties/jcr:content/componentWithParentPageReference" + subPath,
        "pageWithOwnProperties"));
  }

  @Test
  public void routingToAuthorPageTest() {
    componentAllowedNodes.forEach(subPath -> retrieveAndValidateModel(
        "/pageWithOwnProperties/jcr:content/componentWithAuthorPageReference" + subPath,
        "authorBioPage"));
    pageAllowedNodes.forEach(subPath -> retrieveAndValidateModel(
        "/pageWithDoubleReference" + subPath,
        "authorBioPage"));
  }

  @Test
  public void doubleReferenceTest() {
    componentAllowedNodes.forEach(subPath -> retrieveAndValidateModel(
        "/pageWithDoubleReference/jcr:content/componentWithReferenceToAuthorPageViaParentPage" + subPath,
        "authorBioPage"));
  }

  /*  Check routing END   */


  /*  Check misconfigurations START   */

  private  <T extends Throwable> void validateException(
      String resourceRelativePath, String expectedExceptionTemplate, Class<T> expectedExceptionType) {
    Resource resource = getResource(resourceRelativePath);
    T e = assertThrows(
        expectedExceptionType,
        () -> authorInfoResolver.retrieveAuthorInfo(resource, context.resourceResolver())
    );
    System.out.println(e.getClass());
    String expectedRegex = expectedExceptionTemplate.replace("%s", ".+");
    assertLinesMatch(List.of(expectedRegex), List.of(e.getMessage()));
  }

  @Test
  public void authorNodeMissingInConsumerTest() {
    pageAllowedNodes.forEach(subPath -> validateException(
        "/pageWithAuthorNodeMissing" + subPath,
        "/jcr:content/author".equals(subPath) ? COUNSUMER_IS_NULL : AUTHOR_NODE_MISSING_IN_CONSUMER,
        AuthorInfoConfigurationException.class
    ));
  }

  @Test
  public void authorNodeMissingInReferenceTest() {
    List.of(
        "componentReferencingAuthorPageWithMissingAuthorNode",
        "componentReferencingParentPageWithMissingAuthorNode"
    ).forEach(
        component -> {
          componentAllowedNodes.forEach(subPath -> validateException(
              String.format("/pageWithAuthorNodeMissing/jcr:content/%s%s", component, subPath),
              AUTHOR_NODE_MISSING_IN_REFERENCE,
              AuthorInfoConfigurationException.class
          ));
        }
    );
  }

  @Test
  public void sourceTypeNotSetTest() {
    componentAllowedNodes.forEach(subPath -> validateException(
        "/pageWithAuthorNodeMissing/jcr:content/componentWithSourceTypeMissing" + subPath,
        AUTHOR_INFO_SOURCE_NOT_SET,
        AuthorInfoConfigurationException.class
    ));
  }

  @Test
  public void sourceTypeUnknownTest() {
    componentAllowedNodes.forEach(subPath -> validateException(
        "/pageWithAuthorNodeMissing/jcr:content/componentWithUnknownSourceType" + subPath,
        AUTHOR_INFO_SOURCE_TYPE_UNKNOWN,
        AuthorInfoConfigurationException.class
    ));
  }

  @Test
  public void sourcePathNotSetTest() {
    componentAllowedNodes.forEach(subPath -> validateException(
        "/pageWithAuthorNodeMissing/jcr:content/componentWithAuthorPageLinkMissing" + subPath,
        AUTHOR_INFO_SOURCE_PATH_NOT_SET,
        AuthorInfoConfigurationException.class
    ));
  }

  @Test
  public void circularReferenceDetectionTest() {
    List.of("a", "b", "c").forEach(
        page -> {
          pageAllowedNodes.forEach(subPath -> validateException(
              String.format("/circularReference/%s%s", page, subPath),
              CIRCULAR_REFERENCE,
              AuthorInfoCircularReferenceException.class
          ));
        }
    );
  }

  @Test
  public void sourceNotExists() {
    componentAllowedNodes.forEach(subPath -> validateException(
        "/pageWithAuthorNodeMissing/jcr:content/componentReferencingMissingPage" + subPath,
        AUTHOR_INFO_SOURCE_IS_NULL,
        AuthorInfoConfigurationException.class
    ));
  }

  /*  Check misconfigurations END   */

}
