package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Request {

    private static JSONObject generate(Node node) throws JSONException {
        JSONObject o = new JSONObject();
        o.put("lt", node.getLaE7());
        o.put("ln", node.getLoE7());
	    o.put("name", node.getName());
        return o;
    }


    public static JSONObject generate(ArrayList<Node> nodes) {
        JSONObject o = new JSONObject();
        JSONArray a = new JSONArray();
        try {
            for (int i = 0; i < nodes.size(); i++) {
                a.put(generate(nodes.get(i)));
            }

            o.put("points", a);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return o;
    }
}
