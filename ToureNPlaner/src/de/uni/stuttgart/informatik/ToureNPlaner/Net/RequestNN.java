package de.uni.stuttgart.informatik.ToureNPlaner.Net;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.Request;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Result;

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
	
		try{
			String str = Request.generate(nodes).toString();
		    OutputStream outputStream = urlConnection.getOutputStream();
			outputStream.write(str.getBytes("US-ASCII"));
			InputStream stream = urlConnection.getInputStream();
			Result result = Result.parse(Util.ContentType.parse(urlConnection.getContentType()), stream);
			return result.getPoints()[0][0];

		
		} finally {
			urlConnection.disconnect();
		}
	} catch (Exception e) {
		return e;
	}
}
}