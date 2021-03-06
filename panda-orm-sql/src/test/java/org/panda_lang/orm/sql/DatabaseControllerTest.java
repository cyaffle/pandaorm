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

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.panda_lang.orm.PandaOrm;
import org.panda_lang.orm.entity.DataEntity;
import org.panda_lang.orm.properties.As;
import org.panda_lang.orm.properties.Association;
import org.panda_lang.orm.properties.Association.Relation;
import org.panda_lang.orm.properties.Entity;
import org.panda_lang.orm.properties.Generated;
import org.panda_lang.orm.properties.Id;
import org.panda_lang.orm.properties.Repository;
import org.panda_lang.utilities.commons.UnsafeUtils;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

final class DatabaseControllerTest {

    @Test
    void testSQL() {
        UnsafeUtils.disableIllegalAccessMessage();

        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.h2.Driver");
        config.setJdbcUrl("jdbc:h2:mem:test-database");
        config.setUsername("sa");
        config.setPassword("sa");
        HikariDataSource dataSource = new HikariDataSource(config);

        PandaOrm orm = PandaOrm.initialize(new DatabaseController(dataSource))
                .createCollection()
                    .name("users")
                    .entity(User.class)
                    .repository(UserRepository.class)
                    .append()
                .createCollection()
                    .name("groups")
                    .entity(Group.class)
                    .repository(GroupRepository.class)
                    .append()
                .collect();

        UserRepository userRepository = orm.getCollection("users").getRepository(UserRepository.class);
        GroupRepository groupRepository = orm.getCollection("groups").getRepository(GroupRepository.class);

        User user = userRepository.createUser("insertt");
        System.out.println("User: " + user);

        user.setName("makub");
        System.out.println("Updated user: " + user);

        Group group = groupRepository.createGroup("natural born pranksters");
        System.out.println("Group: " + group);

        user.setGroup(group);
        System.out.println("User group: " + user);
    }

    @Entity
    public interface User extends DataEntity<User> {

        @Association(ref = "groups", relation = Relation.MANY_TO_ONE)
        Group getGroup();
        void setGroup(Group group);

        void setName(String name);
        String getName();

        @Id
        @Generated
        UUID getId();

        @Override
        default String asString() {
            return "User { id=" + getId() + ", name=" + getName() + ", group=" + getGroup() + " }";
        }

    }

    @Entity
    public interface Group extends DataEntity<Group> {

        @Association(ref = "users:group", relation = Relation.ONE_TO_MANY)
        Collection<User> getMembers();

        void setName(String name);
        String getName();

        @Id
        @Generated
        UUID getId();

        @Override
        default String asString() {
            return "Group { id=" + getId() + ", name=" + getName() + ", members=" + getMembers() + " }";
        }

    }

    @Repository
    public interface GroupRepository extends SqlRepository<Group> {

        Group createGroup(@As("name") String name);

    }

    @Repository
    public interface UserRepository extends SqlRepository<User> {

        User createUser(@As("name") String name);

        Optional<User> findUserByName(String name);

        User findByNameOrId(String name, UUID id);

    }

}
