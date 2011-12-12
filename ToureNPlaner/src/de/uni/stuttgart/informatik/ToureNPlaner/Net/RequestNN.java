package de.uni.stuttgart.informatik.ToureNPlaner.Net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.mapsforge.android.maps.GeoPoint;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Request;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Result;

public class RequestNN extends ConnectionHandler {
	private final Session session;

	public RequestNN(Observer listener, Session session) {
		super(listener);
		this.session = session;
	}

	@Override
protected Object doInBackground(Void... voids) {
	try {
		HttpURLConnection urlConnection = session.openConnection("/algnns");
		urlConnection.setDoOutput(true);
		urlConnection.setChunkedStreamingMode(0);
		urlConnection.setRequestProperty("Content-Type", "application/json;");
	
		try{
			String str = Request.generate(session).toString();
		    OutputStream outputStream = urlConnection.getOutputStream();
			outputStream.write(str.getBytes("US-ASCII"));
			InputStream stream = urlConnection.getInputStream();
			Result result = Result.parse(stream);
			return result.getPoints();

		
		} finally {
			urlConnection.disconnect();
		}
	} catch (Exception e) {
		return e;
	}
}
}