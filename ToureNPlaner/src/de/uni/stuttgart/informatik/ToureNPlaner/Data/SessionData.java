package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

//  this class stores all data from the userinput
public class SessionData {

    private static final String BundleString = "SessionData";
    private static final String PREFS_NAME = "SessionData";

    public static SessionData Instance;

    private transient final SharedPreferences settings;
    private Data data = new Data();

    static public void createInstance(Context context) {
        Instance = new SessionData(context);
    }

    public SessionData(Context context) {
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void save(Bundle outBundle) {
        outBundle.putSerializable(BundleString, Instance.data);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("ServerURL",data.ServerURL);
        editor.commit();
    }

    public void load(Bundle savedInstanceState) {
        data.ServerURL = settings.getString("ServerURL","");

    	if(savedInstanceState != null) {
	        Object savedData = savedInstanceState.get(BundleString);
	        if(savedData != null) {
                try {
	                Instance.data = (Data) savedData;
                } catch (Exception e) {
                    // Should only happen if the implementation has changed
                }
	        }
    	}
    }

	public String getServerURL() {
		return data.ServerURL;
	}

	public void setServerURL(String serverURL) {
		data.ServerURL = serverURL;
	}

	public Boolean getAlgorithmHasStarAndEndMarker() {
		return data.AlgorithmHasStarAndEndMarker;
	}

	public void setAlgorithmHasStarAndEndMarker(
		Boolean algorithmHasStarAndEndMarker) {
		data.AlgorithmHasStarAndEndMarker = algorithmHasStarAndEndMarker;
	}

	public String getEmail() {
		return data.email;
	}

	public void setEmail(String email) {
		data.email = email;
	}

	public String getPassword() {
		return data.password;
	}

	public void setPassword(String password) {
		data.password = password;
	}

	public String getChoosenAlgorithm() {
		return data.choosenAlgorithm;
	}

	public void setChoosenAlgorithm(String choosenAlgorithm) {
		data.choosenAlgorithm = choosenAlgorithm;
	}

	public void setSelectedNode(Integer selectedNode) {
		data.selectedNode = selectedNode;
	}

	public Integer getSelectedNode() {
		return data.selectedNode;
	}
}
