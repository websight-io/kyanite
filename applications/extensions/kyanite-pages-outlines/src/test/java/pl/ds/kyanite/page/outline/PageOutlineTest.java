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

package pl.ds.kyanite.page.outline;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.Test;


class PageOutlineTest {

  @Test
  void shouldBuildProperOutlinePath() {
    Resource resource = mock(Resource.class);
    when(resource.getPath()).thenReturn("/content/page-outlines-space/pages/test/page");

    PageOutline pageOutline = PageOutline.from(resource, "thumbnail-outline")
        .orElseThrow();

    assertThat(pageOutline.getOutlinePagePath())
        .isEqualTo("/content/page-outlines-space/pages/outlines/thumbnail-outline/test-page");
  }

}