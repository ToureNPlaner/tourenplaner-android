package de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.User;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.JacksonManager;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.InputStream;

public class AuthRequestHandler extends ConnectionHandler {
	public AuthRequestHandler(Observer listener, Session session) {
		super(listener, session);
	}

	@Override
	protected boolean isPost() {
		return false;
	}

	@Override
	protected String getSuffix() {
		return "/authuser";
	}

	@Override
	protected Object handleInput(JacksonManager.ContentType type, InputStream inputStream) throws Exception {
		ObjectMapper mapper = JacksonManager.getMapper(type);
		session.setUser(User.parse(mapper.readValue(inputStream, JsonNode.class)));
		return true;
	}
}
