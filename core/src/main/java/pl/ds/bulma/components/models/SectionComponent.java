package pl.ds.bulma.components.models;

import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SectionComponent {

  @Inject
  @Getter
  @Default(values = StringUtils.EMPTY)
  private String size;

  @Inject
  @Getter
  private String id;
}
