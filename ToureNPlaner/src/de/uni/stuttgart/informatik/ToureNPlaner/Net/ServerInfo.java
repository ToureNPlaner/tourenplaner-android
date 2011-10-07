package de.uni.stuttgart.informatik.ToureNPlaner.Net;

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
    private int SslPort;

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

    static ServerInfo parse(JSONObject object) throws JSONException {
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
