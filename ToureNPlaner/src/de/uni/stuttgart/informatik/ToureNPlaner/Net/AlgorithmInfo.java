package de.uni.stuttgart.informatik.ToureNPlaner.Net;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraint;

import java.io.Serializable;
import java.util.ArrayList;

public class AlgorithmInfo implements Serializable {
    private String version;
    private String name;
    private String urlsuffix;
    private ArrayList<Constraint> point_constraints;
    private ArrayList<Constraint> constraints;
    private int minPoints;
    private boolean sourceIsTarget;
}
