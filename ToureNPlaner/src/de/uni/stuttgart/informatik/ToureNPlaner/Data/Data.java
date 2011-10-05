package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import java.io.Serializable;

class Data implements Serializable {
    public Integer selectedNode = 0;
    public String email;
    public String password = "";
    public String choosenAlgorithm;
    public Boolean AlgorithmHasStarAndEndMarker = true;
    //TODO get serverURl
    public String ServerURL = "TestURl";
}
