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

const nonGetHttpMethods = ['POST', 'PUT', 'PATCH', 'DELETE', 'OPTIONS'];

/**
 * json-server works in non-only-read mode - it can modify the db.json file.
 * That's because we do want to let POST methods.
 * But letting saving files from user content is a security risk.
 * To stay secure this middleware modifies all other methods to non-file-modifying GET.
 */
module.exports = (req, res, next) => {
    if(req.method === "PUT"){
        res.status(202);
    }
    else if (nonGetHttpMethods.includes(req.method)) {
        req.method = 'GET';
    }
    next();
};
