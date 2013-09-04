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

import de.uni.stuttgart.informatik.ToureNPlaner.Data.Result;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class StoredRoute {
	public Result result;
	public long timestamp;

	public int getNumnodes() {
		int[][] subways = result.getWay();
		int numnodes = 0;
		for (int[]	subway : subways) {
			numnodes += subway.length;
		}
		return numnodes;
	}

	@Override
	public String toString() {
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormater.format(new Date(timestamp*1000)) + " (" + getNumnodes() +")";
	}
}
