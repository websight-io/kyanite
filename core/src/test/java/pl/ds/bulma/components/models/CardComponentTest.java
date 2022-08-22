package pl.ds.bulma.components.models;

import org.apache.commons.lang3.StringUtils;
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
class CardComponentTest {

    private static final String PATH = "/content/card";
    private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

    @BeforeEach
    public void init() {
        context.addModelsForClasses(CardComponent.class);
        context.load().json(requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream("card.json")), PATH);
    }

    @Test
    void defaultCardComponentModelTest() {
        CardComponent model = context.resourceResolver().getResource(PATH + "/default").adaptTo(CardComponent.class);

        assertThat(model).isNotNull();
        assertThat(model.getContent()).isEqualTo("Card content");
        assertThat(model.getHeader()).isEqualTo(StringUtils.EMPTY);
        assertThat(model.getTitle()).isEqualTo(StringUtils.EMPTY);
        assertThat(model.getSubtitle()).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    void buttonComponentModelTest() {
        CardComponent model = context.resourceResolver().getResource(PATH + "/complex").adaptTo(CardComponent.class);

        assertThat(model).isNotNull();
        assertThat(model).isNotNull();
        assertThat(model.getContent()).isEqualTo("Lorem ipsum");
        assertThat(model.getHeader()).isEqualTo("Card header");
        assertThat(model.getTitle()).isEqualTo("Title");
        assertThat(model.getSubtitle()).isEqualTo("Subtitle");
    }
}
