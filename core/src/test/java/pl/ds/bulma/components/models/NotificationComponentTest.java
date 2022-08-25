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
class NotificationComponentTest {

    private static final String PATH = "/content/notification";
    private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

    @BeforeEach
    public void init() {
        context.addModelsForClasses(NotificationComponent.class);
        context.load().json(requireNonNull(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("notification.json")), PATH);
    }

    @Test
    void defaultNotificationComponentModelTest() {
        NotificationComponent model = context.resourceResolver().getResource(PATH + "/default").adaptTo(NotificationComponent.class);

        assertThat(model).isNotNull();
        assertThat(model.getContent()).isNull();
        assertThat(model.getVariant()).isNull();
        assertThat(model.getShowButton()).isFalse();
        assertThat(model.isLight()).isFalse();
        assertThat(model.getNotificationClasses()).isEmpty();
    }

    @Test
    void notificationComponentModelTest() {
        NotificationComponent model = context.resourceResolver().getResource(PATH + "/styled").adaptTo(NotificationComponent.class);

        assertThat(model).isNotNull();
        assertThat(model.getContent()).isEqualTo("Content");
        assertThat(model.getVariant()).isEqualTo("is-info");
        assertThat(model.isLight()).isTrue();
        assertThat(model.getShowButton()).isTrue();
    }
}
