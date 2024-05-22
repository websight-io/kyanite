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

package groovyscripttests;

import static org.assertj.core.api.Assertions.assertThat;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.builder.ImmutableValueMap;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.junit.jupiter.api.Test;

public class RenameTablecellColspanRowspanToColumnsRowsTest {

    private static final File SCRIPT_FILE = new File(
            "src/main/groovy/0.5.1/rename-tablecell-colspan-rowspan-to-columns-rows.groovy"
    );

    private final SlingContext context = new SlingContext(ResourceResolverType.JCR_OAK);

    @Test
    public void testScriptExecution() throws IOException {
        // given
        ResourceResolver resourceResolver = context.resourceResolver();

        Resource tableCell = context.create().resource("/content/tablecell", ImmutableValueMap.of(
                "sling:resourceType", "kyanite/common/components/table/tablecell",
                "colspan", 2L,
                "rowspan", 3L
        ));

        Resource nestedTableCell = context.create().resource("/content/nested-tablecell", ImmutableValueMap.of(
                "sling:resourceType", "kyanite/common/components/table/tablecell",
                "colspan", 5L,
                "rowspan", 6L
        ));

        Resource divCell = context.create().resource("/content/divcell", ImmutableValueMap.of(
                "sling:resourceType", "kyanite/common/components/divcell",
                "colspan", 8L,
                "rowspan", 9L
        ));
        resourceResolver.commit();

        // when: execute script
        Binding binding = new Binding();
        binding.setVariable("resourceResolver", resourceResolver);
        GroovyShell shell = new GroovyShell(binding);

        String script = FileUtils.readFileToString(SCRIPT_FILE, StandardCharsets.UTF_8);
        shell.evaluate(script);
        resourceResolver.commit();

        // then
        Resource tableCellAfterwards = resourceResolver.getResource(tableCell.getPath());
        assertThat(tableCellAfterwards.getValueMap())
                .containsEntry("columns", 2L)
                .containsEntry("rows", 3L)
                .doesNotContainKeys("colspan", "rowspan");

        Resource nestedTableCellAfterwards = resourceResolver.getResource(nestedTableCell.getPath());
        assertThat(nestedTableCellAfterwards.getValueMap())
                .containsEntry("columns", 5L)
                .containsEntry("rows", 6L)
                .doesNotContainKeys("colspan", "rowspan");

        Resource divCellAfterwards = resourceResolver.getResource(divCell.getPath());
        assertThat(divCellAfterwards.getValueMap())
                .containsEntry("colspan", 8L)
                .containsEntry("rowspan", 9L)
                .doesNotContainKeys("columns", "rows");

        // cleanup
        resourceResolver.delete(tableCellAfterwards);
        resourceResolver.delete(nestedTableCellAfterwards);
        resourceResolver.delete(divCellAfterwards);
    }
}