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
 * Created with IntelliJ IDEA.
 * User: niklas
 * Date: 9/29/12
 * Time: 9:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class NullGraph implements  SimpleGraph{
	@Override
	public final int getDist(int edgeId) {
		return -1;
	}

	@Override
	public final int getSource(int edgeId) {
		return -1;
	}

	@Override
	public final int getTarget(int edgeId) {
		return -1;
	}

	@Override
	public final int getEdgeCount() {
		return 0;
	}

	@Override
	public final int getNodeCount() {
		return 0;
	}

	@Override
	public final int getOutEdgeCount(int nodeId) {
		return 0;
	}

	@Override
	public final int getOutEdgeId(int nodeId, int edgeNum) {
		return -1;
	}

	@Override
	public final boolean hasNode(int nodeId){
		return false;
	}
}
