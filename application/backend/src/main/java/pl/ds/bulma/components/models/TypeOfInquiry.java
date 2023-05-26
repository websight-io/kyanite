package pl.ds.bulma.components.models;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TypeOfInquiry {

  @ValueMapValue
  @Getter
  @Default(values = "Message from website.")
  private String label;

  @ValueMapValue
  @Getter
  @Default(values = StringUtils.EMPTY)
  private String email;

}
