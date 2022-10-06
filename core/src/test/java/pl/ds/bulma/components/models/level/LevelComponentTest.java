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
class LevelComponentTest {


  private static final String PATH = "/content/level";
  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(LevelComponent.class);
    context.load().json(requireNonNull(
        Thread.currentThread().getContextClassLoader().getResourceAsStream("level.json")), PATH);
  }


  @Test
  void defaultLevel() {
    LevelComponent model = requireNonNull(
        getResource("/default")).adaptTo(LevelComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.isVertical()).isTrue();
  }

  @Test
  void centeredLevel() {
    LevelComponent model = requireNonNull(
        getResource("/centered")).adaptTo(LevelComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.isVertical()).isFalse();
    getResource("/centered").getChildren().forEach(child -> {
      assertThat(child.isResourceType(LEVEL_ITEM_COMPONENT_RESOURCE_TYPE)).isTrue();
    });
  }

  @Nullable
  private Resource getResource(String variant) {
    return context.resourceResolver().getResource(PATH + variant);
  }


}