package pl.ds.bulma.components.models.navbar;

import java.util.List;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NavbarDropdownComponent {

  @Inject
  @Getter
  @Default(values = "Label")
  private String label;

  @Inject
  @Getter
  @Default(booleanValues = true)
  private boolean hasArrow;

  @Inject
  @Getter
  private boolean isBoxed;


  @Inject
  @Getter
  private List<NavbarItemComponent> items;
}
