package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import java.io.Serializable;

//  this class stores all data from the userinput
public class SessionData {
    private static class Data implements Serializable {
        public Integer selectedNode = 0;
        public String email;
        public String password = "";
    }

    public static SessionData Instance;

    private Data data = new Data();

    static public void createInstance() {
        Instance = new SessionData();
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

	public void setSelectedNode(Integer selectedNode) {
		data.selectedNode = selectedNode;
	}

	public Integer getSelectedNode() {
		return data.selectedNode;
	}
}
