package de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits;


import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;

import java.io.Serializable;
import java.util.ArrayList;

public class NodeModel implements Serializable {

	private ArrayList<Node> nodeArrayList = new ArrayList<Node>();

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

	void add(Node node) {
		nodeArrayList.add(node);
	}

	void addBeginning(Node node) {
		reverseNodes();
		nodeArrayList.add(node);
		reverseNodes();
	}

	void clear() {
		nodeArrayList.clear();
	}

	void remove(int pos) {
		nodeArrayList.remove(pos);
	}

	void reverseNodes() {
		for (int front = 0, back = nodeArrayList.size() - 1;
		     front < nodeArrayList.size() / 2;
		     front++, back--) {
			// swap
			Node tmp = nodeArrayList.set(front, nodeArrayList.get(back));
			nodeArrayList.set(back, tmp);
		}
	}
}
