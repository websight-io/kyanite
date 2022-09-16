package pl.ds.bulma.components.models;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ModalComponent {

  public static final String MODAL_ID_PREFIX = "modal_";

  @Inject
  @Getter
  @Default(booleanValues = true)
  private Boolean contentFrame;

  @SlingObject
  private Resource resource;

  public String getId() {
    return MODAL_ID_PREFIX + Math.abs(resource.getPath().hashCode() - 1);
  }
}
