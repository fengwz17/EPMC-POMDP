package solver;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.Math;

public class Parser implements PomdpInterface {

    public Parser() {

        ArrayList<String> Actions = new ArrayList<String>();
        Actions.add("turnLeft");
        Actions.add("turnRight");
        Actions.add("goAhead");
        this.Actions = Actions;
        this.actionSize = Actions.size();
  
        ArrayList<Integer> Observations = new ArrayList<Integer>();
        Observations.add(0);
        Observations.add(1);
        Observations.add(2);
        this.Observations = Observations;
        this.obsSize = Observations.size();

        // x1 = {-1,0,1}, y1 = {0,1,2,3}
        // {<(x1, -1),(x2, -1),(y1, 0),(y2, 0)>,
        //  <(x1, -1),(x2, -1),(y1, 0),(y2, 1)>,
        //  ... }
    
        ArrayList<HashMap<String, Integer>> States = new ArrayList<HashMap<String, Integer>>();
       // HashMap<String, Integer> tmpState = new HashMap<String, Integer>();
   
        ArrayList<Integer> x1 = new ArrayList<Integer>();
        // for(int i = -1; i <= 1; ++i) { x1.add(i); }

        ArrayList<Integer> x2 = new ArrayList<Integer>();
        // for(int i = -1; i <= 1; ++i) { x2.add(i); }

        ArrayList<Integer> y1 = new ArrayList<Integer>();
        // for(int i = 0; i <= 3; ++i) { y1.add(i); }

        ArrayList<Integer> y2 = new ArrayList<Integer>();
        // for(int i = 0; i <= 3; ++i) { y2.add(i); }
        for (int i = -1; i <= 1; i++)
        {
            for (int j = -1; j <= 1; j++)
            {
                for (int m = 0; m <= 3; m++)
                {
                    for (int n = 0; n <= 3; n++)
                    {
                        x1.add(i);
                        x2.add(j);
                        y1.add(m);
                        y2.add(n);
                        HashMap<String, Integer> tmpState = new HashMap<String, Integer>();
                        tmpState.put("x1",i);
                        tmpState.put("x2",j);
                        tmpState.put("y1",m);
                        tmpState.put("y2",n);
                        States.add(tmpState);
                    }
                }
            }
        }
        this.States = States;
        this.stateSize = States.size();

        // System.out.println("States: " + States.size()); 
        // for (int i = 0; i < States.size(); i++)
        // {
        //     System.out.println("state: " + States.get(i));
        // }
        
        double b = 1/(States.size());
        for (int i = 0; i < States.size(); i++)
        {   
            b0.add(b);
        }

        ArrayList<ArrayList<ArrayList<Double>>> T = new ArrayList<ArrayList<ArrayList<Double>>>();
        ArrayList<ArrayList<ArrayList<Double>>> O = new ArrayList<ArrayList<ArrayList<Double>>>();
        ArrayList<ArrayList<Double>> R = new ArrayList<ArrayList<Double>>();

        for (int ai = 0; ai < actionSize; ai++)
        {
            ArrayList<Double> tmptmp = new ArrayList<Double>();
            for (int si = 0; si < stateSize; si++)
            {
                HashMap<String, Integer> tmp = new HashMap<String, Integer>();
                tmp = States.get(si);
                
                if (Math.pow(tmp.get("x1") - tmp.get("x2"), 2) + Math.pow(tmp.get("y1") - tmp.get("y2"), 2) < 1 * 1)
                {
                    tmptmp.set(si, -1000000.0);
                }
                else
                {
                    if (tmp.get("x1") == 0 && tmp.get("y1") == 3)
                    {
                        tmptmp.set(si, 1000.0);
                    }
                    else
                    {
                        if (Actions.get(ai) == "goAhead")
                        {
                            tmptmp.set(si, 0.0);
                        }
                        else
                        {
                            tmptmp.set(si, -1.0);
                        }
                    }
                }
                
            }
            R.set(ai, tmptmp);
        }

        this.Rewards = R;

    }

    
    
    @override
    public int getSizeofStates() {
        return this.stateSize;
    };
    
    @override
    public int getSizeofActions() {
        return this.actionSize;
    };

    @override
    public int getSizeObservables() {
        return this.obsSize;
    };

    @override
    public ArrayList<Double> getInitBelief() {
        return this.b0;
    };

    @override
    public Double transFunction(int sI, int aI, int nexSI) {
        return this.Trans.get(aI).get(sI).get(nexSI);
    };
    
    @override
    public Double obsFunction(int oI, int nexSI, int aI) {
        return this.Obs.get(aI).get(nexSI).get(oI);
    };
    
    @override
    public Double reward(int ai, int si) {
        return this.Rewards.get(aI).get(sI);
    };

    private int obsSize;
    private int actionSize;
    private int stateSize;

    private ArrayList<String> Actions;
    private ArrayList<Integer> Observations;
    // private ArrayList<Integer> Variables;
    private ArrayList<HashMap<String, Integer>> States;
    private ArrayList<Double> b0;
    private ArrayList<ArrayList<ArrayList<Double>>> Trans;
    private ArrayList<ArrayList<ArrayList<Double>>> Obs;
    private ArrayList<ArrayList<Double>> Rewards;


}