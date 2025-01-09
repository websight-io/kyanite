/*
 * Copyright (C) 2025 Dynamic Solutions
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

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SlingContextExtension.class)
public class AccordionComponentTest {

  private static final String PATH = "/content/accordion";
  public static final String IS_5 = "is-5";

  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(AccordionComponent.class);
    context.addModelsForClasses(AccordionItemComponent.class);
    context.addModelsForClasses(ContentComponent.class);
    context.load().json(requireNonNull(
        Thread.currentThread().getContextClassLoader().getResourceAsStream("accordion.json")), PATH);
  }

  @Test
  void defaultAccordionComponentModelTest() {
    AccordionComponent model = context.resourceResolver().getResource(PATH + "/default")
        .adaptTo(AccordionComponent.class);
    assertThat(model).isNotNull();
    List<Resource> childrenComponents = model.getChildrenComponents();
    assertThat(childrenComponents).hasSize(2);
    AccordionItemComponent accordionItemComponent = childrenComponents.get(1).adaptTo(AccordionItemComponent.class);
    assertThat(accordionItemComponent).isNotNull();
    assertThat(accordionItemComponent.getClassList()).containsExactly(IS_5);
    assertThat(accordionItemComponent.getSize()).isEqualTo(IS_5);
    assertThat(accordionItemComponent.getTitle()).isEqualTo("Title2");
    Resource content = childrenComponents.get(0).getChild("content");
    ContentComponent contentComponent = content.adaptTo(ContentComponent.class);
    assertThat(contentComponent.getText()).isEqualTo("<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>");
  }
}
