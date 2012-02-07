package de.uni.stuttgart.informatik.ToureNPlaner.Net;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Request;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Result;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * RequestNN are not persistent, so we don't need to update the session etc.
 */
public class RequestNN extends ConnectionHandler {
	private final Session session;
	private final Node node;

	public RequestNN(Observer listener, Session session, Node node) {
		super(listener);
		this.session = session;
		this.node = node;
	}

	public Node getNode() {
		return node;
	}

	@Override
	protected Object doInBackground(Void... voids) {
		try {
			HttpURLConnection urlConnection = session.openPostConnection("/algnns", true);

			ArrayList<Node> nodes = new ArrayList<Node>(1);
			nodes.add(node);

			try {
				ObjectMapper mapper = JacksonManager.getJsonMapper();
				JsonNode root = Request.generate(mapper.getNodeFactory(),
						nodes,
						null);
				JsonGenerator generator = mapper.getJsonFactory()
						.createJsonGenerator(urlConnection.getOutputStream());
				mapper.writeTree(generator, root);
				generator.close();

				InputStream stream = urlConnection.getInputStream();

				Result result = Result.parse(JacksonManager.ContentType.parse(urlConnection.getContentType()), stream);
				return result.getPoints().get(0);
			} finally {
				urlConnection.disconnect();
			}
		} catch (Exception e) {
			return e;
		}
	}
}