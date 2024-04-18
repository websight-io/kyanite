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

import {DialogAction} from "./dialogAction";

/**
 *  Represents a queue of actions on a component/page dialog.
 *  Can be used for combining a set of actions together
 *  Basically is just a wrapper around List.
 */
export class DialogActionsQueue {

  private queue: DialogAction[] = [];

  public add(action: DialogAction){
    this.queue.push(action);
  }

  public clear() {
    this.queue.length = 0;
  }

  public execute() {
    for (const action of this.queue) {
      action.execute();
    }
  }
}
