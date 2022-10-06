package pl.ds.bulma.components.models.level;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(SlingContextExtension.class)
class LevelItemComponentTest {


  private static final String PATH = "/content/level";
  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(LevelItemComponent.class, LevelComponent.class, PositionedLevelComponent.class);
    context.load().json(requireNonNull(
        Thread.currentThread().getContextClassLoader().getResourceAsStream("levelitem.json")), PATH);
  }

  @Test
  void levelItemOnLevel() {
    LevelItemComponent model = requireNonNull(
        getResource("/onLevel/firstItem")).adaptTo(LevelItemComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getLevelItemStyle()).isEqualTo("has-text-centered");
    assertThat(model.getElementType()).isEqualTo("div");
  }

  @Test
  void levelItemOnPositionedLevel() {
    LevelItemComponent model = requireNonNull(
        getResource("/onPositionedLevel/firstItem")).adaptTo(LevelItemComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getLevelItemStyle()).isEmpty();
    assertThat(model.getElementType()).isEqualTo("p");
  }

  @Nullable
  private Resource getResource(String variant) {
    return context.resourceResolver().getResource(PATH + variant);
  }

}