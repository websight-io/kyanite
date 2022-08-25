package pl.ds.bulma.components.models;

import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SlingContextExtension.class)
class PricingTableComponentTest {

    private static final String PATH = "/content/pricingtable";
    private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

    @BeforeEach
    public void init() {
        context.addModelsForClasses(PricingTableComponent.class);
        context.load().json(requireNonNull(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("pricingtable.json")), PATH);
    }

    @Test
    void defaultPricingTableComponentModelTest() {
        PricingTableComponent model = context.resourceResolver().getResource(PATH + "/default").adaptTo(PricingTableComponent.class);

        assertThat(model).isNotNull();
        assertThat(model.getPricingClasses()).isEmpty();
        assertThat(model.isComparative()).isFalse();
        assertThat(model.isHorizontal()).isFalse();
    }

    @Test
    void horizontalPricingTableModelTest() {
        PricingTableComponent model = context.resourceResolver().getResource(PATH + "/horizontal").adaptTo(PricingTableComponent.class);


        assertThat(model).isNotNull();
        assertThat(model.getPricingClasses()).containsExactlyInAnyOrder("is-horizontal");
        assertThat(model.isComparative()).isFalse();
        assertThat(model.isHorizontal()).isTrue();
    }

    @Test
    void comparativePricingTableModelTest() {
        PricingTableComponent model = context.resourceResolver().getResource(PATH + "/comparative").adaptTo(PricingTableComponent.class);

        assertThat(model).isNotNull();
        assertThat(model.getPricingClasses()).containsExactlyInAnyOrder("is-comparative");
        assertThat(model.isComparative()).isTrue();
        assertThat(model.isHorizontal()).isFalse();
    }

    @Test
    void styledPricingTableModelTest() {
        PricingTableComponent model = context.resourceResolver().getResource(PATH + "/bothStyles").adaptTo(PricingTableComponent.class);

        assertThat(model).isNotNull();
        assertThat(model.getPricingClasses()).containsExactlyInAnyOrder("is-comparative", "is-horizontal");
        assertThat(model.isComparative()).isTrue();
        assertThat(model.isHorizontal()).isTrue();
    }
}
