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

package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serializable;
import java.util.ArrayList;

public class ServerInfo implements Serializable {
	public static enum ServerType {
		PUBLIC,
		PRIVATE
	}

	public ServerInfo(){
	}

	private String version;
	private ServerType serverType;
	private int port;
	private int SslPort;
	private String hostname;

	public String getHostname() {
		return hostname;
	}

	public String getProtocol() {
		return serverType == ServerType.PRIVATE ? "https" : "http";
	}

	public String getURL() {
		return getProtocol() + "://" + getHostname() + ":" + (serverType == ServerType.PRIVATE ? getSslPort() : getPort());
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getVersion() {
		return version;
	}

	public ServerType getServerType() {
		return serverType;
	}

	public int getSslPort() {
		return SslPort;
	}

	public ArrayList<AlgorithmInfo> getAlgorithms() {
		return algorithms;
	}

	private ArrayList<AlgorithmInfo> algorithms;

	public static ServerInfo parse(JsonNode object) {
		ServerInfo info = new ServerInfo();
		info.version = object.get("version").asText();
		info.serverType = ServerType.valueOf(object.get("servertype").asText().toUpperCase());
		info.SslPort = object.get("sslport").asInt();

		JsonNode algorithms = object.get("algorithms");
		info.algorithms = new ArrayList<AlgorithmInfo>(algorithms.size());
		for (JsonNode node : algorithms) {
			info.algorithms.add(AlgorithmInfo.parse(node));
		}

		info.algorithms.add(AlgorithmInfo.createTestClientSide());
		return info;
	}
}
