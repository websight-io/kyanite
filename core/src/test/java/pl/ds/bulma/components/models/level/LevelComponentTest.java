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

@ExtendWith({SlingContextExtension.class, MockitoExtension.class})
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
    assertThat(model.getLevelType()).isEqualTo("positioned");
    getResource("/default").getChildren().forEach(child -> {
      assertThat(child.isResourceType(POSITIONED_LEVEL_COMPONENT_RESOURCE_TYPE)).isTrue();
      System.out.println(child.getPath());
    });
  }

  @Test
  void centeredLevel() {
    LevelComponent model = requireNonNull(
        getResource("/centered")).adaptTo(LevelComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.isVertical()).isFalse();
    assertThat(model.getLevelType()).isEqualTo("centered");
    getResource("/centered").getChildren().forEach(child -> {
      assertThat(child.isResourceType(LEVEL_ITEM_COMPONENT_RESOURCE_TYPE)).isTrue();
      System.out.println(child.getPath());
    });
  }

  @Test
  void changeToCenteredLevel() {
    LevelComponent model = requireNonNull(
        getResource("/changeToCentered")).adaptTo(LevelComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.isVertical()).isFalse();
    assertThat(model.getLevelType()).isEqualTo("centered");
    //PositionedElements should be deleted
    assertThat(getResource("/changeToCentered").hasChildren()).isFalse();
  }

  @Test
  void positionedLevel() {
    LevelComponent model = requireNonNull(
        getResource("/positioned")).adaptTo(LevelComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.isVertical()).isTrue();
    assertThat(model.getLevelType()).isEqualTo("positioned");
    //should only have positioned elements
    getResource("/positioned").getChildren().forEach(child -> {
      assertThat(child.isResourceType(POSITIONED_LEVEL_COMPONENT_RESOURCE_TYPE)).isTrue();
    });
  }

  @Nullable
  private Resource getResource(String variant) {
    return context.resourceResolver().getResource(PATH + variant);
  }

}