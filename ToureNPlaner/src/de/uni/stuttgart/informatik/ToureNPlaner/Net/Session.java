package de.uni.stuttgart.informatik.ToureNPlaner.Net;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

public class Session {



    class ConnectionHandler implements Runnable {
        Observer listener;
        String url;

        ConnectionHandler(String url, Observer listener) {
            this.listener = listener;
            this.url = url;
        }

        @Override
        public void run() {
            HttpURLConnection urlConnection = null;
            JSONObject object = null;
            try {
                URL uri = new URL(url+"/info");
                urlConnection = (HttpURLConnection) uri.openConnection();

                try {
                    String content = Util.streamToString(urlConnection.getInputStream());
                    object = new JSONObject(content);
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(object != null)
                listener.onCompleted(object);
            else {
                listener.onError(null);
            }
        }
    }

    private ServerInfo serverInfo;

    public void connect(String url, Observer listener) throws Exception {
        new Thread(new ConnectionHandler(url, listener)).start();
    }
}
