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

package pl.ds.kyanite.page.outline.publish;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.resource.Resource;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import pl.ds.kyanite.page.outline.PageOutline;
import pl.ds.kyanite.page.outline.PageOutlineActionException;
import pl.ds.kyanite.page.outline.configuration.PageOutlineConfig;
import pl.ds.kyanite.page.outline.configuration.PageOutlineConfigStore;
import pl.ds.websight.publishing.framework.PublishException;
import pl.ds.websight.publishing.framework.PublishService;
import pl.ds.websight.publishing.framework.spi.PublishingPostprocessor;

@Slf4j
@Component(service = PublishingPostprocessor.class)
public class PageOutlinePublishPostProcessor implements PublishingPostprocessor {

  public static final String NT_PAGE = "ws:Page";
  public static final String NN_JCR_CONTENT = "jcr:content";
  public static final String PN_PRIMARY_TYPE = "jcr:primaryType";
  public static final String PN_TEMPLATE = "ws:template";
  @Reference
  private PublishService publishService;

  @Reference
  private PageOutlineConfigStore pageOutlineConfigStore;

  @Override
  public void afterPublish(@NotNull List<Resource> resources) throws PublishException {
    for (Resource contentResource : resources) {
      if (canProcess(contentResource)) {
        String template = getTemplate(contentResource);
        for (PageOutlineConfig config : pageOutlineConfigStore.findByPageTemplate(template)) {
          Optional<PageOutline> pageOutline = PageOutline.from(contentResource, config.getName());
          if (pageOutline.isPresent()) {
            try {
              pageOutline.get().delete().createPage(config.getOutlineResourceType()).commit();
            } catch (PageOutlineActionException e) {
              throw new PublishException(e.getMessage());
            }
            publishOutlinePage(pageOutline.get());
          }
        }
      }
    }
  }

  @Override
  public void afterUnpublish(@NotNull List<Resource> resources) throws PublishException {
    for (Resource contentResource : resources) {
      if (canProcess(contentResource)) {
        String template = getTemplate(contentResource);
        for (PageOutlineConfig config : pageOutlineConfigStore.findByPageTemplate(template)) {
          try {
            PageOutline.from(contentResource, config.getName())
                .ifPresent(pageOutline -> pageOutline.delete().commit());
          } catch (PageOutlineActionException e) {
            throw new PublishException(e.getMessage());
          }
        }
      }
    }
  }

  @Override
  public @NotNull String getName() {
    return "Page-Outline-Processor";
  }

  private boolean canProcess(Resource resource) {
    return isPage(resource) && hasAvailableOutlines(resource);
  }

  private boolean isPage(Resource resource) {
    return NT_PAGE.equals(resource.getValueMap().get(PN_PRIMARY_TYPE));
  }

  private boolean hasAvailableOutlines(Resource resource) {
    String template = getTemplate(resource);

    return template != null && !pageOutlineConfigStore.findByPageTemplate(template).isEmpty();
  }

  private String getTemplate(Resource resource) {
    Resource content = resource.getChild(NN_JCR_CONTENT);
    if (content == null) {
      return null;
    }


    return content.getValueMap().get(PN_TEMPLATE, String.class);
  }

  private void publishOutlinePage(PageOutline pageOutline) throws PublishException {
    publishService.publish(pageOutline.getOutlineResource());
  }

}
