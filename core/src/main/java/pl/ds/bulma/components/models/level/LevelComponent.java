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


  @Inject
  @Getter
  @Default(booleanValues = true)
  private boolean isVertical;

}
