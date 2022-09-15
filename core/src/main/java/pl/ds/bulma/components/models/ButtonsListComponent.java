package pl.ds.bulma.components.models;


import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class ButtonsListComponent {

  @Inject
  @Default(values = "false")
  @Getter
  private String hasAddons;

  @Inject
  @Default(values = "is-left")
  @Getter
  private String alignment;
}
