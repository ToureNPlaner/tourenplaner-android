package de.uni.stuttgart.informatik.ToureNPlaner.Net;

import android.os.AsyncTask;
import android.util.Log;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.*;
import de.uni.stuttgart.informatik.ToureNPlaner.Util.Base64;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class Session implements Serializable {
    public static final String IDENTIFIER = "session";

	public Session() {
		this.uuid = UUID.randomUUID();
	}

	private final UUID uuid;
	private transient boolean dirty;

	private static class Data implements Serializable
	{
		private ServerInfo serverInfo;
	    private String url;
	    private String user;
	    private String password;
	    private AlgorithmInfo selectedAlgorithm;
	    private NodeModel nodeModel = new NodeModel();
		private Result result;
	}
	
	Data d = new Data();

    public Result getResult() {
        return d.result;
    }

    public void setResult(Result result) {
	    d.result = result;
    }

    public void setNodeModel(NodeModel nodeModel) {
        d.nodeModel = nodeModel;
    }

	public void setUrl(String url) {
		d.url = url;
	}

    public String getUrl() {
        return d.url;
    }

    public String getUser() {
        return d.user;
    }

    public String getPassword() {
        return d.password;
    }

    public NodeModel getNodeModel() {
        return d.nodeModel;
    }

    public AlgorithmInfo getSelectedAlgorithm() {
        return d.selectedAlgorithm;
    }

    public void setSelectedAlgorithm(AlgorithmInfo selectedAlgorithm) {
        d.selectedAlgorithm = selectedAlgorithm;
    }

	public void setServerInfo(ServerInfo serverInfo) {
       d.serverInfo = serverInfo;
    }

    public ServerInfo getServerInfo() {
        return d.serverInfo;
    }

    public void setUser(String user) {
        d.user = user;
    }

    public void setPassword(String password) {
        d.password = password;
    }

    /**
     * @param url      The URL to connect to
     * @param listener the Callback listener
     * @return Use this to cancel the task with cancel(true)
     */
    public static ConnectionHandler createSession(String url, Observer listener) {
        ConnectionHandler handler = new ConnectionHandler(url, listener);
        handler.execute();
        return handler;
    }
}
