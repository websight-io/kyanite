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

package pl.ds.bulma.components.models.tiles;


import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class TileComponent {

  private static final String PARENT_TYPE = "is-parent";
  private static final String CHILD_TYPE = "is-child";
  private static final String IS_VERTICAL = "is-vertical";
  private static final String HIERARCHY_CLASS_PROPERTY = "hierarchyClass";

  @SlingObject
  private Resource resource;

  @Inject
  @Getter
  @Default(values = PARENT_TYPE)
  private String type;

  @Inject
  @Getter
  @Default(values = StringUtils.EMPTY)
  private String hierarchyClass;

  @Inject
  @Getter
  @Default(values = StringUtils.EMPTY)
  private String contentClasses;

  @Inject
  @Getter
  @Default(booleanValues = false)
  private boolean isVertical;

  @Inject
  @Getter
  @Default(values = StringUtils.EMPTY)
  private String size;

  @Inject
  @Getter
  private String[] classes;

  @PostConstruct
  private void init() throws RepositoryException, PersistenceException {
    ResourceResolver resourceResolver = resource.getResourceResolver();
    Session session = resourceResolver.adaptTo(Session.class);
    Resource parent = resourceResolver.getParent(resource);
    List<String> classList = new ArrayList<>();

    if (PARENT_TYPE.equals(type)) {
      handleWhenParentType(resourceResolver, session, parent, classList);
    }

    if (CHILD_TYPE.equals(type)) {
      handleWhenChildType(resourceResolver, session, parent, classList);
    }

    if (StringUtils.isNotEmpty(hierarchyClass)) {
      classList.add(hierarchyClass);
    }

    classes = classList.toArray(new String[]{});
  }

  private void handleWhenParentType(ResourceResolver resourceResolver, Session session,
      Resource parent,
      List<String> classList) throws RepositoryException, PersistenceException {
    if (parent != null && session != null) {
      updateHierarchyClassProperty(session, parent, StringUtils.EMPTY);
      resourceResolver.commit();
    }
    if (StringUtils.isNotEmpty(size)) {
      classList.add(size);
    }
    if (isVertical) {
      classList.add(IS_VERTICAL);
    }
  }

  private void handleWhenChildType(ResourceResolver resourceResolver, Session session,
      Resource parent,
      List<String> classList) throws RepositoryException, PersistenceException {
    if (StringUtils.isNotEmpty(contentClasses)) {
      classList.add(contentClasses);
      if (parent != null && session != null) {
        updateHierarchyClassProperty(session, parent, PARENT_TYPE);
        updateHierarchyClassProperty(session, resource, CHILD_TYPE);
        resourceResolver.commit();
      }
    }
  }

  private void updateHierarchyClassProperty(Session session, Resource resource, String value)
      throws RepositoryException {
    Node parentNode = session.getNode(resource.getPath());
    parentNode.setProperty(HIERARCHY_CLASS_PROPERTY, value);
  }
}
