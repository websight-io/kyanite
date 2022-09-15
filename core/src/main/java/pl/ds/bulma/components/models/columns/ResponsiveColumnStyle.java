package pl.ds.bulma.components.models.columns;

import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;

@Getter
@Setter
@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class ResponsiveColumnStyle {

  @Inject
  @Default(booleanValues = true)
  private boolean isNormalColumn;

  @Inject
  @Default(values = StringUtils.EMPTY)
  private String sizeType;

  @Inject
  @Default(values = StringUtils.EMPTY)
  private String size;

  @Inject
  @Default(values = StringUtils.EMPTY)
  private String offsetType;

  @Inject
  @Default(values = StringUtils.EMPTY)
  private String offset;

}
