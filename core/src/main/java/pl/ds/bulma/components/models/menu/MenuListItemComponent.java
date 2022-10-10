package pl.ds.bulma.components.models.menu;

import java.util.List;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MenuListItemComponent {
  @Inject
  @Getter
  private String text;

  @Inject
  @Getter
  private String url;

  @Inject
  @Getter
  private List<MenuListItemComponent> secondLevelItems;
}
