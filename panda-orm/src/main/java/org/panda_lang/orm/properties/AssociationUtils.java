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

package org.panda_lang.orm.properties;

public final class AssociationUtils {

    private AssociationUtils() { }

    public static String getCollection(String ref) {
        String[] content = ref.split(":");

        if (content.length == 1) {
            return ref;
        }

        return content[0];
    }

    public static String getProperty(String ref) {
        String[] content = ref.split(":");

        if (content.length == 1) {
            return "";
        }

        return content[1];
    }

}
