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

import org.junit.jupiter.api.Test;
import org.panda_lang.autodata.AutomatedDataSpace;
import org.panda_lang.autodata.defaults.sql.SQLDataController;
import org.panda_lang.utilities.commons.UnsafeUtils;

final class ADSQLControllerTest {

    @Test
    void testSQL() {
        UnsafeUtils.disableIllegalAccessMessage();

        SQLDataController sqlController = new SQLDataController();

        AutomatedDataSpace space = AutomatedDataSpace.initialize(sqlController)
                .createCollection()
                    .name("users")
                    .entity(User.class)
                    .service(UserService.class)
                    .repository(UserRepository.class)
                    .append()
                .createCollection()
                    .name("groups")
                    .entity(Group.class)
                    .service(GroupService.class)
                    .repository(GroupRepository.class)
                    .append()
                .collect();
    }


}
