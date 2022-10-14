package pl.ds.bulma.components.models.navbar;

import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import pl.ds.bulma.components.models.ImageComponent;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NavbarItemComponent {


  @Inject
  @Getter
  private String type;

  @Inject
  @Getter
  private String label;

  @Inject
  @Getter
  private String url;

  @Inject
  @Getter
  private ImageComponent image;

  @Inject
  @Getter
  private String imageWidth;

  @Inject
  @Getter
  private String imageHeight;

  @Inject
  @Getter
  private boolean hasDivider;
}
