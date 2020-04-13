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

package org.panda_lang.orm.sql;

import org.panda_lang.orm.collection.CollectionModel;
import org.panda_lang.orm.collection.DataCollection;
import org.panda_lang.orm.collection.DataCollectionImpl;
import org.panda_lang.orm.properties.Association;
import org.panda_lang.orm.properties.Association.Relation;
import org.panda_lang.orm.properties.Id;
import org.panda_lang.orm.properties.NonNull;
import org.panda_lang.orm.properties.Unique;
import org.panda_lang.orm.serialization.Type;
import org.panda_lang.orm.sql.containers.AssociativeRepository;
import org.panda_lang.orm.sql.containers.AssociativeTable;
import org.panda_lang.orm.sql.containers.Column;
import org.panda_lang.orm.sql.containers.Table;
import org.panda_lang.orm.utils.Annotations;
import org.panda_lang.utilities.commons.collection.Pair;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

final class EntityTableLoader {

    private final Map<Class<?>, Type<?>> types;

    EntityTableLoader(Map<Class<?>, Type<?>> types) {
        this.types = types;
    }

    Collection<Table> loadModels(Map<String, ? extends CollectionModel> models, Map<String, ? extends DataCollection> collections) {
        Map<String, Table> tables = new HashMap<>();
        Map<String, Pair<String, String>> associative = new HashMap<>();

        for (CollectionModel model : models.values()) {
            Table table = loadTable(model, collections.get(model.getName()), associative);
            tables.put(table.getName(), table);
        }

        associative.forEach((name, pair) -> {
            Table keyTable = tables.get(pair.getKey());
            Table valueTable = tables.get(pair.getValue());

            DataCollection associativeCollection = new DataCollectionImpl(name, Pair.class, new AssociativeRepository());
            tables.put(name, new AssociativeTable(name, associativeCollection, keyTable, valueTable));
        });

        return tables.values();
    }

    Table loadTable(CollectionModel model, DataCollection collection, Map<String, Pair<String, String>> associative) {
        Map<String, Column<?>> columns = new HashMap<>();

        model.getEntityModel().getProperties().forEach((name, property) -> {
            Annotations annotations = property.getAnnotations();

            Column<?> column = new Column<>(
                    name,
                    types.get(property.getType()),
                    annotations.getAnnotation(Id.class).isPresent(),
                    annotations.getAnnotation(Unique.class).isPresent(),
                    annotations.getAnnotation(NonNull.class).isPresent()
            );

            columns.put(column.getName(), column);
        });

        model.getEntityModel().getProperties().forEach((name, property) -> {
            property.getAnnotations().getAnnotation(Association.class)
                    .filter(association -> association.relation() == Relation.MANY)
                    .ifPresent(association -> {
                        Pair<String, String> content = new Pair<>(model.getName(), association.name());
                        associative.put(property.getName(), content);
                    });
        });


        return new Table(model.getName(), collection, columns);
    }

}
