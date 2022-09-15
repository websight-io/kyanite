package pl.ds.bulma.components.models.columns;

import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

@Getter
@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class DefaultResponsiveColumnComponent {

  @Inject
  private ResponsiveColumnStyle mobileColumnStyle;

  @Inject
  private ResponsiveColumnStyle tabletColumnStyle;

  @Inject
  private ResponsiveColumnStyle desktopColumnStyle;

}
