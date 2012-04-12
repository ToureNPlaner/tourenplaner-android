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

package de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import org.mapsforge.core.GeoPoint;

public class UpdateDndEdit extends Edit {
	private final Node node;
	private final GeoPoint geoPoint;

	public UpdateDndEdit(Session session, Node node, GeoPoint geoPoint) {
		super(session);
		this.node = node;
		this.geoPoint = geoPoint;
	}

	@Override
	public void perform() {
		node.setGeoPoint(geoPoint);
		session.notifyChangeListerners(new Session.Change(Session.DND_CHANGE));
	}
}
