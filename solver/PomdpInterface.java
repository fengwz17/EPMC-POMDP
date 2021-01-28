package solver;

import java.util.ArrayList;

public interface PomdpInterface {
    
    public int getSizeofStates();
    public int getSizeofActions();
    public int getSizeObservables();
    public ArrayList<Double> getInitBelief();
    public Double transFunction(int aI, int sI, int nexSI);
    public Double obsFunction(int aI, int nexSI, int oI);
    public Double reward(int ai, int si);

}