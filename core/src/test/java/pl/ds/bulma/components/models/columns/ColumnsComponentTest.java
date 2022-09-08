package pl.ds.bulma.components.models.columns;

import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SlingContextExtension.class)
public class ColumnsComponentTest {

    private static final String PATH = "/content/columns";
    private static final String SLING_RESOURCE_TYPE = "sling:resourceType";

    private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

    @BeforeEach
    public void init() {
        context.addModelsForClasses(ColumnsComponent.class);
        context.load().json(requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream("columns.json")), PATH);
    }

    @Test
    void defaultColumnsComponentModelTest() {
        ColumnsComponent model = context.resourceResolver().getResource(PATH + "/default").adaptTo(ColumnsComponent.class);

        assertThat(model).isNotNull();
    }

    @Test
    void complexColumnsComponentModelTest() {
        String[] expectedClasses = new String[]{"is-variable", "is-0-mobile", "is-2-tablet", "is-8-desktop", "is-mobile"};
        ColumnsComponent model = context.resourceResolver().getResource(PATH + "/complex").adaptTo(ColumnsComponent.class);

        assertThat(model).isNotNull();
        assertThat(model.getColumnsActivationLevel()).isEqualTo("is-mobile");
        assertThat(model.isCustomGapLevel()).isTrue();
        assertThat(model.getMobileGapLevel()).isEqualTo("is-0");
        assertThat(model.getTabletGapLevel()).isEqualTo("is-2");
        assertThat(model.getDesktopGapLevel()).isEqualTo("is-8");
        assertThat(model.getClasses()).containsExactly(expectedClasses);
    }

    @Test
    void hasOnlyColumnsWhichHasNoChildrenWorksWhenChildColumnIsEmptyTest() {
        addChildResourceType("column1", PATH + "/default", "bulma/components/columns/column");
        ColumnsComponent model = context.resourceResolver().getResource(PATH + "/default").adaptTo(ColumnsComponent.class);

        assertThat(model).isNotNull();
        assertTrue(model.hasOnlyColumnsWhichHasNoChildren());
    }

    @Test
    void hasOnlyColumnsWhichHasNoChildrenWorksWhenChildColumnIsNotEmptyTest() {
        addChildResourceType("column1", PATH + "/default", "bulma/components/columns/column");
        addChildResourceType("button", PATH + "/default/column1","bulma/components/button");
        ColumnsComponent model = context.resourceResolver().getResource(PATH + "/default").adaptTo(ColumnsComponent.class);

        assertThat(model).isNotNull();
        assertFalse(model.hasOnlyColumnsWhichHasNoChildren());
    }

    private void addChildResourceType(String childResourceName, String resourcePath, String childResourceTypeValue) {
        Map<String, String> childrenValues = new HashMap<>();
        childrenValues.put(SLING_RESOURCE_TYPE, childResourceTypeValue);
        context.build().resource(resourcePath).siblingsMode().resource(childResourceName, childrenValues);
    }
}
