/*
 * Copyright 2013 ToureNPlaner
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.uni.stuttgart.informatik.ToureNPlaner.Data.LocalStorage;

import android.provider.BaseColumns;

public final class RoutesStorageContract {
	// To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
	public RoutesStorageContract() {}

	/* Inner class that defines the table contents */
    public static abstract class RoutesEntry implements BaseColumns {
        public static final String TABLE_NAME = "routes";
        public static final String COLUMN_NAME_ENTRY_ID = "route_id";
        public static final String COLUMN_NAME_ROUTEDATA = "routedata";
        public static final String COLUMN_NAME_TBTROUTEDATA = "tbtroutedata";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
    }
}
