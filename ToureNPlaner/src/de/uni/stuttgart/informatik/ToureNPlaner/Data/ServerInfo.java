package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class ServerInfo implements Serializable {
    public static enum ServerType {
        PUBLIC,
        PRIVATE
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

    public static ServerInfo parse(JSONObject object) throws JSONException {
        ServerInfo info = new ServerInfo();
        info.version = object.getString("version");
        info.serverType = ServerType.valueOf(object.getString("servertype").toUpperCase());
        info.SslPort = object.getInt("sslport");

        JSONArray array = object.getJSONArray("algorithms");
        info.algorithms = new ArrayList<AlgorithmInfo>(array.length());
        for(int i=0;i<array.length();i++) {
            info.algorithms.add(AlgorithmInfo.parse(array.getJSONObject(i)));
        }

        return info;
    }
}
