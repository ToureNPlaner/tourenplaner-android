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

import com.carrotsearch.hppc.IntArrayDeque;
import com.carrotsearch.hppc.IntIntOpenHashMap;

/**
 * Provides an implementation of ShortestPath algorithm.
 * 
 * @author Christoph Haag, Sascha Meusel, Niklas Schnelle, Peter Vollmer
 */
public class ShortestPath {

    /**
     * Performs the Dijkstra Search on euclidian dists stopping when
     * the destination point is removed from the pq
     *
     * @param dists
     * @return
     * @throws IllegalAccessException
     */
    public static final boolean dijkstraStopAtDest(ClientGraph graph, IntIntOpenHashMap dists, IntIntOpenHashMap predEdges)
            throws IllegalAccessException {
		int srcId = graph.getOrigSource();
	    int trgtId = graph.getOrigTarget();
        dists.put(srcId, 0);
        Heap heap = new Heap();
        heap.insert(srcId, 0);

        int nodeDist;
        int edgeId;
        int tempDist;
        int targetNode;
	    int targetDist;
        int nodeId = srcId;
        DIJKSTRA:
        while (!heap.isEmpty()) {
            nodeId = heap.peekMinId();
            nodeDist = heap.peekMinDist();
            heap.removeMin();
            if (nodeId == trgtId) {
                break DIJKSTRA;
            }
            if (dists.containsKey(nodeId) && nodeDist > dists.lget()) {
                continue;
            }
            int edgeCount = graph.getOutEdgeCount(nodeId);
            for (int i = 0; i < edgeCount; i++) {
                edgeId = graph.getOutEdgeId(nodeId, i);
                targetNode = graph.getTarget(edgeId);

                // with multiplier = shortest path
                tempDist = nodeDist + graph.getDist(edgeId);

	            targetDist = (dists.containsKey(targetNode))? dists.lget():Integer.MAX_VALUE;

                if (tempDist < targetDist) {
                    dists.put(targetNode, tempDist);
                    predEdges.put(targetNode, edgeId);
                    heap.insert(targetNode, tempDist);
                }
            }
        }
        return nodeId == trgtId;
    }

	public static final IntArrayDeque backtrack(ClientGraph graph, IntIntOpenHashMap predEdges){
		IntArrayDeque result = new IntArrayDeque();
		int srcId = graph.getOrigSource();
		int currId = graph.getOrigTarget();
		int edgeId;
		while (currId != srcId){
			result.addFirst(currId);
			edgeId = predEdges.get(currId);
			currId = graph.getSource(edgeId);
		}
		// don't forget to add the source
		result.addFirst(srcId);
		return result;
	}


}
