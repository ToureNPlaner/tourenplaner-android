package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import java.util.Vector;

import android.app.Application;

public class NodeModel extends Application{
	
	private static Vector<Node> nodeVector = new Vector<Node>();

	public static Vector<Node> getNodeVector() {
		return nodeVector;
	}

	public static void setNodeVector(Vector<Node> nodeVector) {
		NodeModel.nodeVector = nodeVector;
	}

}
