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
class HeroComponentTest {
    private static final String PATH = "/content/hero";
    private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

    @BeforeEach
    public void init() {
        context.addModelsForClasses(HeroComponent.class);
        context.load().json(requireNonNull(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("hero.json")), PATH);
    }

    @Test
    void defaultHero() {
        HeroComponent model = requireNonNull(
                context.resourceResolver().getResource(PATH + "/default")).adaptTo(HeroComponent.class);

        assertThat(model).isNotNull();
        assertThat(model.getTitle()).isEqualTo("Default title");
        assertThat(model.getSubTitle()).isNull();
        assertThat(model.getSize()).isNull();
        assertThat(model.getVariant()).isNull();
        assertThat(model.getHeroClasses()).isEmpty();
    }

    @Test
    void onlySizeClass() {
        HeroComponent model = requireNonNull(
                context.resourceResolver().getResource(PATH + "/onlySize")).adaptTo(HeroComponent.class);

        assertThat(model).isNotNull();
        assertThat(model.getTitle()).isEqualTo("Nice title");
        assertThat(model.getSubTitle()).isEqualTo("Subtitle");
        assertThat(model.getSize()).isEqualTo("is-medium");
        assertThat(model.getVariant()).isNull();
        assertThat(model.getHeroClasses()).containsExactlyInAnyOrder("is-medium");
    }

    @Test
    void bothStyles() {
        HeroComponent model = requireNonNull(
                context.resourceResolver().getResource(PATH + "/bothStyles")).adaptTo(HeroComponent.class);

        assertThat(model).isNotNull();
        assertThat(model.getTitle()).isEqualTo("Nice title");
        assertThat(model.getSubTitle()).isEqualTo("Subtitle");
        assertThat(model.getSize()).isEqualTo("is-medium");
        assertThat(model.getVariant()).isEqualTo("is-warning");
        assertThat(model.getHeroClasses()).containsExactlyInAnyOrder("is-medium", "is-warning");
    }

    @Test
    void onlyVariantClass() {
        HeroComponent model = requireNonNull(
                context.resourceResolver().getResource(PATH + "/onlyVariant")).adaptTo(HeroComponent.class);

        assertThat(model).isNotNull();
        assertThat(model.getTitle()).isEqualTo("Nice title");
        assertThat(model.getSubTitle()).isEqualTo("Subtitle");
        assertThat(model.getSize()).isNull();
        assertThat(model.getVariant()).isEqualTo("is-warning");
        assertThat(model.getHeroClasses()).containsExactlyInAnyOrder( "is-warning");
    }

}