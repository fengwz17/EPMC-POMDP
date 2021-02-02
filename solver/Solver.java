package solver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;

public class Solver {
    public static void main(String[] args) {
        System.out.println("main");

        long start,end;
        start = System.currentTimeMillis();

        PomdpInterface Pb = new Tiger();
        // System.out.println("R: " + Pb.reward(0,0));
        // Belief b0 = new Belief(Pb.getInitBelief());
        // b0.printBelief();
        
        PbviPlanner PbviPlanner = new PbviPlanner(0.95, 50, 200, 50, Pb);

        System.out.println("...Initial Value Function...");
        for (AlphaVector AlphaVector : PbviPlanner.getValueFunction())
        {
            AlphaVector.printAlphaVector();
        }

        PbviPlanner.plan();

        System.out.println("...Result Value Function...");
        System.out.println("... " + PbviPlanner.getValueFunction().size() + " alpha vectors in Result...");

        for (AlphaVector AlphaVector : PbviPlanner.getValueFunction())
        {
            AlphaVector.printAlphaVector();
        }

        end = System.currentTimeMillis();  
        System.out.println("...End...");
        System.out.println("start time:" + start+ "; end time:" + end+ "; Run Time:" + (end - start) + "(ms)");
        
        Simulator s = new Simulator();
        int initState = 0;
        ArrayList<Double> initStateProb = new ArrayList<Double>(s.sampleInitBelief(initState));
        Belief iniBelief = new Belief(initStateProb);
        s.simulate(iniBelief, PbviPlanner.getValueFunction(), 20);
    }

}
