package de.uni.stuttgart.informatik.ToureNPlaner.Net;

import java.io.*;

public class Util {
	public enum ContentType {
		JSON("application/json"), SMILE("application/x-jackson-smile");

		public final String identifier;

		private ContentType(String s) {
			identifier = s;
		}

		public static ContentType parse(String header) {
			String[] s = header.split(";");
			if(s.length < 0)
				throw new IllegalArgumentException();
			
			for(ContentType t : ContentType.values()) {
				if(s[0].toLowerCase().equals(t.identifier))
					return t;
			}

			throw new IllegalArgumentException();
		}
	}

    static String streamToString(InputStream stream) {
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];

        try {
            Reader reader = new InputStreamReader(stream);
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
