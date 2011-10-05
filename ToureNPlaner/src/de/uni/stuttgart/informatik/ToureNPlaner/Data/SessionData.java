package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import android.app.Application;
import android.os.Bundle;

import java.io.Serializable;

//  this class stores all data from the userinput
public class SessionData implements Serializable {

    public transient static SessionData Instance = new SessionData();
    private static final String BundleString = "SessionData";

    private Integer selectedNode = 0;
	private String email;
	private String password = "";
	private String choosenAlgorithm;
    private Boolean AlgorithmHasStarAndEndMarker = true;
	//TODO get serverURl
	private String ServerURL = "TestURl";

    public static void save(Bundle outBundle) {
        outBundle.putSerializable(BundleString, Instance);
    }

    public static void load(Bundle savedInstanceState) {
        Instance = (SessionData) savedInstanceState.get(BundleString);
    }

	public String getServerURL() {
		return ServerURL;
	}

	public void setServerURL(String serverURL) {
		ServerURL = serverURL;
	}



	public Boolean getAlgorithmHasStarAndEndMarker() {
		return AlgorithmHasStarAndEndMarker;
	}

	public void setAlgorithmHasStarAndEndMarker(
			Boolean algorithmHasStarAndEndMarker) {
		AlgorithmHasStarAndEndMarker = algorithmHasStarAndEndMarker;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getChoosenAlgorithm() {
		return choosenAlgorithm;
	}

	public void setChoosenAlgorithm(String choosenAlgorithm) {
		this.choosenAlgorithm = choosenAlgorithm;
	}

	public void setSelectedNode(Integer selectedNode) {
		this.selectedNode = selectedNode;
	}

	public Integer getSelectedNode() {
		return selectedNode;
	}
}
