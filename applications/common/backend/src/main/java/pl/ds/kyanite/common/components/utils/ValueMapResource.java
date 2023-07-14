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

package pl.ds.kyanite.common.components.utils;

import java.util.Collection;
import java.util.Iterator;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.SyntheticResource;
import org.apache.sling.api.resource.ValueMap;
import org.jetbrains.annotations.NotNull;

public class ValueMapResource extends SyntheticResource {

  private final ValueMap vm;
  private Collection<Resource> children;

  public ValueMapResource(final ResourceResolver resourceResolver, final String path,
      final String resourceType, final ValueMap vm) {
    super(resourceResolver, path, resourceType);
    this.vm = vm;
  }

  public ValueMapResource(final ResourceResolver resourceResolver, final String path,
      final String resourceType, final ValueMap vm, final Collection<Resource> children) {
    this(resourceResolver, path, resourceType, vm);
    this.children = children;
  }

  public ValueMapResource(final ResourceResolver resourceResolver, final ResourceMetadata rm,
      final String resourceType, final ValueMap vm) {
    super(resourceResolver, rm, resourceType);
    this.vm = vm;
  }

  public ValueMapResource(final ResourceResolver resourceResolver, final ResourceMetadata rm,
      final String resourceType, final ValueMap vm, final Collection<Resource> children) {
    this(resourceResolver, rm, resourceType, vm);
    this.children = children;
  }

  @Override
  public <T> T adaptTo(final Class<T> type) {
    if (ValueMap.class.equals(type)) {
      return (T) this.vm;
    }
    return (T) super.adaptTo((Class) type);
  }

  @Override
  public Resource getChild(final String relPath) {
    if (this.children == null) {
      return super.getChild(relPath);
    }
    for (final Resource child : this.children) {
      if (child.getPath() != null && child.getPath()
          .equals(this.getPath() + "/" + relPath)) {
        return child;
      }
    }
    return null;
  }

  @Override
  public @NotNull Iterator<Resource> listChildren() {
    if (this.children == null) {
      return super.listChildren();
    }
    return this.children.iterator();
  }

  @Override
  public @NotNull Iterable<Resource> getChildren() {
    return ValueMapResource.this::listChildren;
  }

  @Override
  public boolean hasChildren() {
    if (this.children == null) {
      return super.hasChildren();
    }
    return !this.children.isEmpty();
  }
}