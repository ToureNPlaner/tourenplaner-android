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

	public synchronized ArrayList<Node> getNodeVector() {
		return nodeArrayList;
	}

	public synchronized void setNodeVector(ArrayList<Node> nodeArrayList) {
		incVersion();
		this.nodeArrayList = nodeArrayList;
	}

	public synchronized Node get(int i) {
		return nodeArrayList.get(i);
	}

	public synchronized Integer size() {
		return nodeArrayList.size();
	}

	synchronized void add(Node node) {
		nodeArrayList.add(node);
	}

	synchronized void addBeginning(Node node) {
		reverseNodes();
		nodeArrayList.add(node);
		reverseNodes();
	}

	public synchronized void clear() {
		nodeArrayList.clear();
	}

	synchronized void remove(int pos) {
		nodeArrayList.remove(pos);
	}

	synchronized void reverseNodes() {
		int first = 0;
		int last = nodeArrayList.size();
		while ((first != last) && (first != --last)) {
			Node tmp = nodeArrayList.set(first++, nodeArrayList.get(last));
			nodeArrayList.set(last, tmp);
		}
	}

	public synchronized boolean allSet() {
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
