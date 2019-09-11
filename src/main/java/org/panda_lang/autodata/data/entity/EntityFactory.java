/*
 * Copyright (c) 2015-2019 Dzikoysk
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

package org.panda_lang.autodata.data.entity;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.panda_lang.autodata.data.repository.RepositoryModel;

public final class EntityFactory {

    private static final org.panda_lang.autodata.data.entity.EntityModelLoader ENTITY_SCHEME_LOADER = new org.panda_lang.autodata.data.entity.EntityModelLoader();
    private static final org.panda_lang.autodata.data.entity.EntityGenerator ENTITY_GENERATOR = new org.panda_lang.autodata.data.entity.EntityGenerator();

    public EntityModel createEntityScheme(Class<?> entityClass) {
        return ENTITY_SCHEME_LOADER.load(entityClass);
    }

    public Class<? extends DataEntity> generateEntityClass(RepositoryModel scheme) throws CannotCompileException, NotFoundException {
        return ENTITY_GENERATOR.generate(scheme);
    }

}
