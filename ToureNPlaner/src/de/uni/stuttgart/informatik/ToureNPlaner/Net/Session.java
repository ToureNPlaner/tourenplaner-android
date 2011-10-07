package de.uni.stuttgart.informatik.ToureNPlaner.Net;

import android.os.AsyncTask;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.AlgorithmInfo;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.ServerInfo;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

public class Session implements Serializable {
    public static final String IDENTIFIER = "session";

    static class ConnectionHandler extends AsyncTask<Void, Void, Object> {
        Observer listener;
        String url;

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
                URL uri = new URL(url+"/info");
                HttpURLConnection urlConnection = (HttpURLConnection) uri.openConnection();

                try {
                    String content = Util.streamToString(urlConnection.getInputStream());
                    ServerInfo info = ServerInfo.parse(new JSONObject(content));
                    Session session = new Session();
                    session.serverInfo = info;
                    session.url = url;
                    return session;
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                return e;
            }
        }
    }

    private ServerInfo serverInfo;
    private String url;
    private AlgorithmInfo selectedAlgorithm;

    public AlgorithmInfo getSelectedAlgorithm() {
        return selectedAlgorithm;
    }

    public void setSelectedAlgorithm(AlgorithmInfo selectedAlgorithm) {
        this.selectedAlgorithm = selectedAlgorithm;
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    /**
     *
     * @param url The URL to connect to
     * @param listener the Callback listener
     * @return Use this to cancel the task with cancel(true)
     */
    public static ConnectionHandler connect(String url, Observer listener) {
        ConnectionHandler handler = new ConnectionHandler(url, listener);
        handler.execute();
        return handler;
    }
}
