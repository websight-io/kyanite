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

package pl.ds.kyanite.page.outline.configuration;

import java.util.List;
import org.apache.sling.api.resource.Resource;
import org.jetbrains.annotations.NotNull;

public interface PageOutlineConfigStore {

  List<PageOutlineConfig> getAll();

  List<PageOutlineConfig> findAvailableOutlines(@NotNull Resource pageResource);

  List<String> findTemplatesByOutlineType(@NotNull String outlineType);

}
