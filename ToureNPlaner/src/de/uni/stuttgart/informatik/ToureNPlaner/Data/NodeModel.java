package de.uni.stuttgart.informatik.ToureNPlaner.Data;


import java.io.Serializable;
import java.util.ArrayList;

import org.mapsforge.android.maps.GeoPoint;

public class NodeModel implements Serializable {

	private ArrayList<Node> nodeArrayList = new ArrayList<Node>();
	private static GeoPoint GPSGeoPoint;
   
	public static final String IDENTIFIER = "nodemodel";

	public ArrayList<Node> getNodeVector() {
		return nodeArrayList;
	}

	public Node get(int i) {
		return nodeArrayList.get(i);
	}

	public Integer size() {
		return nodeArrayList.size();
	}

	public void addNodeToVector(Node node) {
		nodeArrayList.add(node);
	}
	public void setNodeToVector(int index,Node node) {
		nodeArrayList.set(index,node);
	}
	public void addNodeAtIndexToVector(int index,Node node) {
		nodeArrayList.add(index,node);
	}
	public void clear(){
		nodeArrayList.clear();
	}

	public void remove(int pos) {
		nodeArrayList.remove(pos);
		}
public void revertNodes(){
	ArrayList<Node> tempArrayList = new ArrayList<Node>();
	for (int i = 0; i<=nodeArrayList.size() -1 ; i++){
		tempArrayList.add(nodeArrayList.get(i));
	}
	nodeArrayList.clear();
	for (int i = tempArrayList.size() - 1 ; i >= 0;i--){
		nodeArrayList.add(tempArrayList.get(i));
	
	}
	tempArrayList.clear();
}

public GeoPoint getGPSGeoPoint() {
	return GPSGeoPoint;
}

public void setGPSGeoPoint(GeoPoint gPSGeoPoint) {
	GPSGeoPoint = gPSGeoPoint;
}
	
}
