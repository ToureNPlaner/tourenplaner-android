package de.uni.stuttgart.informatik.ToureNPlaner.Net;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.ServerInfo;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerInfoHandler extends ConnectionHandler {
	private String url;

	public ServerInfoHandler(Observer listener, String url) {
		super(listener);
		this.url = url;
	}

	@Override
	protected Object doInBackground(Void... voids) {
		try {
			URL uri = new URL(url + "/info");
			HttpURLConnection urlConnection = (HttpURLConnection) uri.openConnection();
			urlConnection.setRequestProperty("Accept", JacksonManager.ContentType.JSON.identifier);

			try {
				InputStream stream = new DoneHandlerInputStream(urlConnection.getInputStream());
				ObjectMapper mapper = JacksonManager.getMapper(JacksonManager.ContentType.parse(urlConnection.getContentType()));
				Session session = new Session();
				session.setServerInfo(ServerInfo.parse(mapper.readValue(stream, JsonNode.class)));
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
