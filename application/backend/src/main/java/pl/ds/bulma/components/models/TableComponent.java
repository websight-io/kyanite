package pl.ds.bulma.components.models;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TableComponent {

  @Inject
  private boolean isBordered;

  @Inject
  private boolean isStriped;

  @Inject
  private boolean isNarrow;

  @Inject
  private boolean isHoverable;

  @Inject
  private boolean isFullwidth;

  @Inject
  @Getter
  private String[] tableClasses;

  @PostConstruct
  private void init() {
    List<String> classes = new ArrayList<>();
    if (isBordered) {
      classes.add("is-bordered");
    }
    if (isStriped) {
      classes.add("is-striped");
    }
    if (isNarrow) {
      classes.add("is-narrow");
    }
    if (isHoverable) {
      classes.add("is-hoverable");
    }
    if (isFullwidth) {
      classes.add("is-fullwidth");
    }
    tableClasses = classes.toArray(new String[]{});
  }
}
