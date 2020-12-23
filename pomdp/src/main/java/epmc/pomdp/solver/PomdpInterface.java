package epmc.pomdp.solver;

import java.util.ArrayList;

public interface PomdpInterface {
    
    public int getSizeofStates();
    public int getSizeofActions();
    public int getSizeObservables();
    public ArrayList<Double> getInitBelief();
    public Double transFunction(int sI, int aI, int nexSI);
    public Double obsFunction(int oI, int nexSI, int aI);
    public Double reward(int sI, int aI);

}