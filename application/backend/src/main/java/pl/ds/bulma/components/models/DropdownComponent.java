package pl.ds.bulma.components.models;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DropdownComponent {

  @Inject
  @Getter
  @Default(values = "Label")
  private String label;

  @Inject
  private boolean isHoverable;

  @Inject
  private boolean isRight;

  @Inject
  @Getter
  private boolean isUp;

  @Inject
  @Getter
  private List<DropdownElementComponent> items;

  @Inject
  @Getter
  private String[] dropdownClasses;

  @PostConstruct
  private void init() {
    List<String> classes = new ArrayList<>();
    if (isHoverable) {
      classes.add("is-hoverable");
    }
    if (isRight) {
      classes.add("is-right");
    }
    if (isUp) {
      classes.add("is-up");
    }
    dropdownClasses = classes.toArray(new String[]{});
  }
}
