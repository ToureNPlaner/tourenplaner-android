package de.uni.stuttgart.informatik.ToureNPlaner.Net;

import android.os.AsyncTask;
import android.util.Log;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.*;
import de.uni.stuttgart.informatik.ToureNPlaner.Util.Base64;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class Session implements Serializable {
    public static final String IDENTIFIER = "session";

    /**
     * This input stream won't read() after the underlying stream is exhausted.
     * http://code.google.com/p/android/issues/detail?id=14562
     */
    static final class DoneHandlerInputStream extends FilterInputStream {
        private boolean done;

        public DoneHandlerInputStream(InputStream stream) {
            super(stream);
        }

        @Override
        public int read(byte[] bytes, int offset, int count) throws IOException {
            if (!done) {
                int result = super.read(bytes, offset, count);
                if (result != -1) {
                    return result;
                }
            }
            done = true;
            return -1;
        }
    }

    public static class ConnectionHandler extends AsyncTask<Void, Void, Object> {
        Observer listener;
        String url;

        public void setListener(Observer listener) {
            this.listener = listener;
        }

        @Override
        protected void onPostExecute(Object object) {
            if (object instanceof Session) {
                listener.onCompleted(object);
            } else {
                listener.onError(object);
            }
        }

        ConnectionHandler(String url, Observer listener) {
            super();
            this.listener = listener;
            this.url = url;
        }

        @Override
        protected Object doInBackground(Void... voids) {
            try {
                URL uri = new URL(url + "/info");
                HttpURLConnection urlConnection = (HttpURLConnection) uri.openConnection();

                try {
                    InputStream stream = new DoneHandlerInputStream(urlConnection.getInputStream());
                    String content = Util.streamToString(stream);
                    Session session = new Session();
                    session.setServerInfo(ServerInfo.parse(new JSONObject(content)));
                    session.setUrl(url);
                    return session;
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                return e;
            }
        }
    }

	public static class RequestHandler extends AsyncTask<Void, Void, Object> {
        Observer listener;
        final Session session;

        public void setListener(Observer listener) {
            this.listener = listener;
        }

        @Override
        protected void onPostExecute(Object object) {
            if (object instanceof Result) {
                listener.onCompleted(object);
            } else {
                listener.onError(object);
            }
        }

        public RequestHandler(Session session, Observer listener) {
            super();
            this.session = session;
            this.listener = listener;
        }

        @Override
        protected Object doInBackground(Void... voids) {
            try {
                URL uri = new URL(session.getUrl() + "/alg" + session.getSelectedAlgorithm().getUrlsuffix());

                HttpURLConnection urlConnection = (HttpURLConnection) uri.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setChunkedStreamingMode(0);
                urlConnection.setRequestProperty("Content-Type", "application/json;");
                if (session.getServerInfo().getServerType() == ServerInfo.ServerType.PRIVATE) {
                    String userPassword = session.getUser() + ":" + session.getPassword();
                    String encoding = Base64.encodeString(userPassword);
                    urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
                }

                try {
                    String str = Request.generate(session).toString();
                    OutputStream outputStream = urlConnection.getOutputStream();
                    outputStream.write(str.getBytes("US-ASCII"));
                    InputStream stream = new DoneHandlerInputStream(urlConnection.getInputStream());

                    final long t0 = System.currentTimeMillis();
                    Result result = Result.parse(stream);
                    Log.v("TP", "ResultParse: " + (System.currentTimeMillis() - t0) + " ms");

                    return result;
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                return e;
            }
        }
    }

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

	private void setUrl(String url) {
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
