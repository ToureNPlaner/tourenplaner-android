package de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import org.mapsforge.core.GeoPoint;

public class UpdateGeoPointEdit extends Edit {
	private final Node node;
	private final GeoPoint geoPoint;

	public UpdateGeoPointEdit(Session session, Node node, GeoPoint geoPoint) {
		super(session);
		this.node = node;
		this.geoPoint = geoPoint;
	}

	@Override
	public void perform() {
		node.setGeoPoint(geoPoint);
		session.notifyChangeListerners(Session.Change.MODEL_CHANGE);
	}
}
