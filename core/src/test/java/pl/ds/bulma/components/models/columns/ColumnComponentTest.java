package pl.ds.bulma.components.models.columns;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SlingContextExtension.class)
public class ColumnComponentTest {

    private static final String PATH = "/content/columns/column";

    private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

    @BeforeEach
    public void init() {
        context.addModelsForClasses(ColumnComponent.class);
        context.load().json(requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream("column.json")), PATH);
    }

    @Test
    void defaultComponentModelTest() {
        ColumnComponent model = context.resourceResolver().getResource(PATH + "/default").adaptTo(ColumnComponent.class);

        assertThat(model).isNotNull();
        assertThat(model.getSizeType()).isEqualTo(StringUtils.EMPTY);
        assertThat(model.getOffsetType()).isEqualTo(StringUtils.EMPTY);
        assertThat(model.getClasses()).isEmpty();
    }

    @Test
    void columnComponentModelWithSizeAndOffsetTest() {
        ColumnComponent model = context.resourceResolver().getResource(PATH + "/complex").adaptTo(ColumnComponent.class);

        assertThat(model).isNotNull();
        assertThat(model.getSizeType()).isEqualTo("fraction");
        assertThat(model.getOffsetType()).isEqualTo("evenNumber");
        assertThat(model.getClasses()).isEqualTo(new String[]{"is-half", "is-offset-4"});
    }

    @Test
    void simpleNarrowColumnComponentModelTest() {
        ColumnComponent model = context.resourceResolver().getResource(PATH + "/narrow").adaptTo(ColumnComponent.class);

        assertThat(model).isNotNull();
        assertThat(model.getSizeType()).isEqualTo(StringUtils.EMPTY);
        assertThat(model.getOffsetType()).isEqualTo(StringUtils.EMPTY);
        assertThat(model.getClasses()).isEqualTo(new String[]{"is-narrow"});
    }
}
