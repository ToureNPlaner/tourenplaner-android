package de.uni.stuttgart.informatik.ToureNPlaner.Net;

import android.os.AsyncTask;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.*;
import de.uni.stuttgart.informatik.ToureNPlaner.Util.Base64;
import org.json.JSONObject;

import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

public class Session implements Serializable {
    public static final String IDENTIFIER = "session";

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
                String userPassword = session.getUser() + ":" + session.getPassword();
                String encoding =  Base64.encodeString(userPassword);
                urlConnection.setRequestProperty("Authorization","Basic " + encoding);
                
                OutputStream outputStream =  urlConnection.getOutputStream();
                String str = Request.generate(session).toString();
                outputStream.write(str.getBytes());

                try {

                    String content = Util.streamToString(urlConnection.getInputStream());

                    return Result.parse(new JSONObject(content));
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
    private String user;
    private String password;
    private AlgorithmInfo selectedAlgorithm;
    private NodeModel nodeModel = new NodeModel();

    public void setNodeModel(NodeModel nodeModel) {
        this.nodeModel = nodeModel;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public NodeModel getNodeModel() {
        return nodeModel;
    }

    public AlgorithmInfo getSelectedAlgorithm() {
        return selectedAlgorithm;
    }

    public void setSelectedAlgorithm(AlgorithmInfo selectedAlgorithm) {
        this.selectedAlgorithm = selectedAlgorithm;
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
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
