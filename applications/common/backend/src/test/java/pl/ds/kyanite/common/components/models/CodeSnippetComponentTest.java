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

package pl.ds.kyanite.common.components.models;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SlingContextExtension.class)
class CodeSnippetComponentTest {

    private static final String PATH = "/content";

    private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

    @BeforeEach
    public void init() {
        context.addModelsForClasses(CodeSnippetComponent.class);
    }

    @Test
    void defaultCodeSnippetComponentModelTest() {
        // given
        context.create().resource(PATH);

        // when
        CodeSnippetComponent component = retrieveResourceAsComponent(PATH);

        // then
        assertThat(component).isNotNull();
        assertThat(component.getTitle()).isNull();
        assertThat(component.getFileName()).isNull();
        assertThat(component.getFileType()).isNull();
        assertThat(component.getCode()).isNull();
        assertThat(component.getIsExpandingOn()).isNull();
        assertThat(component.getIsInitiallyExpanded()).isNull();
    }

    @Test
    void filledCodeSnippetComponentModelTest() {
        // given
        context.create().resource(PATH, Map.of(
                "title", "Some Title",
                "fileName", "File 1.txt",
                "fileType", "Text file",
                "code", "ABC",
                "isExpandingOn", true,
                "isInitiallyExpanded", false
        ));

        // when
        CodeSnippetComponent component = retrieveResourceAsComponent(PATH);

        // then
        assertThat(component).isNotNull();
        assertThat(component.getTitle()).isEqualTo("Some Title");
        assertThat(component.getFileName()).isEqualTo("File 1.txt");
        assertThat(component.getFileType()).isEqualTo("Text file");
        assertThat(component.getCode()).isEqualTo("ABC");
        assertThat(component.getIsExpandingOn()).isTrue();
        assertThat(component.getIsInitiallyExpanded()).isFalse();
    }

    private CodeSnippetComponent retrieveResourceAsComponent(String path) {
        return context
                .resourceResolver()
                .getResource(path)
                .adaptTo(CodeSnippetComponent.class);
    }
}