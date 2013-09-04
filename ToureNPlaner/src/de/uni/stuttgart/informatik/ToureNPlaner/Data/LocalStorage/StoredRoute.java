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

import de.uni.stuttgart.informatik.ToureNPlaner.Data.AlgorithmInfo;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Result;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.TBTResult;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.Util.CoordinateTools;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class StoredRoute implements Serializable {
	public Result result;
	public long timestamp;
	public TBTResult tbtresult;

	public int getNumnodes() {
		int[][] subways = result.getWay();
		int numnodes = 0;
		for (int[]	subway : subways) {
			numnodes += subway.length;
		}
		return numnodes;
	}

	private static double factor = 1000000;

	public int getMeters() {
		int meter = 0;
		for (int[] subway : result.getWay()) {
			for (int i = 0; i < subway.length-2; i+=2) {
				meter += CoordinateTools.directDistance(subway[i+1]/factor, subway[i]/factor, subway[i+3]/factor, subway[i+2]/factor);
			}
		}
		return meter;
	}

	public AlgorithmInfo getAlgorithm(Session session) {
		String alg = result.getMisc().getAlgorithm();
		for (AlgorithmInfo ai : session.getServerInfo().getAlgorithms()) {
			if (ai.getName().equals(alg)) {
				return ai;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormater.format(new Date(timestamp*1000)) + " (" + result.getMisc().getAlgorithm() +  ", " + result.getMisc().getDistance() / 1000 +" Kilometer)";
	}
}
