package pl.ds.bulma.components.models.navbar;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NavbarComponent {

  @Inject
  @Getter
  private String variant;

  @Inject
  @Getter
  private boolean isTransparent;

  @Inject
  @Getter
  private String navbarStyle;

  @PostConstruct
  private void init() {
    //reset color variant if navbar is transparent
    if (isTransparent) {
      variant = null;
      navbarStyle = "is-transparent";
    } else {
      navbarStyle = variant;
    }
  }
}
