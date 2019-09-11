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

package org.panda_lang.autodata.sql;

import org.panda_lang.panda.utilities.autodata.data.entity.DataEntity;
import org.panda_lang.panda.utilities.autodata.orm.Association;
import org.panda_lang.panda.utilities.autodata.orm.Generated;
import org.panda_lang.panda.utilities.autodata.orm.Id;
import org.panda_lang.panda.utilities.autodata.stereotype.Entity;

import java.util.Collection;
import java.util.UUID;

@Entity
public interface Group extends DataEntity {

    @Association(name = "members", type = User.class, relation = Association.Type.MANY)
    Collection<User> getMembers();
    //void addMember(User member);

    void setName(String name);
    String getName();

    @Id
    @Generated
    UUID getId();

}
