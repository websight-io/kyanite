package pl.ds.bulma.components.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import pl.ds.bulma.components.utils.LinkUtil;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class VideoComponent {

  @ValueMapValue
  @Default(values = StringUtils.EMPTY)
  private String assetReference;

  @ValueMapValue
  private boolean isMuted;

  @ValueMapValue
  private boolean isLooped;

  @ValueMapValue
  private boolean hasControls;

  @ValueMapValue
  private boolean autoplay;


  @ValueMapValue
  private boolean hasThumbnail;


  @ValueMapValue
  @Default(values = StringUtils.EMPTY)
  private String thumbnail;

  @Getter
  private String style;

  @ValueMapValue
  private boolean isFullWidth;


  @SlingObject
  private Resource resource;

  @Getter
  private final Map<String, String> attributes = new HashMap<>();

  @PostConstruct
  private void init() {
    List<String> classes = new ArrayList<>();
    if (hasControls) {
      this.attributes.put("controls", "true");
    }
    if (autoplay) {
      this.attributes.put("autoplay", "true");
    }
    if (isLooped) {
      this.attributes.put("loop", "true");
    }
    if (isMuted) {
      this.attributes.put("muted", "true");
    }
    if (hasThumbnail) {
      this.attributes.put("poster", this.getThumbnail());
    }
    if (isFullWidth) {
      classes.add("is-fullwidth");
    }
    this.style = String.join(" ", classes);
  }

  public String getAssetReference() {
    return LinkUtil.handleLink(assetReference, resource.getResourceResolver());
  }

  public String getThumbnail() {
    return LinkUtil.handleLink(thumbnail, resource.getResourceResolver());
  }

}
