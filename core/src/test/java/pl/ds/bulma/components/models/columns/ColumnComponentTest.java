package pl.ds.bulma.components.models.columns;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.ds.bulma.components.services.ColumnClassProvider;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith({SlingContextExtension.class, MockitoExtension.class})
public class ColumnComponentTest {

    private static final String PATH = "/content/columns/column";

    private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

    @Mock
    private ColumnClassProvider columnClassProvider;

    @BeforeEach
    public void init() {
        context.addModelsForClasses(ColumnComponent.class, ResponsiveColumnStyle.class);
        context.registerService(ColumnClassProvider.class, columnClassProvider);
        context.load().json(requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream("column.json")), PATH);
    }

    @Test
    void defaultComponentModelTest() {
        Mockito.when(columnClassProvider.getClasses(any())).thenReturn(new String[]{});
        Resource resource = context.resourceResolver().getResource(PATH + "/default");
        ColumnComponent model = resource.adaptTo(ColumnComponent.class);

        assertThat(model).isNotNull();
        assertThat(model.getClasses()).isEmpty();
    }

    @Test
    void complexModelTest() {
        String[] expectedClasses = new String[]{"is-half-tablet", "is-offset-4-tablet"};
        Mockito.when(columnClassProvider.getClasses(any())).thenReturn(expectedClasses);
        ColumnComponent model = context.resourceResolver().getResource(PATH + "/complex").adaptTo(ColumnComponent.class);

        assertThat(model).isNotNull();
        assertThat(model.getChildrenComponents()).isEmpty();
        assertThat(model.getMobileColumnStyle().isNarrowColumn()).isEqualTo(true);
        assertThat(model.getMobileColumnStyle().getOffset()).isBlank();
        assertThat(model.getMobileColumnStyle().getOffsetType()).isBlank();
        assertThat(model.getMobileColumnStyle().getSize()).isBlank();
        assertThat(model.getMobileColumnStyle().getSizeType()).isBlank();

        assertThat(model.getTabletColumnStyle().isNarrowColumn()).isEqualTo(false);
        assertThat(model.getTabletColumnStyle().getOffset()).isEqualTo("four");
        assertThat(model.getTabletColumnStyle().getOffsetType()).isEqualTo("evenNumber");
        assertThat(model.getTabletColumnStyle().getSize()).isEqualTo("half");
        assertThat(model.getTabletColumnStyle().getSizeType()).isEqualTo("fraction");

        assertThat(model.getDesktopColumnStyle().isNarrowColumn()).isEqualTo(false);
        assertThat(model.getDesktopColumnStyle().getOffset()).isBlank();
        assertThat(model.getDesktopColumnStyle().getOffsetType()).isBlank();
        assertThat(model.getDesktopColumnStyle().getSize()).isBlank();
        assertThat(model.getDesktopColumnStyle().getSizeType()).isBlank();


        assertThat(model.getClasses()).isEqualTo(expectedClasses);
    }

}
