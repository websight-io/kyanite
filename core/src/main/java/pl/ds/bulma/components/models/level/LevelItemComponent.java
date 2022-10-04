package pl.ds.bulma.components.models.level;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LevelItem {
  private static final String LEVEL_COMPONENT_RESOURCE_TYPE = "bulma/components/level";

  @SlingObject
  private Resource resource;

  @Inject
  @Getter
  @Default(values = StringUtils.EMPTY)
  private String levelItemStyle;

  @Inject
  @Getter
  @Default(values = "div")
  private String elementType;


  @PostConstruct
  private void init(){
    if(LEVEL_COMPONENT_RESOURCE_TYPE.equals(resource.getParent().getResourceType())){
      levelItemStyle = "has-text-centered";
    }
  }
}
