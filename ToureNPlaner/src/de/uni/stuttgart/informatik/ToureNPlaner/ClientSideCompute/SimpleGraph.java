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

package de.uni.stuttgart.informatik.ToureNPlaner.ClientSideCompute;

/**
 * Class ClientGraph
 *
 * @author Niklas Schnelle
 */
public interface SimpleGraph {

	/**
	 * Gets the distance in the shortest path format that is multiplied for
	 * travel time of the the edge given by it's edgeId (that's not an edgeNum
	 * but a unique Id for each edge) get the Id with GetOutEdgeID() and
	 * GetInEdgeID()
	 *
	 * @param edgeId
	 * @return int
	 */
	public int getDist(int edgeId);

	/**
	 * Gets the source of the the edge given by it's edgeId (that's not an
	 * edgeNum but a unique Id for each edge) get the Id with GetOutEdgeID() and
	 * GetInEdgeID()
	 *
	 * @param edgeId
	 * @return int
	 */
	public int getSource(int edgeId);

	/**
	 * Gets the target of the the edge given by it's edgeId (that's not an
	 * edgeNum but a unique Id for each edge) get the Id with GetOutEdgeID() and
	 * GetInEdgeID()
	 *
	 * @param edgeId
	 * @return int
	 */
	public int getTarget(int edgeId);

	/**
	 * Gets the number of edges in the graph
	 *
	 * @return int
	 */
	public int getEdgeCount();

	/**
	 * Gets the (maximum) number of nodes in the graph
	 * Due to nesting the actual number of nodes might be less than what this function reports.
	 *
	 * @return int
	 */
	public int getNodeCount();


	/**
	 * Gets the number of out going edges of the given node
	 *
	 * @param nodeId
	 */
	public int getOutEdgeCount(int nodeId);

	/**
	 * Gets the edgeId of the out going edge identified by it's source node and
	 * edgeNum
	 *
	 * @param nodeId
	 * @param edgeNum
	 * @return
	 */
	public int getOutEdgeId(int nodeId, int edgeNum);

	boolean hasNode(int nodeId);
}
