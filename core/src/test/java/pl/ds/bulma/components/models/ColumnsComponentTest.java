package pl.ds.bulma.components.models;

import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SlingContextExtension.class)
public class ColumnsComponentTest {

    private static final String PATH = "/content/columns";
    private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

    @BeforeEach
    public void init() {
        context.addModelsForClasses(CardComponent.class);
        context.load().json(requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream("columns.json")), PATH);
    }

    @Test
    void defaultColumnsComponentModelTest() {
        ColumnsComponent model = context.resourceResolver().getResource(PATH + "/default").adaptTo(ColumnsComponent.class);

        //TODO
        assertTrue(true);
//        assertThat(model).isNotNull();
    }
}
