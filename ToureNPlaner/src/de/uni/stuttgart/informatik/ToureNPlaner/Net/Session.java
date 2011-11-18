package de.uni.stuttgart.informatik.ToureNPlaner.Net;

import android.content.Context;
import android.util.Log;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.AlgorithmInfo;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.NodeModel;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Result;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.ServerInfo;
import de.uni.stuttgart.informatik.ToureNPlaner.ToureNPlanerApplication;

import java.io.*;
import java.util.UUID;

public class Session implements Serializable {
    public static final String IDENTIFIER = "session";

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

	private final UUID uuid;
	private static transient Data d;

	public Session() {
		this.uuid = UUID.randomUUID();
		this.d = new Data();
	}

	public void safe() {
		try {
            FileOutputStream outputStream = ToureNPlanerApplication.getContext().openFileOutput(uuid.toString(), Context.MODE_PRIVATE);
            try {
                ObjectOutputStream out = new ObjectOutputStream(outputStream);
                out.writeObject(d);
            } finally {
                outputStream.close();
            }
        } catch (Exception e) {
			Log.e("ToureNPLaner","Session loading failed", e);
        }
	}

	private void load() {
		try {
            FileInputStream inputStream = ToureNPlanerApplication.getContext().openFileInput(uuid.toString());
            try {
                ObjectInputStream in = new ObjectInputStream(inputStream);
                d = (Data) in.readObject();
            } finally {
                inputStream.close();
            }
        } catch (Exception e) {
			Log.e("ToureNPLaner","Session loading failed", e);
        }
	}

	private void checkData() {
		if(d == null)
			load();
	}

	public Result getResult() {
	    checkData();
        return d.result;
    }

    public void setResult(Result result) {
	    checkData();
	    d.result = result;
	    safe();
    }

    public void setNodeModel(NodeModel nodeModel) {
	    checkData();
        d.nodeModel = nodeModel;
	    safe();
    }

	public void setUrl(String url) {
		checkData();
		d.url = url;
		safe();
	}

    public String getUrl() {
	    checkData();
        return d.url;
    }

    public String getUser() {
	    checkData();
        return d.user;
    }

    public String getPassword() {
	    checkData();
        return d.password;
    }

    public NodeModel getNodeModel() {
	    checkData();
        return d.nodeModel;
    }

    public AlgorithmInfo getSelectedAlgorithm() {
	    checkData();
        return d.selectedAlgorithm;
    }

    public void setSelectedAlgorithm(AlgorithmInfo selectedAlgorithm) {
	    checkData();
        d.selectedAlgorithm = selectedAlgorithm;
	    safe();
    }

	public void setServerInfo(ServerInfo serverInfo) {
		checkData();
        d.serverInfo = serverInfo;
		safe();
    }

    public ServerInfo getServerInfo() {
	    checkData();
        return d.serverInfo;
    }

    public void setUser(String user) {
	    checkData();
        d.user = user;
	    safe();
    }

    public void setPassword(String password) {
	    checkData();
        d.password = password;
	    safe();
    }

    /**
     * @param url      The URL to connect to
     * @param listener the Callback listener
     * @return Use this to cancel the task with cancel(true)
     */
    public static ServerInfoHandler createSession(String url, Observer listener) {
        ServerInfoHandler handler = new ServerInfoHandler(listener, url);
        handler.execute();
        return handler;
    }
}
