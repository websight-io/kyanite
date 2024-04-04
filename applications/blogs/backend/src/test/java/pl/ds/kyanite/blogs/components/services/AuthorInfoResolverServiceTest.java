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
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.factory.ModelFactory;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pl.ds.kyanite.blogs.components.models.AuthorInfoModel;
import pl.ds.kyanite.blogs.components.services.impl.AuthorInfoResolverServiceImpl;

@ExtendWith(SlingContextExtension.class)
public class AuthorInfoResolverServiceTest {

  private static final String PATH = "/content/author-info";
  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  private AuthorInfoResolverService authorInfoResolver;

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
    Resource authorNode = getResource(authorNodeRelativePath);
    AuthorInfoModel model = authorInfoResolver.retrieveAuthorInfo(authorNode, context.resourceResolver());
    validateModel(model, expectedPropertyValuePrefix);
  }

  @Test
  public void routingToOwnPropertiesTest() {
    retrieveAndValidateModel(
        "/pageWithOwnProperties/jcr:content/componentWithOwnProperties/author",
        "componentWithOwnProperties");
    retrieveAndValidateModel(
        "/pageWithOwnProperties/jcr:content/author",
        "pageWithOwnProperties");
  }

  @Test
  public void routingToParentPageTest() {
    retrieveAndValidateModel(
        "/pageWithOwnProperties/jcr:content/componentWithParentPageReference/author",
        "pageWithOwnProperties");
  }

  @Test
  public void routingToAuthorPageTest() {
    retrieveAndValidateModel(
        "/pageWithOwnProperties/jcr:content/componentWithAuthorPageReference/author",
        "authorBioPage");
  }

  @Test
  public void doubleReferenceTest() {
    retrieveAndValidateModel(
        "/pageWithDoubleReference/jcr:content/componentWithReferenceToAuthorPageViaParentPage/author",
        "authorBioPage");
  }

  /*  Check routing END   */
}
