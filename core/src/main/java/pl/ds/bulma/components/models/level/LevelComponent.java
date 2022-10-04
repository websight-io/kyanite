package pl.ds.bulma.components.models.level;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LevelComponent {

  static final String POSITIONED_LEVEL_COMPONENT_RESOURCE_TYPE = "bulma/components/level/positionedlevel";
  static final String LEVEL_ITEM_COMPONENT_RESOURCE_TYPE = "bulma/components/level/levelitem";
  private static final String SLING_RESOURCE_TYPE = "sling:resourceType";
  public static final String POSITIONED = "positioned";
  public static final String POSITIONED_STYLE = "positionedStyle";

  @SlingObject
  private Resource resource;

  @Inject
  @Getter
  @Default(values = POSITIONED)
  private String levelType;

  @Inject
  @Getter
  @Default(booleanValues = true)
  private boolean isVertical;


  @PostConstruct
  private void init() throws PersistenceException {
    updatePositionedLevelContainers(levelType, "level-left");
    updatePositionedLevelContainers(levelType, "level-right");
    updateLevelItemContainers(levelType);
    resource.getResourceResolver().commit();
  }

  private void updateLevelItemContainers(String levelType) {
    //if there are Centered items, and we switched level type remove resources
    if (!"centered".equals(levelType) && hasLevelItemChildCreated()) {
      StreamSupport
          .stream(resource.getChildren().spliterator(), false)
          .filter(this::isLevelItem)
          .forEach(levelItem -> {
            try {
              resource.getResourceResolver().delete(levelItem);
            } catch (PersistenceException e) {
              throw new RuntimeException(e);
            }
          });
    }
  }

  private void updatePositionedLevelContainers(String levelType, String style)
      throws PersistenceException {
    if (POSITIONED.equals(levelType) && !hasPositionedChildCreated(style)) {
      Map<String, Object> properties = new HashMap<>();
      properties.put("jcr:primaryType", "nt:unstructured");
      properties.put(SLING_RESOURCE_TYPE, POSITIONED_LEVEL_COMPONENT_RESOURCE_TYPE);
      properties.put(POSITIONED_STYLE, style);
      resource.getResourceResolver()
          .create(resource, "positionedlevel_" + style, properties);
    }
    if (!POSITIONED.equals(levelType) && hasPositionedChildCreated(style)) {
      Optional<Resource> positionedLevelResource = getChildResource(style);
      if (positionedLevelResource.isPresent()) {
        resource.getResourceResolver().delete(positionedLevelResource.get());
      }
    }
  }

  private boolean hasPositionedChildCreated(String style) {
    return StreamSupport
        .stream(resource.getChildren().spliterator(), false)
        .filter(this::isPositionedLevel)
        .anyMatch(resource1 -> resource1.getValueMap().get(POSITIONED_STYLE).equals(style));
  }

  private boolean hasLevelItemChildCreated() {
    return StreamSupport
        .stream(resource.getChildren().spliterator(), false)
        .anyMatch(this::isLevelItem);
  }

  private Optional<Resource> getChildResource(String style) {
    return StreamSupport
        .stream(resource.getChildren().spliterator(), false)
        .filter(this::isPositionedLevel)
        .filter(resource1 -> resource1.getValueMap().get(POSITIONED_STYLE).equals(style))
        .findFirst();
  }

  private boolean isPositionedLevel(Resource resource) {
    return POSITIONED_LEVEL_COMPONENT_RESOURCE_TYPE.equals(resource.getResourceType());
  }

  private boolean isLevelItem(Resource resource) {
    return LEVEL_ITEM_COMPONENT_RESOURCE_TYPE.equals(resource.getResourceType());
  }
}
