package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class AlgorithmInfo implements Serializable, Comparable<AlgorithmInfo> {
	private String version;
	private String name;
	private String urlsuffix;
	private String sslport;
	private ArrayList<Constraint> point_constraints;
	private int minPoints;
	private boolean sourceIsTarget;
	private boolean isHidden;

	private AlgorithmInfo() {
	}

	@Override
	public String toString() {
		return name;
	}

	public boolean sourceIsTarget() {
		return sourceIsTarget;
	}

	public String getUrlsuffix() {
		return urlsuffix;
	}

	public ArrayList<Constraint> getPointConstraints() {
		return point_constraints;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public int getMinPoints() {
		return minPoints;
	}
	
	public String getSSLPort() {
		return sslport;
	}

	public static AlgorithmInfo parse(JSONObject object) throws JSONException {
		AlgorithmInfo info = new AlgorithmInfo();
		info.version = object.getString("version");
		info.name = object.getString("name");
		info.urlsuffix = object.getString("urlsuffix");
	//	info.sslport = object.getString("sslport");
		
		if (!object.isNull("details")) {
			info.minPoints = object.getJSONObject("details").getInt("minpoints");
			info.sourceIsTarget = object.getJSONObject("details").getBoolean("sourceistarget");
			info.isHidden = object.getJSONObject("details").getBoolean("hidden");
		}
	

		if (object.isNull("pointconstraints")) {
			info.point_constraints = new ArrayList<Constraint>();
		} else {
			JSONArray array = object.getJSONArray("pointconstraints");
			info.point_constraints = new ArrayList<Constraint>(array.length());
			for (int i = 0; i < array.length(); i++) {
				info.point_constraints.add(Constraint.parse(array.getJSONObject(i)));
			}
		}
		return info;
	}

	@Override
	public int compareTo(AlgorithmInfo another) {
		if (another == null)
			return 0;

		return name.compareTo(another.name);
	}

	public static AlgorithmInfo createMock() {
		AlgorithmInfo info = new AlgorithmInfo();
		info.name = "Mock";
		info.version = "";
		info.urlsuffix = "";
		info.point_constraints = new ArrayList<Constraint>();
		info.point_constraints.add(new Constraint("Price", "price", 0.0, 2000.0));
		return info;
	}
}
