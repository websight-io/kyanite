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
class MessageComponentTest {

    private static final String PATH = "/content/message";
    private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

    @BeforeEach
    public void init() {
        context.addModelsForClasses(MessageComponent.class);
        context.load().json(requireNonNull(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("message.json")), PATH);
    }

    @Test
    void defaultMessageComponentModelTest() {
        MessageComponent model = context.resourceResolver().getResource(PATH + "/default").adaptTo(MessageComponent.class);

        assertThat(model).isNotNull();
        assertThat(model.getHeader()).isNull();
        assertThat(model.getContent()).isNull();
        assertThat(model.getVariant()).isNull();
        assertThat(model.getSize()).isNull();
        assertThat(model.getButton()).isNull();
        assertThat(model.getShowHeader()).isNull();
        assertThat(model.getMessageClasses()).isEmpty();
    }

    @Test
    void cardComponentModelTest() {
        MessageComponent model = context.resourceResolver().getResource(PATH + "/styled").adaptTo(MessageComponent.class);

        assertThat(model).isNotNull();
        assertThat(model.getHeader()).isEqualTo("Header");
        assertThat(model.getContent()).isEqualTo("Content");
        assertThat(model.getVariant()).isEqualTo("is-info");
        assertThat(model.getSize()).isEqualTo("is-small");
        assertThat(model.getButton()).isEqualTo("true");
        assertThat(model.getShowHeader()).isEqualTo("true");
        assertThat(model.getMessageClasses()).containsExactlyInAnyOrder("is-small", "is-info");
    }
}
