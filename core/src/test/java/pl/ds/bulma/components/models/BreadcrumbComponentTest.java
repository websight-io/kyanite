package pl.ds.bulma.components.models;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SlingContextExtension.class)
class BreadcrumbComponentTest {

  private static final String PATH = "/content/breadcrumb";
  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(BreadcrumbComponent.class);
    context.load().json(requireNonNull(
            Thread.currentThread().getContextClassLoader().getResourceAsStream("breadcrumb.json")),
        PATH);
  }

  @Test
  void defaultBreadcrumbComponentModelTest() {
    BreadcrumbComponent model = context.resourceResolver().getResource(PATH + "/default")
        .adaptTo(BreadcrumbComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getAlignment()).isEmpty();
    assertThat(model.getSize()).isEmpty();
    assertThat(model.getSeparator()).isEmpty();
    assertThat(model.getClasses()).isEmpty();
    assertThat(model.getElements()).isNull();
  }

  @Test
  void breadcrumbComponentModelTest() {
    BreadcrumbComponent model = context.resourceResolver().getResource(PATH + "/complex")
        .adaptTo(BreadcrumbComponent.class);

    assertThat(model.getAlignment()).isEqualTo("center");
    assertThat(model.getSize()).isEqualTo("is-small");
    assertThat(model.getSeparator()).isEqualTo("has-bullet-separator");
    assertThat(model.getClasses()).containsExactlyInAnyOrder("center", "is-small",
        "has-bullet-separator");
    assertThat(model.getElements().stream()
        .filter(i -> i.getLabel().equals("Test1"))
        .count()).isEqualTo(1);
    assertThat(model.getElements().stream()
        .filter(i -> i.getLabel().equals("Test2")));
  }
}
