package pl.ds.bulma.components.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ImageComponent {

  @Inject
  @Getter
  private String src;

  @Inject
  @Getter
  private String assetReference;

  @Inject
  @Getter
  private String type;

  @Inject
  @Getter
  private String style;

  @Inject
  @Getter
  private String isRounded;

  @Inject
  @Getter
  private String alt;

  @PostConstruct
  private void init() {
    if (assetReference == null && src != null) {
      assetReference = src;
    }
  }
}
