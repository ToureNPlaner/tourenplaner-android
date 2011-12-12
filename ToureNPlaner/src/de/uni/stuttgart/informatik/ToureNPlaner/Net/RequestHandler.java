package de.uni.stuttgart.informatik.ToureNPlaner.Net;

import android.os.AsyncTask;
import android.util.Log;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Request;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Result;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.ServerInfo;
import de.uni.stuttgart.informatik.ToureNPlaner.Util.Base64;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestHandler extends ConnectionHandler {
	private final Session session;

	public RequestHandler(Session session, Observer listener) {
		super(listener);
		this.session = session;
	}

	@Override
	protected Object doInBackground(Void... voids) {
		try {
			HttpURLConnection urlConnection = session.openPostConnection("/alg" + session.getSelectedAlgorithm().getUrlsuffix());

			try {
				String str = Request.generate(session.getNodeModel().getNodeVector()).toString();
				OutputStream outputStream = urlConnection.getOutputStream();
				outputStream.write(str.getBytes("US-ASCII"));
				InputStream stream = new DoneHandlerInputStream(urlConnection.getInputStream());

				final long t0 = System.currentTimeMillis();
				Result result = Result.parse(stream);
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
