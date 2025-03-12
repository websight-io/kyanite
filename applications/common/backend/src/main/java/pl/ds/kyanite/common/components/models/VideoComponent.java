/*
 * Copyright (C) 2023 Dynamic Solutions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.ds.kyanite.common.components.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import pl.ds.kyanite.common.components.utils.LinkUtil;
import pl.ds.kyanite.common.components.utils.VideoLinkParser;

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
  @Getter
  private boolean hasThumbnail;


  @ValueMapValue
  @Default(values = StringUtils.EMPTY)
  private String thumbnail;

  private final List<String> classes = new ArrayList<>();

  @ValueMapValue
  private boolean isFullWidth;

  @ValueMapValue
  @Getter
  @Default(values = "disk")
  private String source;

  @ValueMapValue
  @Getter
  @Default(values = "640")
  private String width;

  @ValueMapValue
  @Getter
  @Default(values = "480")
  private String height;

  @ValueMapValue
  @Default(values = StringUtils.EMPTY)
  private String youtubeLink;

  @Getter
  private String youtubeIframeId;

  @ValueMapValue
  @Getter
  @Default(booleanValues = false)
  private boolean trackInteractions;

  @ValueMapValue
  @Default(values = StringUtils.EMPTY)
  private String vimeoLink;

  @Getter
  private String src;

  @SlingObject
  private Resource resource;

  @Getter
  private Map<String, String> attributes;

  private final Map<String, Boolean> parameters = new HashMap<>();

  @PostConstruct
  private void init() {
    this.parameters.put("controls", hasControls);
    this.parameters.put("autoplay", autoplay);
    this.parameters.put("loop", isLooped);
    this.parameters.put(this.source.equals("youtube") ? "mute" : "muted", isMuted);
    this.attributes = parameters.entrySet()
        .stream()
        .filter(Entry::getValue)
        .collect(Collectors.toMap(Entry::getKey, elem -> String.valueOf(true)));
    if (hasThumbnail) {
      this.attributes.put("poster", this.getThumbnail());
    }
    if (isFullWidth) {
      classes.add("is-fullwidth");
    }

    switch (this.source) {
      case ("youtube") -> {
        this.src = VideoLinkParser.getYouTubeLink(youtubeLink, trackInteractions);
        this.youtubeIframeId = Optional.ofNullable(VideoLinkParser.getYouTubeId(youtubeLink))
            .map(id -> "player-" + id).orElse("player");
        if (trackInteractions) {
          this.parameters.put("enablejsapi", true);
        }
      }
      case ("vimeo") -> this.src = VideoLinkParser.getVimeoLink(vimeoLink);
      default -> this.src = StringUtils.EMPTY;
    }
  }

  public String getStyle() {
    return String.join(" ", classes);
  }

  public String getShareLinkParameters() {
    return parameters.entrySet().stream()
        .map(entry -> entry.getKey() + "=" + (Boolean.TRUE.equals(entry.getValue()) ? 1 : 0))
        .collect(Collectors.joining("&"));
  }

  public boolean isVideo() {
    return source.equals("video");
  }

  public String getAssetReference() {
    return LinkUtil.handleLink(assetReference, resource.getResourceResolver());
  }

  public String getThumbnail() {
    return LinkUtil.handleLink(thumbnail, resource.getResourceResolver());
  }

}
