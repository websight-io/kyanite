package pl.ds.bulma.components.models;

import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ModalComponent {

  public static final String MODAL_ID_PREFIX = "modal_";

  @Inject
  @Getter
  @Default(booleanValues = true)
  private Boolean contentFrame;

  @SlingObject
  private Resource resource;

  //TODO Maybe use UUID that will be stored in properties as each time path changes same happens with ID
  //We would like it to be general
  public String getId() {
    return MODAL_ID_PREFIX + Math.abs(resource.getPath().hashCode() - 1);
  }
}
