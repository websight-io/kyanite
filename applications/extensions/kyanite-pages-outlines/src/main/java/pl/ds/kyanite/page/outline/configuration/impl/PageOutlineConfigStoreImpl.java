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

package pl.ds.kyanite.page.outline.configuration.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.sling.api.resource.Resource;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import pl.ds.kyanite.page.outline.configuration.PageOutlineConfig;
import pl.ds.kyanite.page.outline.configuration.PageOutlineConfigStore;

@Component(service = PageOutlineConfigStore.class, immediate = true)
public class PageOutlineConfigStoreImpl implements PageOutlineConfigStore {

  public static final String NN_JCR_CONTENT = "jcr:content";
  public static final String PN_TEMPLATE = "ws:template";
  private final Map<String, List<PageOutlineConfig>> pageOutlineConfigs = new HashMap<>();

  @Reference(service = PageOutlineConfig.class,
      cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC,
      bind = "bind", unbind = "unbind")
  public synchronized void bind(final PageOutlineConfig config) {
    pageOutlineConfigs.computeIfAbsent(config.getPageTemplate(), k -> new ArrayList<>());
    pageOutlineConfigs.get(config.getPageTemplate()).add(config);
  }

  public synchronized void unbind(final PageOutlineConfig config) {
    if (pageOutlineConfigs.get(config.getPageTemplate()) != null) {
      pageOutlineConfigs.get(config.getPageTemplate()).remove(config);
      if (pageOutlineConfigs.get(config.getPageTemplate()).isEmpty()) {
        pageOutlineConfigs.remove(config.getPageTemplate());
      }
    }
  }

  @Override
  public List<PageOutlineConfig> findAvailableOutlines(@NotNull Resource pageResource) {
    String template = getTemplate(pageResource);

    return findByPageTemplate(template);
  }

  public List<PageOutlineConfig> findByPageTemplate(String pageTemplate) {
    return pageOutlineConfigs.getOrDefault(pageTemplate, new ArrayList<>());
  }

  private String getTemplate(Resource pageResource) {
    Resource content = pageResource.getChild(NN_JCR_CONTENT);
    if (content == null) {
      return null;
    }
    return content.getValueMap().get(PN_TEMPLATE, String.class);
  }
}
