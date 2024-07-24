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

package pl.ds.kyanite.common.components.utils;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.function.Function;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import pl.ds.websight.assets.core.api.Asset;
import pl.ds.websight.assets.core.api.Rendition;

@ExtendWith(SlingContextExtension.class)
class LinkUtilTest {

  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  private final Asset assetMock = Mockito.mock(Asset.class);
  private final Rendition renditionMock = Mockito.mock(Rendition.class);

  @BeforeEach
  public void init() {
    final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
    context.load()
        .json(requireNonNull(contextClassLoader.getResourceAsStream("links/pages.json")),
            "/content/test/pages");
    context.load()
        .json(requireNonNull(contextClassLoader.getResourceAsStream("links/assets.json")),
            "/content/test/assets");
    context.load()
        .json(requireNonNull(contextClassLoader.getResourceAsStream("links/web-resources.json")),
            "/libs/kyanite/webroot");

    context.registerAdapter(Resource.class, Asset.class,
        (Function<Resource, Asset>) (Resource adaptable) -> {
          if ("/content/test/assets/PortfolioForum.png".equals(adaptable.getPath())) {
            when(assetMock.getOriginalRendition()).thenReturn(renditionMock);
            when(renditionMock.getPath()).thenReturn(
                "/content/test/assets/PortfolioForum.png/jcr:content/renditions/original.png");

            return assetMock;
          } else {
            return null;
          }
        });
  }

  @Test
  public void shouldProduceCorrectLinkWhenPageExists() {
    assertLink("/content/test/pages/links/page",
        "/content/test/pages/links/page.html");
  }

  @Test
  public void shouldProduceCorrectLinkForPublishedPagesWhenPageExists() {
    assertLink("/published/test/pages/links/page",
        "/published/test/pages/links/page.html");
  }

  @Test
  public void shouldNotAddHtmlExtensionWhenPageDoesNotExist() {
    assertLink("/content/test/pages/links/non-existent-page",
        "/content/test/pages/links/non-existent-page");
  }

  @Test
  public void shouldProduceCorrectLinkForAnchors() {
    assertLink("#i-am-an-anchor", "#i-am-an-anchor");
  }

  @Test
  public void shouldProduceCorrectLinkToOriginalRenditionWhenAssetExists() {
    assertLink("/content/test/assets/PortfolioForum.png",
        "/content/test/assets/PortfolioForum.png/jcr:content/renditions/original.png");

    verify(assetMock, times(2)).getOriginalRendition();
    verify(renditionMock).getPath();
  }

  @Test
  public void shouldProduceOriginalLinkWhenAssetDoesNotExist() {
    assertLink("/content/test/assets/no-such-asset.png",
        "/content/test/assets/no-such-asset.png");

    verifyNoInteractions(assetMock);
    verifyNoInteractions(renditionMock);
  }

  @Test
  public void shouldNotRewriteLinksForWebResources() {
    assertLink("/libs/test/webroot/image.png",
        "/libs/test/webroot/image.png");
  }

  private void assertLink(final String path, final String expectedLink) {
    final String actualLink = LinkUtil.handleLink(path, context.resourceResolver());

    assertThat(actualLink).isNotNull();
    assertThat(actualLink).isEqualTo(expectedLink);
  }
}
