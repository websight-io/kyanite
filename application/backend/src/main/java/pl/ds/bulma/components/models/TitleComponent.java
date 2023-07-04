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

package pl.ds.bulma.components.models;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.bulma.components.helpers.ColorService;
import pl.ds.bulma.components.services.GenerateTestContent;
import pl.ds.bulma.components.utils.ContentGeneration;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TitleComponent {

  @Inject
  @Getter
  @Default(values = "Title")
  @ContentGeneration(stringValues = {"textExample1", ""})
  private String text;

  @Inject
  @Default(values = "Subtitle")
  @ContentGeneration(stringValues = {"subtitle1", ""})
  private String subtitle;

  @Inject
  @Getter
  @Default(values = "h2")
  private String element;

  @Inject
  @Getter
  @Default(booleanValues = false)
  private boolean isSpaced;

  @Inject
  @Getter
  @Default(booleanValues = false)
  @ContentGeneration(booleanValues = {false, true})
  private boolean addSubtitle;

  @Inject
  @Getter
  @Default(values = StringUtils.EMPTY)
  //@ContentGeneration(stringValues = {"is-2", "is-3"})
  @ContentGeneration(useDialog = {true})
  private String size;

  @Inject
  @Getter
  private String[] titleClasses;

  @Inject
  @Getter
  private String[] subtitleClasses;

  @Inject
  @Default(values = "bw_has-text-black")
  @ContentGeneration(stringValues = {
      "bw_has-text-black",
      "bw_has-text-white",
      "grey_has-text-grey",
      "rest_has-text-primary",
      "rest_has-text-link",
      "rest_has-text-info",
      "rest_has-text-success",
      "rest_has-text-warning",
      "rest_has-text-danger"
  })
  private String color;

  @Inject
  @Default(values = "bw_has-text-black")
  private String subtitleColor;

  @Inject
  @Default(values = StringUtils.EMPTY)
  //@ContentGeneration(stringValues = {"", "bis"})
  @ContentGeneration(stringValues = {"", "bis", "ter"})
  private String shadeBw;

  @Inject
  @Default(values = StringUtils.EMPTY)
  //@ContentGeneration(stringValues = {"light", "dark"})
  @ContentGeneration(stringValues = {"", "light", "lighter", "dark", "darker"})
  private String shadeGrey;

  @Inject
  @Default(values = StringUtils.EMPTY)
  @ContentGeneration(stringValues = {"light", "dark"})
  //@ContentGeneration(stringValues = {"", "light", "dark"})
  private String shadeRest;

  @Inject
  @Default(values = StringUtils.EMPTY)
  private String subtitleShadeBw;

  @Inject
  @Default(values = StringUtils.EMPTY)
  private String subtitleShadeGrey;

  @Inject
  @Default(values = StringUtils.EMPTY)
  private String subtitleShadeRest;

  @Inject
  @Getter
  @Default(values = "")
  private String anchorId;

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

    String textColorVariant = getColorVariant(color, shadeBw, shadeGrey, shadeRest);
    String subtitleColorVariant = getColorVariant(
        subtitleColor, subtitleShadeBw, subtitleShadeGrey, subtitleShadeRest);

    if (textColorVariant != null && !textColorVariant.isEmpty()) {
      titleClassList.add(textColorVariant);
    }
    if (subtitleColorVariant != null && !subtitleColorVariant.isEmpty()) {
      subtitleClassList.add(subtitleColorVariant);
    }
    titleClasses = titleClassList.toArray(new String[]{});
    subtitleClasses = subtitleClassList.toArray(new String[]{});
  }

  public String getMyTest() {
    String dialogPath = "libs/%s/dialog".formatted(resource.getResourceType());
    ResourceResolver resourceResolver = resource.getResourceResolver();
    var dialogResource = resourceResolver.resolve(dialogPath);
    List<Field> classFields = GenerateTestContent.getClassFields(getClass());
    var fieldNameToValues = classFields.stream()
        .collect(Collectors.toMap(Field::getName,
            it -> it.getAnnotation(ContentGeneration.class)));

    for (var entry : fieldNameToValues.entrySet()) {
      var name = entry.getKey();
      var content = entry.getValue();
      if (content.useDialog().length > 0) {
        String fieldIncludePath = getDescendants(dialogResource).stream()
            .filter(it -> it.getName().equals(name))
            .map(it -> it.getValueMap().get("path"))
            .findFirst().get().toString();
        String joinedAllowedValues = getDescendants(resourceResolver.resolve(fieldIncludePath))
            .stream().map(it -> it.getValueMap().get("value").toString())
            .collect(Collectors.joining(". "));
        return joinedAllowedValues;
      }
    }

    return "getMyTest";
  }

  private Collection<Resource> getDescendants(Resource resource) {
    final Collection<Resource> children = Optional.ofNullable(resource)
        .map(res -> StreamSupport.stream(res.getChildren().spliterator(), false).collect(
            Collectors.toSet()))
        .orElse(Collections.emptySet());
    return children.isEmpty() ? Collections.emptySet()
        : Stream.concat(children.stream(), children.stream()
            .map(this::getDescendants)
            .flatMap(Collection::stream))
        .collect(Collectors.toSet());
  }

  private String getColorVariant(String color, String shadeBw, String shadeGrey, String shadeRest) {
    ColorService colorService = new ColorService(resource, "bulma/components/common/text/color",
        color, shadeBw, shadeGrey, shadeRest);

    return colorService.getTextColorVariant();
  }

  public String getSubtitle() {
    return addSubtitle ? subtitle : "";
  }
}
