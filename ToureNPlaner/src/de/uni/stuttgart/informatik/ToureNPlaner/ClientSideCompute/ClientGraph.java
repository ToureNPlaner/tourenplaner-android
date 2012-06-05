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
import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

/**
 * Class ClientGraph
 *
 * @author Niklas Schnelle
 */
public class ClientGraph {
    private static final long serialVersionUID = 1L;

	private final IntObjectOpenHashMap<IntArrayList> outEdgeIndices;
	private int edgeCount;
	private int nodeCount;
	private int origSrc;
	private int origTrgt;

	private IntArrayList edges;

	public ClientGraph(){
		this.outEdgeIndices = new IntObjectOpenHashMap<IntArrayList>();
		this.edges = new IntArrayList();
		edgeCount = 0;
		nodeCount = 0;
		origSrc = 0;
		origTrgt = 0;
	}


	public void setOrigSource(int src) {
		origSrc = src;
	}

	public void setOrigTarget(int trgt) {
		origTrgt = trgt;
	}

	public int getOrigSource(){
		return origSrc;
	}

	public int getOrigTarget() {
		return origTrgt;
	}

	public void addEdge(int srcId, int trgtId, int dist){
		edges.add(srcId, trgtId, dist);
		IntArrayList outAdd;
		if(!outEdgeIndices.containsKey(srcId)){
			outAdd = new IntArrayList(1);
			nodeCount++;
			outEdgeIndices.put(srcId, outAdd);
		} else {
			outAdd = outEdgeIndices.lget();
		}
		outAdd.add(edges.size()/3 - 1);
		edgeCount++;
	}

    /**
     * Gets the distance in the shortest path format that is multiplied for
     * travel time of the the edge given by it's edgeId (that's not an edgeNum
     * but a unique Id for each edge) get the Id with GetOutEdgeID() and
     * GetInEdgeID()
     *
     * @param edgeId
     * @return int
     */
    public final int getDist(int edgeId) {
        return edges.get((edgeId*3)+2);
    }

    /**
     * Gets the number of edges in the graph
     *
     * @return int
     */
    public final int getEdgeCount() {
        return edgeCount;
    }

    /**
     * Gets the number of nodes in the graph
     *
     * @return int
     */
    public final int getNodeCount() {
        return nodeCount;
    }


    /**
     * Gets the number of out going edges of the given node
     *
     * @param nodeId
     */
    public final int getOutEdgeCount(int nodeId) {
        return (outEdgeIndices.containsKey(nodeId))? outEdgeIndices.lget().size(): 0;
    }

    /**
     * Gets the edgeId of the out going edge identified by it's source node and
     * edgeNum
     *
     * @param nodeId
     * @param edgeNum
     * @return
     */
    public final int getOutEdgeId(int nodeId, int edgeNum) {
        return outEdgeIndices.get(nodeId).get(edgeNum);
    }

    /**
     * Gets the source of the the edge given by it's edgeId (that's not an
     * edgeNum but a unique Id for each edge) get the Id with GetOutEdgeID() and
     * GetInEdgeID()
     *
     * @param edgeId
     * @return int
     */
    public final int getSource(int edgeId) {
        return edges.get(edgeId*3);
    }

    /**
     * Gets the target of the the edge given by it's edgeId (that's not an
     * edgeNum but a unique Id for each edge) get the Id with GetOutEdgeID() and
     * GetInEdgeID()
     *
     * @param edgeId
     * @return int
     */
    public final int getTarget(int edgeId) {
	    return edges.get((edgeId *3) + 1);
    }
    
}
