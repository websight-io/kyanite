package pl.ds.bulma.components.models.level;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.ds.bulma.components.models.level.LevelComponent.LEVEL_ITEM_COMPONENT_RESOURCE_TYPE;
import static pl.ds.bulma.components.models.level.LevelComponent.POSITIONED_LEVEL_COMPONENT_RESOURCE_TYPE;

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
class PositionedLevelComponentTest {


  private static final String PATH = "/content/positionedlevel";
  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(PositionedLevelComponent.class);
    context.load().json(requireNonNull(
        Thread.currentThread().getContextClassLoader().getResourceAsStream("positionedlevel.json")), PATH);
  }

  @Test
  void defaultLevel() {
    PositionedLevelComponent model = requireNonNull(
        getResource("/default")).adaptTo(PositionedLevelComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getPositionedStyle()).isEqualTo("level-left");
  }

  @Test
  void positionedLevel() {
    PositionedLevelComponent model = requireNonNull(
        getResource("/positioned")).adaptTo(PositionedLevelComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getPositionedStyle()).isEqualTo("level-left");
  }

  @Nullable
  private Resource getResource(String variant) {
    return context.resourceResolver().getResource(PATH + variant);
  }

}