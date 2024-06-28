/*
 * Copyright (C) 2024 Dynamic Solutions
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

package pl.ds.kyanite.streamx.resource.impl;

import static org.apache.sling.api.resource.ResourceResolverFactory.SUBSERVICE;

import java.util.Map;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import pl.ds.kyanite.streamx.resource.ResourceResolverProvider;

@Component
public class ResourceResolverProviderImpl implements ResourceResolverProvider {

  private static final String SERVICE_USER_ID = "kyanite-streamx-support";

  @Reference
  private ResourceResolverFactory resourceResolverFactory;

  @Override
  public ResourceResolver getResourceResolver() throws LoginException {
    return resourceResolverFactory.getServiceResourceResolver(Map.of(SUBSERVICE, SERVICE_USER_ID));
  }
}
