/*
 * <!--
 *     Copyright (C) 2022 Dynamic Solutions
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 * -->
 */

package pl.ds.bulma.components.models;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SlingContextExtension.class)
class PricingPlanComponentTest {

  private static final String PATH = "/content/pricingplan";
  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(PricingPlanComponent.class);
    context.load().json(requireNonNull(
            Thread.currentThread().getContextClassLoader().getResourceAsStream("pricingplan.json")),
        PATH);
  }

  @Test
  void defaultPricingPlanComponentModelTest() {
    PricingPlanComponent model = context.resourceResolver().getResource(PATH + "/default")
        .adaptTo(PricingPlanComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getHeader()).isEqualTo("Header");
    assertThat(model.getShowPricing()).isTrue();
    assertThat(model.getPrice()).isEqualTo(0);
    assertThat(model.getCurrency()).isEqualTo("$");
    assertThat(model.getPeriod()).isEqualTo("monthly");
    assertThat(model.getVariant()).isNull();
    assertThat(model.getDetails()).isNull();
    assertThat(model.getShowButton()).isTrue();
    assertThat(model.getButtonLabel()).isEqualTo("Choose");
  }

  @Test
  void pricingPlanComponentModelTest() {
    PricingPlanComponent model = context.resourceResolver().getResource(PATH + "/complex")
        .adaptTo(PricingPlanComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getHeader()).isEqualTo("Header");
    assertThat(model.getShowPricing()).isFalse();
    assertThat(model.getPrice()).isEqualTo(3);
    assertThat(model.getCurrency()).isEqualTo("zl");
    assertThat(model.getPeriod()).isEqualTo("weekly");
    assertThat(model.getVariant()).isEqualTo("is-info");
    assertThat(model.getDetails().stream()
        .filter(i -> i.getDescription().equals("Description item 1"))
        .count()).isEqualTo(1);
    assertThat(model.getDetails().stream()
        .filter(i -> i.getDescription().equals("Description item 2"))
        .count()).isEqualTo(1);
    assertThat(model.getShowButton()).isFalse();
    assertThat(model.getButtonLabel()).isEqualTo("Nice");
  }
}
