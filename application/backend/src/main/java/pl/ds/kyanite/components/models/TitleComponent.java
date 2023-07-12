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

package pl.ds.kyanite.components.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Required;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.jetbrains.annotations.NotNull;
import pl.ds.kyanite.components.services.ColorService;

@Model(adaptables = Resource.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TitleComponent {

  public static final String STR = "\"";
  public static final String SPACE = " ";
  @Inject
  @Getter
  private String text;

  @Inject
  @Getter
  private String subtitle;

  @Inject
  @Getter
  private String element;

  @Inject
  @Getter
  private boolean isSpaced;

  @Inject
  @Getter
  private boolean addSubtitle;

  @Inject
  @Getter
  private String size;

  @Inject
  @Getter
  private String[] titleClasses;

  @Inject
  @Getter
  private String[] subtitleClasses;

  @Inject
  private String color;

  @Inject
  private String subtitleColor;

  @Inject
  @Getter
  private String anchorId;

  @Getter
  private String titleAlign;

  @Inject
  @Getter
  private boolean addEyebrow;

  @Inject
  private String eyebrowText;


  @OSGiService
  @Required
  private ColorService colorService;

  @SlingObject
  private Resource resource;

  @PostConstruct
  private void init() {
    List<String> titleClassList = new ArrayList<>();
    List<String> subtitleClassList = new ArrayList<>();
    titleClassList.add("title");
    subtitleClassList.add("subtitle");

    if (StringUtils.isNotBlank(size)) {
      titleClassList.add(size);
      int sizeNumber = Integer.parseInt(size.split("-")[1]);
      if (sizeNumber > 4) {
        addSubtitle = false;
      }
      subtitleClassList.add("is-" + (sizeNumber + 2));
    }
    if (isSpaced) {
      titleClassList.add("is-spaced");
    }

    titleClassList.add(colorService.getShadeClass(resource, color, "shade"));
    subtitleClassList.add(colorService.getShadeClass(resource, subtitleColor,
        "subtitleShade"));

    titleClasses = titleClassList.toArray(new String[]{});
    subtitleClasses = subtitleClassList.toArray(new String[]{});
    titleAlign = this.searchTitleAlign();
  }

  private String searchTitleAlign() {
    if (StringUtils.isBlank(text)) {
      return StringUtils.EMPTY;
    }
    String textAlignToken = "text-align: ";
    if (text.contains(textAlignToken)) {
      int styleStart = text.indexOf(textAlignToken);
      String substring = text.substring(styleStart);
      if (substring.contains(STR)) {
        int styleEnd = substring.indexOf(STR);

        return text.substring(styleStart + textAlignToken.length(), styleStart + styleEnd);
      }
    }

    return StringUtils.EMPTY;
  }

  private String enforceCorrectTags(String value) {
    boolean containsAnyTags = Stream.of("<p>", "<p ", "<h1>", "<h2>", "<h3>", "<h4>", "<h5>",
        "<h6>").anyMatch(value::contains);
    return containsAnyTags
        ? replaceP(value)
        : embedInTags(value);
  }

  @NotNull
  private String replaceP(String value) {
    return value.replace("<p>",
            "<%s class=\"%s\">".formatted(element, String.join(SPACE, titleClasses)))
        .replace("<p ", "<%s class=\"%s\" ".formatted(element, String.join(SPACE, titleClasses)))
        .replace("</p>", "</%s>".formatted(element));
  }

  private String embedInTags(String value) {
    return "<%s class=\"%s\">%s</%s>".formatted(element, String.join(SPACE, titleClasses), value,
        element);
  }

  public String getTextAsOneLine() {
    return this.text.replaceAll("<p[^>]*>", "").replace("</p>", SPACE);
  }

  public String getRawText() {
    return this.text.replaceAll("<[^>]*>", SPACE).replace("  ", SPACE);
  }

  public String getEyebrowText() {
    return addEyebrow ? eyebrowText : StringUtils.EMPTY;
  }
}
