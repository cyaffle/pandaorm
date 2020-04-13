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

package org.panda_lang.orm;

import org.panda_lang.orm.structure.repository.DataController;
import org.panda_lang.orm.structure.collection.DataCollectionStereotype;
import org.panda_lang.utilities.inject.DependencyInjection;
import org.panda_lang.utilities.inject.Injector;

import java.util.ArrayList;
import java.util.List;

public final class AutomatedDataSpaceCreator {

    protected final Injector injector;
    protected final DataController controller;
    protected final List<DataCollectionStereotype> stereotypes = new ArrayList<>();

    AutomatedDataSpaceCreator(DataController controller) {
        this.controller = controller;
        this.injector = DependencyInjection.createInjector();
    }

    public AutomatedDataSpaceCreator withStereotype(DataCollectionStereotype stereotype) {
        stereotypes.add(stereotype);
        return this;
    }

    public DataCollectionStereotype.DataCollectionStereotypeBuilder createCollection() {
        return DataCollectionStereotype.builder(this);
    }

    public AutomatedDataSpace collect() {
        if (controller == null) {
            throw new AutomatedDataException("Missing data controller");
        }

        AutomatedDataSpace automatedDataSpace = new AutomatedDataSpace(controller);

        AutomatedDataSpaceInitializer dataSpaceInitializer = new AutomatedDataSpaceInitializer(automatedDataSpace, injector);
        dataSpaceInitializer.initialize(stereotypes).forEach(automatedDataSpace::addCollection);

        return automatedDataSpace;
    }

}