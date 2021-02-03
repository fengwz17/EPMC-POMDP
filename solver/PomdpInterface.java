package solver;

import java.util.ArrayList;
import java.util.HashMap;

public interface PomdpInterface {
    
    public int getSizeofStates();
    public int getSizeofActions();
    public int getSizeObservables();
    public ArrayList<Double> getInitBelief();
    public Double transFunction(int aI, int sI, int nexSI);
    public Double obsFunction(int aI, int nexSI, int oI);
    public Double reward(int ai, int si);

    public ArrayList<String> getAction();
    public ArrayList<Integer> getObservations();
    public ArrayList<HashMap<String, Integer>> getStates(); 
}