package pl.ds.bulma.components.models;

import java.util.List;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DropdownComponent {

  @Inject
  @Getter
  @Default(values = "Label")
  private String label;

  @Inject
  @Getter
  private boolean isHoverable;

  @Inject
  @Getter
  private boolean isRight;

  @Inject
  @Getter
  private boolean isDropup;

  @Inject
  @Getter
  private List<DropdownElementComponent> items;

}
