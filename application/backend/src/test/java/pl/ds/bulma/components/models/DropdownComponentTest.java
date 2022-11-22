package pl.ds.bulma.components.models;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Predicate;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SlingContextExtension.class)
class DropdownComponentTest {

  private static final String PATH = "/content/dropdown";

  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(DropdownComponent.class);
    context.load().json(requireNonNull(
        Thread.currentThread().getContextClassLoader().getResourceAsStream("dropdown.json")), PATH);
  }

  @Test
  void defaultDropdownComponentModelTest() {
    DropdownComponent model = context.resourceResolver().getResource(PATH + "/default")
        .adaptTo(DropdownComponent.class);
    assertThat(model).isNotNull();
    assertThat(model.getLabel()).isEqualTo("Label");
    assertThat(model.getDropdownClasses()).isEmpty();
    assertThat(model.getItems()).isNull();
  }

  @Test
  void dropdownComponentTest() {
    DropdownComponent model = context.resourceResolver().getResource(PATH + "/complex")
        .adaptTo(DropdownComponent.class);
    assertThat(model.getLabel()).isEqualTo("Dropdown");
    assertThat(model.getDropdownClasses()).containsExactlyInAnyOrder("is-hoverable", "is-up",
        "is-right");
    assertThat(model.getItems().stream()
        .filter(i -> i.getLabel().equals("Item 1"))
        .filter(i -> i.getUrl().equals("url1"))
        .filter(i -> i.isHasDivider())
        .count()).isEqualTo(1);
    assertThat(model.getItems().stream()
        .filter(i -> i.getLabel().equals("Item 2"))
        .filter(i -> i.getUrl().equals("url2"))
        .filter(i -> !i.isHasDivider())
        .count()).isEqualTo(1);

  }

}
