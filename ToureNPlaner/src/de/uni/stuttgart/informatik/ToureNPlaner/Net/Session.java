package de.uni.stuttgart.informatik.ToureNPlaner.Net;

import android.os.AsyncTask;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

public class Session {
    static class ConnectionHandler extends AsyncTask<Void, Void, Object> {
        Observer listener;
        String url;

        @Override
        protected void onPostExecute(Object object) {
            if (object instanceof ServerInfo) {
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
                    info.setUrl(url);
                    return info;
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                return e;
            }
        }
    }

    private ServerInfo serverInfo;

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
