package pl.ds.bulma.components.models.menu;

import java.util.List;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MenuListComponent {

  @Getter
  @Inject
  @Default(values = StringUtils.EMPTY)
  private String label;

  @Getter
  @Inject
  private List<MenuListItemComponent> menuListItemComponentList;
}
