package pl.ds.bulma.components.models;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NotificationComponent {

  @Inject
  @Getter
  private String content;

  @Inject
  @Getter
  private String variant;

  @Inject
  @Getter
  @Default(booleanValues = false)
  private boolean isLight;

  @Inject
  @Getter
  @Default(booleanValues = false)
  private Boolean showButton;

  @Inject
  @Getter
  private String[] notificationClasses;

  @PostConstruct
  private void init() {
    List<String> classes = new ArrayList<>();
    if (isLight) {
      classes.add("is-light");
    }
    if (variant != null) {
      classes.add(variant);
    }

    notificationClasses = classes.toArray(new String[]{});
  }

}
