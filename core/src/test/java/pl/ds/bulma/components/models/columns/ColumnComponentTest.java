package pl.ds.bulma.components.models.columns;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
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
    context.load().json(requireNonNull(
        Thread.currentThread().getContextClassLoader().getResourceAsStream("column.json")), PATH);
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
    ColumnComponent model = context.resourceResolver().getResource(PATH + "/complex")
        .adaptTo(ColumnComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getChildrenComponents()).isEmpty();

    assertThat(model.getTabletColumnStyle().getSize()).isEqualTo(ColumnSizes.HALF.getCssClass());
    assertThat(model.getTabletColumnStyle().getSizeType()).isEqualTo("fraction");
    assertThat(model.getTabletColumnStyle().getOffset()).isEqualTo(
        ColumnSizes.FOUR.name().toLowerCase());
    assertThat(model.getTabletColumnStyle().getOffsetType()).isEqualTo("evenNumber");
    assertThat(model.getTabletColumnStyle().isNormalColumn()).isEqualTo(true);
    assertCommonComplexModelFields(model);
    assertThat(model.getClasses()).isEqualTo(expectedClasses);
  }

  @Test
  void nestedColumnsModelTest() {
    String[] expectedSecondNestedColumnClasses = new String[]{"is-6-tablet",
        "is-offset-four-fifths-tablet"};
    Mockito.when(columnClassProvider.getClasses(any()))
        .thenReturn(expectedSecondNestedColumnClasses);
    ColumnComponent model = context.resourceResolver().getResource(PATH + "/nested")
        .adaptTo(ColumnComponent.class);

    assertThat(model).isNotNull();
    assertCommonComplexModelFields(model);
    assertThat(model.getChildrenComponents().size()).isEqualTo(1);

    Resource nestedColumnsResource = model.getChildrenComponents().get(0);
    assertThat(nestedColumnsResource.adaptTo(ColumnsComponent.class)).isNotNull();
    assertThat(nestedColumnsResource.getResourceType()).isEqualTo("bulma/components/columns");

    List<ColumnComponent> nestedColumnList = StreamSupport
        .stream(nestedColumnsResource.getChildren().spliterator(), false)
        .map(it -> it.adaptTo(ColumnComponent.class))
        .collect(Collectors.toList());

    assertThat(nestedColumnList).isNotNull();
    assertThat(nestedColumnList.size()).isEqualTo(2);

    ColumnComponent nestedSecondColumn = nestedColumnList.get(1);
    assertThat(nestedSecondColumn.getTabletColumnStyle().getSize()).isEqualTo(
        ColumnSizes.SIX.getCssClass());
    assertThat(nestedSecondColumn.getTabletColumnStyle().getSizeType()).isEqualTo("evenNumber");
    assertThat(nestedSecondColumn.getTabletColumnStyle().getOffset()).isEqualTo(
        ColumnSizes.FOUR_FIFTHS.getCssClass());
    assertThat(nestedSecondColumn.getTabletColumnStyle().getOffsetType()).isEqualTo("fraction");
    assertThat(nestedSecondColumn.getTabletColumnStyle().isNormalColumn()).isEqualTo(true);
    assertCommonComplexModelFields(nestedSecondColumn);
    assertThat(nestedSecondColumn.getClasses()).isEqualTo(expectedSecondNestedColumnClasses);
  }

  private static void assertCommonComplexModelFields(ColumnComponent model) {
    assertThat(model).isNotNull();
    assertThat(model.getMobileColumnStyle().isNormalColumn()).isEqualTo(false);
    assertThat(model.getMobileColumnStyle().getOffset()).isBlank();
    assertThat(model.getMobileColumnStyle().getOffsetType()).isBlank();
    assertThat(model.getMobileColumnStyle().getSize()).isBlank();
    assertThat(model.getMobileColumnStyle().getSizeType()).isBlank();

    assertThat(model.getDesktopColumnStyle().isNormalColumn()).isEqualTo(false);
    assertThat(model.getDesktopColumnStyle().getOffset()).isBlank();
    assertThat(model.getDesktopColumnStyle().getOffsetType()).isBlank();
    assertThat(model.getDesktopColumnStyle().getSize()).isBlank();
    assertThat(model.getDesktopColumnStyle().getSizeType()).isBlank();

  }

}
