package pl.ds.howlite.components.models;

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
class ButtonComponentTest {

    private static final String PATH = "/content/button";
    private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

    @BeforeEach
    public void init() {
        context.addModelsForClasses(ButtonComponent.class);
        context.load().json(requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream("button.json")), PATH);
    }

    @Test
    void defaultButtonComponentModelTest() {
        ButtonComponent model = context.resourceResolver().getResource(PATH + "/default").adaptTo(ButtonComponent.class);

        assertThat(model).isNotNull();
        assertThat(model.getLabel()).isEqualTo("Label");
        assertThat(model.getType()).isEqualTo("button");
        assertThat(model.getVariant()).isEqualTo("is-primary");
        assertThat(model.getSize()).isEqualTo("is-normal");

    }

    @Test
    void buttonComponentModelTest() {
        ButtonComponent model = context.resourceResolver().getResource(PATH + "/complex").adaptTo(ButtonComponent.class);

        assertThat(model).isNotNull();
        assertThat(model.getLabel()).isEqualTo("Button");
        assertThat(model.getType()).isEqualTo("anchor");
        assertThat(model.getVariant()).isEqualTo("is-white");
        assertThat(model.getSize()).isEqualTo("is-medium");
    }
}
