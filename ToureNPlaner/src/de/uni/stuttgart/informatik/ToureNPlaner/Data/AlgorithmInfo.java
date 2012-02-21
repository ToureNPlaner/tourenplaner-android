package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.ConstraintType;
import org.codehaus.jackson.JsonNode;

import java.io.Serializable;
import java.util.ArrayList;

public class AlgorithmInfo implements Serializable, Comparable<AlgorithmInfo> {
	private String version;
	private String name;
	private String urlsuffix;
	private String sslport;
	private int minPoints;
	private int maxPoints;
	private boolean sourceIsTarget;
	private boolean isHidden;
	private ArrayList<ConstraintType> constraintTypes;
	private ArrayList<ConstraintType> pointConstraintTypes;

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

	public ArrayList<ConstraintType> getPointConstraintTypes() {
		return pointConstraintTypes;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public int getMinPoints() {
		return minPoints;
	}

	public int getMaxPoints() {
		return maxPoints;
	}

	public String getSSLPort() {
		return sslport;
	}

	public static AlgorithmInfo parse(JsonNode object) {
		AlgorithmInfo info = new AlgorithmInfo();
		info.version = object.get("version").asText();
		info.name = object.get("name").asText();
		info.urlsuffix = object.get("urlsuffix").asText();

		JsonNode details = object.get("details");
		if (details != null) {
			info.minPoints = details.path("minpoints").asInt(0);
			info.maxPoints = details.path("maxpoints").asInt(Integer.MAX_VALUE);
			if (details.path("maxpoints").isMissingNode())
				info.maxPoints = Integer.MAX_VALUE;
			info.sourceIsTarget = details.path("sourceistarget").asBoolean();
			info.isHidden = details.path("hidden").asBoolean();
		}

		JsonNode constraints = object.get("constraints");
		if (constraints == null) {
			info.constraintTypes = new ArrayList<ConstraintType>();
		} else {
			info.constraintTypes = new ArrayList<ConstraintType>(constraints.size());
			for (JsonNode constraint : constraints) {
				
				info.constraintTypes.add(ConstraintType.parse(constraint));
			}
		}

		JsonNode pointconstraints = object.get("pointconstraints");
		if (pointconstraints == null) {
			info.pointConstraintTypes = new ArrayList<ConstraintType>();
		} else {
			info.pointConstraintTypes = new ArrayList<ConstraintType>(pointconstraints.size());
			for (JsonNode constraint : pointconstraints) {
				info.pointConstraintTypes.add(ConstraintType.parse(constraint));
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
		info.pointConstraintTypes = new ArrayList<ConstraintType>();
		info.constraintTypes = new ArrayList<ConstraintType>();
		//info.pointConstraintTypes.add(new Constraint("Price", "price", 0.0, 2000.0));
		return info;
	}

	public ArrayList<ConstraintType> getConstraintTypes() {
		return constraintTypes;
	}
}
