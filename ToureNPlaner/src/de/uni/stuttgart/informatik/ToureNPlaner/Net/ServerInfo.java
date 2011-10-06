package de.uni.stuttgart.informatik.ToureNPlaner.Net;

import java.io.Serializable;
import java.util.ArrayList;

public class ServerInfo implements Serializable {
    public static enum ServerType {
        PUBLIC,
        PRIVATE
    }
    private String version;
    private ServerType serverType;
    private int sslport;
    ArrayList<AlgorithmInfo> algorithms;
}
