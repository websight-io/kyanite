# Copyright (C) 2024 Dynamic Solutions
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

FROM node:18-slim

# https://github.com/nodejs/docker-node/blob/4c95f887f7863eccc17d66729cd24ecc230209a2/18/alpine3.17/Dockerfile#L5
ARG UID=1000
ARG GID=1000
ARG USER=node

# Config
ENV APP_HOST=0.0.0.0
ENV APP_PORT=8777
ENV WORKDIR=/app

ENV NODE_ENV=development

# Enable use of local npm packages:
ENV PATH="$WORKDIR/node_modules/.bin:$PATH"

EXPOSE "$APP_PORT"

# User
WORKDIR "$WORKDIR"
RUN chown "$USER":"$GID" "$WORKDIR"
USER "$USER"
COPY --chown="$USER":"$GID" package.json package-lock.json db.json config-routes.json middleware.js ./

# npm
RUN true && \
    # install
    npm ci --loglevel warn && \
    # clean up
    npm cache clean --force

# Run
CMD json-server db.json --port "$APP_PORT" --host "$APP_HOST" --routes config-routes.json --middlewares middleware.js
