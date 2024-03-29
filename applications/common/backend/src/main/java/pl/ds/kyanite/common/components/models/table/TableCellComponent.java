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

package pl.ds.kyanite.common.components.models.table;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TableCellComponent {

  private static final String DEFAULT_TEXT = "Content";

  @Inject
  @Getter
  @Default(values = DEFAULT_TEXT)
  private String text;

  @Inject
  @Getter
  @Default(intValues = 1)
  private int colspan;

  @Inject
  @Getter
  @Default(intValues = 1)
  private int rowspan;

  @PostConstruct
  private void init() {
    if (text.isEmpty()) {
      text = DEFAULT_TEXT;
    }
  }

}
