package pl.ds.bulma.components.models;

import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pl.ds.bulma.components.models.ButtonComponent;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SlingContextExtension.class)
class TagComponentTest {

    private static final String PATH = "/content/tag";
    private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

    @BeforeEach
    public void init() {
        context.addModelsForClasses(TagComponent.class);
        context.load().json(requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream("tag.json")), PATH);
    }

    @Test
    void defaultTagComponentModelTest() {
        TagComponent model = context.resourceResolver().getResource(PATH + "/default").adaptTo(TagComponent.class);

        assertThat(model).isNotNull();
        assertThat(model.getLabel()).isEqualTo("Label");
        assertThat(model.getVariant()).isEqualTo("is-primary");
        assertThat(model.getSize()).isEqualTo("is-normal");

    }

    @Test
    void tagComponentModelTest() {
        TagComponent model = context.resourceResolver().getResource(PATH + "/complex").adaptTo(TagComponent.class);

        assertThat(model).isNotNull();
        assertThat(model.getLabel()).isEqualTo("Button");
        assertThat(model.getVariant()).isEqualTo("is-white");
        assertThat(model.getSize()).isEqualTo("is-medium");
    }
}
