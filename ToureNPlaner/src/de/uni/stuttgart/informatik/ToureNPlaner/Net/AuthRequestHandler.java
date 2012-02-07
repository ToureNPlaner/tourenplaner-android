package de.uni.stuttgart.informatik.ToureNPlaner.Net;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.User;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;

public class AuthRequestHandler extends ConnectionHandler {
	private final Session session;

	public AuthRequestHandler(Observer listener, Session session) {
		super(listener);
		this.session = session;
	}

	@Override
	protected Object doInBackground(Void... voids) {
		try {
			HttpURLConnection urlConnection = session.openGetConnection("/authuser");

			try {
				InputStream stream = new DoneHandlerInputStream(urlConnection.getInputStream());
				ObjectMapper mapper = JacksonManager.getMapper(JacksonManager.ContentType.parse(urlConnection.getContentType()));
				session.setUser(User.parse(mapper.readValue(stream, JsonNode.class)));
				return true;
			} finally {
				urlConnection.disconnect();
			}
		} catch (Exception e) {
			return e;
		}
	}
}
