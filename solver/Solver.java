package solver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;

public class Solver {
    public static void main(String[] args) {
        System.out.println("main");

        long start,end;
        start = System.currentTimeMillis();

        PomdpInterface Pb = new Model2x3();
        // System.out.println("R: " + Pb.reward(0,0));
        // Belief b0 = new Belief(Pb.getInitBelief());
        // b0.printBelief();
        
        PbviPlanner PbviPlanner = new PbviPlanner(0.95, 300, 13, 100, Pb);

        System.out.println("...Initial Value Function...");
        // for (AlphaVector AlphaVector : PbviPlanner.getValueFunction())
        // {
        //     AlphaVector.printAlphaVector();
        // }

        PbviPlanner.plan();

        System.out.println("...Result Value Function...");
        System.out.println("... " + PbviPlanner.getValueFunction().size() + " alpha vectors in Result...");

        // for (AlphaVector AlphaVector : PbviPlanner.getValueFunction())
        // {
        //     AlphaVector.printAlphaVector();
        // }

        end = System.currentTimeMillis();  
        System.out.println("...End...");
        System.out.println("start time:" + start+ "; end time:" + end + "; Run Time:" + (end - start) + "(ms)");
        
        for (int i = 0; i < 10; i++)
        {
            Simulator s = new Simulator();
            int initState = 0;
            initState = stateNum(0,0,2,3,Pb);
            System.out.println("...Simulate...");
            System.out.println("init state: " + initState);
            ArrayList<Double> initStateProb = new ArrayList<Double>(s.sampleInitBelief(initState));
            Belief iniBelief = new Belief(initStateProb);
            s.simulate(iniBelief, PbviPlanner.getValueFunction(), 10);

        }
        
    }


    static public int stateNum(int x1, int y1, int x2, int y2, PomdpInterface Pb) {
        int retI = 0;
        for (int i = 0; i < Pb.getStates().size(); i++)
        {
            int tx1 = Pb.getStates().get(i).get("x1");
            int ty1 = Pb.getStates().get(i).get("y1");
            int tx2 = Pb.getStates().get(i).get("x2");
            int ty2 = Pb.getStates().get(i).get("y2");
            if (tx1 == x1 && tx2 == x2 && ty1 == y1 && ty2 == y2)
            {
                retI = i;
            }
        }
        return retI;
    }

}
