package pl.ds.bulma.components.models;

import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

import java.util.List;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class CardComponent {

  @Inject
  @Getter
  @Default(values = StringUtils.EMPTY)
  private String header;

  @Inject
  @Getter
  @Default(values = StringUtils.EMPTY)
  private String title;

  @Inject
  @Getter
  @Default(values = StringUtils.EMPTY)
  private String subtitle;

  @Inject
  @Getter
  private ImageComponent thumbnail;

  @Inject
  @Getter
  private ImageComponent image;

  @Inject
  @Getter
  private List<URLComponent> urls;

  @Inject
  @Getter
  @Default(values = "Card content")
  private String content;

  @Inject
  @Getter
  @Default(values = "image")
  private String type;

  @Inject
  @Getter
  @Default(values = StringUtils.EMPTY)
  private String icon;

  @Inject
  @Getter
  @Default(values = StringUtils.EMPTY)
  private String textVariant;

}
