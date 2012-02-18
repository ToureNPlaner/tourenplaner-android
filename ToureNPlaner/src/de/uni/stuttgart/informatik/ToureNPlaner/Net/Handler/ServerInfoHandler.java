package de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.ServerInfo;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.JacksonManager;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerInfoHandler extends RawHandler {
	private final String url;

	public ServerInfoHandler(Observer listener, String url) {
		super(listener);
		this.url = url;
	}

	@Override
	protected HttpURLConnection getHttpUrlConnection() throws Exception {
		URL uri = new URL(url + "/info");
		HttpURLConnection urlConnection = (HttpURLConnection) uri.openConnection();
		return urlConnection;
	}

	@Override
	protected Object handleInput(JacksonManager.ContentType type, InputStream inputStream) throws Exception {
		ObjectMapper mapper = JacksonManager.getMapper(type);
		Session session = new Session();
		session.setServerInfo(ServerInfo.parse(mapper.readValue(inputStream, JsonNode.class)));
		session.setUrl(url);
		return session;
	}

}
