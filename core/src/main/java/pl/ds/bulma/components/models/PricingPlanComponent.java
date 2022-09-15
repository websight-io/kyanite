package pl.ds.bulma.components.models;

import java.util.List;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PricingPlanComponent {

  @Inject
  @Getter
  @Default(values = "Header")
  private String header;

  @Inject
  @Getter
  @Default(booleanValues = true)
  private Boolean showPricing;

  @Inject
  @Getter
  @Default(intValues = 0)
  private Integer price;

  @Inject
  @Getter
  @Default(values = "$")
  private String currency;

  @Inject
  @Getter
  @Default(values = "monthly")
  private String period;

  @Inject
  @Getter
  private String variant;


  @Inject
  @Getter
  private List<PricingDetailsComponent> details;

  @Inject
  @Getter
  @Default(booleanValues = true)
  private Boolean showButton;

  @Inject
  @Getter
  @Default(values = "Choose")
  private String buttonLabel;
}
