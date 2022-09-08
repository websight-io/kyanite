/*
 * <!--
 *     Copyright (C) 2022 Dynamic Solutions
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 * -->
 */

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
public class ProgressBarComponent {

  @Inject
  @Getter
  @Default(intValues = 50)
  private Integer value;

  @Inject
  @Getter
  private String variant;

  @Inject
  @Getter
  private String size;

  @Inject
  @Getter
  private boolean isIntermediate;

  @Inject
  @Getter
  private String[] progressBarClasses;

  @PostConstruct
  private void init() {
    List<String> classes = new ArrayList<>();
    if (size != null) {
      classes.add(size);
    }
    if (variant != null) {
      classes.add(variant);
    }

    progressBarClasses = classes.toArray(new String[]{});
  }
}
