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

package org.panda_lang.autodata.defaults.sql;

import org.panda_lang.autodata.data.repository.DataController;
import org.panda_lang.autodata.data.repository.DataHandler;
import org.panda_lang.autodata.data.collection.CollectionModel;
import org.panda_lang.autodata.data.collection.DataCollection;
import org.panda_lang.autodata.orm.Association;
import org.panda_lang.panda.utilities.commons.collection.Pair;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class SQLDataController implements DataController {

    private final Map<String, org.panda_lang.autodata.defaults.sql.SQLDataHandler> tablesHandlers = new HashMap<>();

    @Override
    public void initializeSchemes(Collection<? extends CollectionModel> schemes) {
        Map<String, Pair<String, String>> junctions = new HashMap<>();

        for (CollectionModel scheme : schemes) {
            tablesHandlers.put(scheme.getName(), new org.panda_lang.autodata.defaults.sql.SQLDataHandler());

            scheme.getEntityModel().getProperties().forEach((name, property) -> {
                property.getAnnotations().getAnnotation(Association.class).ifPresent(association -> {
                    Pair<String, String> content = new Pair<>(scheme.getEntityModel().getRootClass().getSimpleName(), association.type().getSimpleName());
                    junctions.put(association.name(), content);
                });
            });
        }

        junctions.forEach((name, scheme) -> {
            tablesHandlers.put(name, new org.panda_lang.autodata.defaults.sql.SQLDataHandler());
        });

        System.out.println("Generated handlers for tables: ");
        tablesHandlers.keySet().forEach(System.out::println);
    }

    @Override
    public void initializeCollections(Collection<? extends DataCollection> dataCollections) {

    }

    @Override
    public <ENTITY> DataHandler<ENTITY> getHandler(String collection) {
        return new org.panda_lang.autodata.defaults.sql.SQLDataHandler<>();
    }

    @Override
    public String getIdentifier() {
        return null;
    }

}
