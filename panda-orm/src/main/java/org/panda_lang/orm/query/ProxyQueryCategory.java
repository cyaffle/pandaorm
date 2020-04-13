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

package org.panda_lang.orm.query;

import java.util.List;

final class ProxyQueryCategory implements DataQueryCategory {

    private final DataQueryCategoryType type;
    private final List<? extends DataQueryRuleScheme> elements;

    ProxyQueryCategory(DataQueryCategoryType type, List<? extends DataQueryRuleScheme> elements) {
        this.type = type;
        this.elements = elements;
    }

    @Override
    public List<? extends DataQueryRuleScheme> getElements() {
        return elements;
    }

    @Override
    public String getName() {
        return type.getName();
    }

}
