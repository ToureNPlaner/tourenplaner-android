package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import android.app.Application;

//  this class stores all data from the userinput
public class UserInput extends Application{
	private static Integer selectedNode = 0 ;	    
	private static String email ;
	private static  String password = "standart";
	private static String choosenAlgorithm ;
	
	public static String getEmail() {
		return email;
	}

	public static void setEmail(String email) {
		UserInput.email = email;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		UserInput.password = password;
	}

	public static String getChoosenAlgorithm() {
		return choosenAlgorithm;
	}

	public static void setChoosenAlgorithm(String choosenAlgorithm) {
		UserInput.choosenAlgorithm = choosenAlgorithm;
	}

	public static void setSelectedNode(Integer selectedNode) {
		UserInput.selectedNode = selectedNode;
	}

	public static Integer getSelectedNode() {
		return selectedNode;
	}
}
