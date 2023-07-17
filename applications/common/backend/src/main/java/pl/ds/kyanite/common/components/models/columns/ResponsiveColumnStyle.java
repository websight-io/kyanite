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

package pl.ds.kyanite.common.components.models.columns;

import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;

@Getter
@Setter
@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class ResponsiveColumnStyle {

  @Inject
  @Default(booleanValues = true)
  private boolean isNormalColumn;

  @Inject
  @Default(values = StringUtils.EMPTY)
  private String sizeType;

  @Inject
  @Default(values = StringUtils.EMPTY)
  private String size;

  @Inject
  @Default(values = StringUtils.EMPTY)
  private String offsetType;

  @Inject
  @Default(values = StringUtils.EMPTY)
  private String offset;

}
