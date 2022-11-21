package pl.ds.bulma.components.models;

import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DropdownElementComponent {

  @Inject
  @Getter
  private String label;

  @Inject
  @Getter
  private String url;

  @Inject
  @Getter
  private boolean hasDivider;

}
