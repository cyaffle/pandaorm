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

package org.panda_lang.orm.collection;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.panda_lang.orm.PandaOrmException;
import org.panda_lang.orm.entity.DataEntity;
import org.panda_lang.orm.entity.EntityFactory;
import org.panda_lang.orm.repository.DataController;
import org.panda_lang.orm.repository.RepositoryFactory;
import org.panda_lang.orm.repository.RepositoryModel;
import org.panda_lang.utilities.inject.Injector;

public final class CollectionFactory {

    private static final EntityFactory ENTITY_FACTORY = new EntityFactory();
    private static final RepositoryFactory REPOSITORY_FACTORY = new RepositoryFactory();

    public DataCollection createCollection(DataController controller, Injector injector, RepositoryModel repositoryModel) {
        try {
            Class<? extends DataEntity> entityClass = ENTITY_FACTORY.generateEntityClass(repositoryModel);

            DataCollection collection = createCollection(repositoryModel.getCollectionScheme(), entityClass, repositoryModel.getRepository());
            REPOSITORY_FACTORY.createRepositoryImplementation(controller, collection, repositoryModel);

            return collection;
        } catch (CannotCompileException | NotFoundException e) {
            throw new PandaOrmException("Cannot generate entity class", e);
        }
    }

    public DataCollection createCollection(CollectionModel scheme, Class<? extends DataEntity> entityClass, Object repository) {
        return new DataCollectionImpl(scheme.getName(), entityClass , repository);
    }

}
