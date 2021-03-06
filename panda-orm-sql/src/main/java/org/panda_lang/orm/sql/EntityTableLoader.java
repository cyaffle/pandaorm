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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.orm.PandaOrmException;
import org.panda_lang.orm.collection.CollectionModel;
import org.panda_lang.orm.collection.DataCollection;
import org.panda_lang.orm.collection.DataCollectionImpl;
import org.panda_lang.orm.properties.Association;
import org.panda_lang.orm.properties.Association.Relation;
import org.panda_lang.orm.properties.AssociationUtils;
import org.panda_lang.orm.properties.AutoIncrement;
import org.panda_lang.orm.properties.Id;
import org.panda_lang.orm.properties.NonNull;
import org.panda_lang.orm.properties.Unique;
import org.panda_lang.orm.serialization.MetadataImpl;
import org.panda_lang.orm.serialization.Type;
import org.panda_lang.orm.serialization.TypeImpl;
import org.panda_lang.orm.sql.containers.AssociativeRepository;
import org.panda_lang.orm.sql.containers.AssociativeTable;
import org.panda_lang.orm.sql.containers.Column;
import org.panda_lang.orm.sql.containers.Reference;
import org.panda_lang.orm.sql.containers.Table;
import org.panda_lang.orm.utils.Annotations;
import org.panda_lang.utilities.commons.collection.Pair;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

final class EntityTableLoader {

    private final DatabaseController databaseController;
    private final Map<Class<?>, Type<?>> types;

    EntityTableLoader(DatabaseController databaseController, Map<Class<?>, Type<?>> types) {
        this.databaseController = databaseController;
        this.types = types;
    }

    Map<String, Table> loadModels(Map<String, ? extends CollectionModel> models, Map<String, ? extends DataCollection> collections) {
        Map<String, Table> tables = new LinkedHashMap<>();
        Map<String, Pair<String, String>> associative = new HashMap<>();

        for (CollectionModel model : models.values()) {
            Table table = loadTable(model, collections.get(model.getName()), associative);
            tables.put(table.getName(), table);
            types.put(model.getEntityModel().getEntityType(), new TypeImpl<>(model.getEntityModel().getEntityType(), null, null, null));
        }

        associative.forEach((name, pair) -> {
            Table keyTable = tables.get(pair.getKey());
            Table valueTable = tables.get(pair.getValue());

            //CollectionModel collectionModel = new CollectionModel();
            DataCollection associativeCollection = new DataCollectionImpl(name, null, new AssociativeRepository(), Pair.class);
            tables.put(name, AssociativeTable.create(name, associativeCollection, keyTable, valueTable));
        });

        return tables;
    }

    Table loadTable(CollectionModel model, DataCollection collection, Map<String, Pair<String, String>> associative) {
        Map<String, Column<?>> columns = new HashMap<>();

        model.getEntityModel().getProperties().forEach((name, property) -> {
            Annotations annotations = property.getAnnotations();
            Association association = annotations.getAnnotation(Association.class).orElse(null);

            if (association != null) {
                if (association.relation() == Relation.MANY_TO_MANY) {
                    Pair<String, String> content = new Pair<>(model.getName(), association.ref());
                    associative.put(property.getName(), content);
                    return;
                }

                if (association.relation() == Relation.ONE_TO_MANY) {
                    return;
                }
            }

            @Nullable Reference reference = association == null ? null : databaseController -> {
                TableHandler<?> handler = databaseController.getHandler(AssociationUtils.getCollection(association.ref())).getOrElseThrow(() -> {
                    throw new PandaOrmException("Internal error: Unknown table " + association.relation()); // should never happen
                });

                return new Pair<>(handler.getTable(), handler.getTable().getPrimary());
            };

            Supplier<Type<?>> type;

            if (reference == null) {
                type = () -> types.get(property.getType());
            }
            else {
                type = () -> {
                    String collectionName = AssociationUtils.getCollection(association.ref());

                    TableHandler<?> tableHandler = databaseController.getHandler(collectionName).getOrElseThrow(() -> {
                        throw new PandaOrmException("Internal error: Unknown table " + collectionName);
                    });

                    return tableHandler.getTable().getPrimary().getType();
                };
            }

            Column<?> column = new Column<>(
                    name,
                    type,
                    new MetadataImpl(),
                    annotations.getAnnotation(Id.class).isPresent(),
                    annotations.getAnnotation(Unique.class).isPresent(),
                    annotations.getAnnotation(NonNull.class).isPresent(),
                    annotations.getAnnotation(AutoIncrement.class).isPresent(),
                    reference
            );

            columns.put(column.getName(), column);
        });

        return new Table(model.getName(), collection, columns);
    }

}
