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

package pl.ds.kyanite.blogs.components.exceptions;

public class AuthorInfoResolvingExceptionTemplates {
  public static final String AUTHOR_NODE_MISSING_IN_CONSUMER =
      "Author node is null";
  public static final String AUTHOR_NODE_MISSING_IN_REFERENCE =
      "Author info reference %s doesn't have 'author' node for consumer %s";
  public static final String AUTHOR_INFO_SOURCE_NOT_SET =
      "Author info source is not set in %s";
  public static final String AUTHOR_INFO_SOURCE_TYPE_UNKNOWN =
      "Unknown author info source type in %s,"
      + " expected one of: authorPage, parentPage, ownProperties, got: %s";
  public static final String AUTHOR_INFO_SOURCE_PATH_NOT_SET =
      "Author info reference path is not set in %s";
  public static final String CIRCULAR_REFERENCE =
      "Circular reference detected in author info reference chain";
  public static final String AUTHOR_INFO_SOURCE_RESOLVING_EXCEPTION =
      "Unable to retrieve author info source %s for consumer %s: %s";
  public static final String AUTHOR_INFO_SOURCE_IS_NULL =
      "Unable to resolve author info source %s to resource for consumer %s";
  public static final String AUTHOR_INFO_NOT_RESOLVING =
      "Resource %s doesn't resolve to AuthorInfo";
}
