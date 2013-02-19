/*
 * Copyright 2012 ToureNPlaner
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

package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.ConstraintType;
import org.mapsforge.core.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;

public class Node implements Serializable {
	private String name;
	private String shortName;
	private GeoPoint geoPoint;

	private int id;

	private ArrayList<Constraint> constraintList;
	private ArrayList<ConstraintType> constraintTypes;

	public Node(int id, String name, String shortName, GeoPoint point, ArrayList<ConstraintType> constraintTypes) {
		this.id = id;
		this.name = name;
		this.shortName = shortName;
		this.geoPoint = point;
		if (constraintTypes != null) {
			this.constraintTypes = constraintTypes;
			constraintList = new ArrayList<Constraint>(constraintTypes.size());
			for (int i = 0; i < constraintTypes.size(); i++) {
				constraintList.add(new Constraint(constraintTypes.get(i)));
			}
		}
	}


	public void setGeoPoint(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public int getLaE7() {
		return geoPoint.latitudeE6 * 10;
	}

	public int getLoE7() {
		return geoPoint.longitudeE6 * 10;
	}

	public ArrayList<Constraint> getConstraintList() {
		return constraintList;
	}

	public GeoPoint getGeoPoint() {
		return geoPoint;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setConstraintList(ArrayList<Constraint> constraintList) {
		this.constraintList = constraintList;
	}

	public ArrayList<ConstraintType> getConstraintTypes() {
		return constraintTypes;
	}
}

