/*
 * Copyright (c) 2019-2020 Dzikoysk
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

package org.panda_lang.orm.structure.query;

public enum DataQueryCategoryType {

    WHAT("what"),
    BY("by"),
    ORDER("order");

    private final String name;

    DataQueryCategoryType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static DataQueryCategoryType of(String name) {
        for (DataQueryCategoryType value : values()) {
            if (name.equals(value.getName())) {
                return value;
            }
        }

        return null;
    }

}
