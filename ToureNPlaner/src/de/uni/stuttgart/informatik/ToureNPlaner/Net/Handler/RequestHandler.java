package de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler;

import android.util.Log;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraint;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Request;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Result;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.JacksonManager;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class RequestHandler extends ConnectionHandler {
	private int version;

	public RequestHandler(Observer listener, Session session) {
		super(listener, session);
	}

	@Override
	protected boolean isPost() {
		return true;
	}

	@Override
	protected String getSuffix() {
		return "/alg" + session.getSelectedAlgorithm().getUrlsuffix();
	}

	protected ArrayList<Node> getNodes() {
		return session.getNodeModel().getNodeVector();
	}

	protected ArrayList<Constraint> getConstraints() {
		return session.getSelectedAlgorithm().getConstraints();
	}

	@Override
	protected void handleOutput(OutputStream outputStream) throws Exception {
		ObjectMapper mapper = JacksonManager.getJsonMapper();
		version = session.getNodeModel().getVersion();
		JsonNode root = Request.generate(mapper.getNodeFactory(),
				getNodes(),
				getConstraints());
		JsonGenerator generator = mapper.getJsonFactory()
				.createJsonGenerator(outputStream);
		mapper.writeTree(generator, root);
		generator.close();
	}

	@Override
	protected Object handleInput(JacksonManager.ContentType type, InputStream inputStream) throws Exception {
		final long t0 = System.currentTimeMillis();
		Result result = Result.parse(type, inputStream);
		Log.v("TP", "ResultParse: " + (System.currentTimeMillis() - t0) + " ms");
		result.setVersion(version);
		return result;
	}
}
