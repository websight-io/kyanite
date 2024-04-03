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

package pl.ds.kyanite.blogs.components.services;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import pl.ds.kyanite.blogs.components.exceptions.AuthorInfoCircularReferenceException;
import pl.ds.kyanite.blogs.components.exceptions.AuthorInfoConfigurationException;
import pl.ds.kyanite.blogs.components.models.AuthorInfoModel;

public interface AuthorInfoResolverService {
  AuthorInfoModel retrieveAuthorInfo(Resource resource, ResourceResolver resourceResolver)
      throws AuthorInfoConfigurationException,
              AuthorInfoCircularReferenceException;
}
