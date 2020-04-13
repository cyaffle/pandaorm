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

package org.panda_lang.orm.structure.collection;

import org.panda_lang.utilities.commons.ObjectUtils;

public final class DataCollectionImpl implements DataCollection {

    private final String name;
    private final Class<?> entity;
    private final Object repository;

    public DataCollectionImpl(String name, Class<?> entity, Object repository) {
        this.name = name;
        this.entity = entity;
        this.repository = repository;
    }

    @Override
    public <R> R getRepository(Class<R> serviceClass) {
        return ObjectUtils.cast(repository);
    }

    @Override
    public Class<?> getEntityClass() {
        return entity;
    }

    @Override
    public String getName() {
        return name;
    }

}