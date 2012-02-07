package de.uni.stuttgart.informatik.ToureNPlaner.Net;

import android.util.Log;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Request;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Result;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;

public class RequestHandler extends ConnectionHandler {
	private final Session session;

	public RequestHandler(Session session, Observer listener) {
		super(listener);
		this.session = session;
	}

	@Override
	protected Object doInBackground(Void... voids) {
		try {
			HttpURLConnection urlConnection = session.openPostConnection("/alg" + session.getSelectedAlgorithm().getUrlsuffix(), true);

			try {
				ObjectMapper mapper = JacksonManager.getJsonMapper();
				JsonNode root = Request.generate(mapper.getNodeFactory(),
						session.getNodeModel().getNodeVector(),
						session.getSelectedAlgorithm().getConstraints());
				JsonGenerator generator = mapper.getJsonFactory()
						.createJsonGenerator(urlConnection.getOutputStream());
				mapper.writeTree(generator, root);
				generator.close();

				InputStream stream = new DoneHandlerInputStream(urlConnection.getInputStream());

				final long t0 = System.currentTimeMillis();
				Result result = Result.parse(JacksonManager.ContentType.parse(urlConnection.getContentType()), stream);
				Log.v("TP", "ResultParse: " + (System.currentTimeMillis() - t0) + " ms");

				return result;
			} finally {
				urlConnection.disconnect();
			}
		} catch (Exception e) {
			return e;
		}
	}
}
