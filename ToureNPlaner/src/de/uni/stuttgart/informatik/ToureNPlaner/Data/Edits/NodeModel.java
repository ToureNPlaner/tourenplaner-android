package de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits;


import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;

import java.io.Serializable;
import java.util.ArrayList;

public class NodeModel implements Serializable {

	private ArrayList<Node> nodeArrayList = new ArrayList<Node>();

	private int version = 0;

	public int getVersion() {
		return version;
	}

	public void incVersion() {
		this.version++;
	}

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

	public boolean allSet() {
		for (int i = 0; i < nodeArrayList.size(); i++) {
			ArrayList<Constraint> list = nodeArrayList.get(i).getConstraintList();
			for (int n = 0; n < list.size(); n++) {
				if (list.get(n).getValue() == null)
					return false;
			}
		}

		return true;
	}
}
