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
class PricingDetailsComponentTest {

  private static final String PATH = "/content/pricingdetails";
  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(PricingDetailsComponent.class);
    context.load().json(requireNonNull(
            Thread.currentThread().getContextClassLoader().getResourceAsStream("pricingdetails.json")),
        PATH);
  }

  @Test
  void defaultPricingDetailsComponentModelTest() {
    PricingDetailsComponent model = context.resourceResolver().getResource(PATH + "/default")
        .adaptTo(PricingDetailsComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getDescription()).isNull();
  }

  @Test
  void pricingDetailsComponentModelTest() {
    PricingDetailsComponent model = context.resourceResolver().getResource(PATH + "/filled")
        .adaptTo(PricingDetailsComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getDescription()).isEqualTo("Cheap plan");
  }
}
