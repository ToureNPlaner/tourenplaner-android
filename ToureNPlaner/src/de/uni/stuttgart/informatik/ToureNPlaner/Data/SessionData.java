package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import android.app.Application;

//  this class stores all data from the userinput
public class SessionData extends Application {
	private static Integer selectedNode = 0;
	private static String email;
	private static String password = "";
	private static String choosenAlgorithm;
	private static Boolean AlgorithmHasStarAndEndMarker = true;
	//TODO get serverURl
	private static String ServerURL = "TestURl"; 
	
	
	
	public static String getServerURL() {
		return ServerURL;
	}

	public static void setServerURL(String serverURL) {
		ServerURL = serverURL;
	}



	public static Boolean getAlgorithmHasStarAndEndMarker() {
		return AlgorithmHasStarAndEndMarker;
	}

	public static void setAlgorithmHasStarAndEndMarker(
			Boolean algorithmHasStarAndEndMarker) {
		AlgorithmHasStarAndEndMarker = algorithmHasStarAndEndMarker;
	}

	public static String getEmail() {
		return email;
	}

	public static void setEmail(String email) {
		SessionData.email = email;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		SessionData.password = password;
	}

	public static String getChoosenAlgorithm() {
		return choosenAlgorithm;
	}

	public static void setChoosenAlgorithm(String choosenAlgorithm) {
		SessionData.choosenAlgorithm = choosenAlgorithm;
	}

	public static void setSelectedNode(Integer selectedNode) {
		SessionData.selectedNode = selectedNode;
	}

	public static Integer getSelectedNode() {
		return selectedNode;
	}
}
