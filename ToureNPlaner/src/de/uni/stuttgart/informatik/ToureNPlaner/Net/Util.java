package de.uni.stuttgart.informatik.ToureNPlaner.Net;

import java.io.*;

public class Util {
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
