// tiger problem

package solver;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Tiger implements PomdpInterface {

    public Tiger() {

        ArrayList<String> Actions = new ArrayList<String>();
        Actions.add("Left");
        Actions.add("Right");
        Actions.add("Listen");
        this.Actions = Actions;
        this.actionSize = Actions.size();
        // System.out.println("actionSize: " + this.actionSize);
  
        ArrayList<Integer> Observations = new ArrayList<Integer>();
        Observations.add(0);
        Observations.add(1);
        this.Observations = Observations;
        this.obsSize = Observations.size();

        // tiger in left and right
        ArrayList<Integer> States = new ArrayList<Integer>();
        for (int i = 0; i < 2; i++)
        {
            States.add(i);
        }
        this.States = States;
        this.stateSize = States.size();

        // System.out.println("States: " + States.size()); 
        // for (int i = 0; i < States.size(); i++)
        // {
        //     System.out.println("state: " + States.get(i));
        // }
        
        double b = 1.0/(States.size());
        ArrayList<Double> b0 = new ArrayList<Double>();
        for (int i = 0; i < States.size(); i++)
        {   
            b0.add(b);
        }
        this.belief = b0;

        ArrayList<ArrayList<ArrayList<Double>>> T = new ArrayList<ArrayList<ArrayList<Double>>>();
        ArrayList<ArrayList<ArrayList<Double>>> O = new ArrayList<ArrayList<ArrayList<Double>>>();
        ArrayList<ArrayList<Double>> R = new ArrayList<ArrayList<Double>>();
        

        // reward function
        // a = listen r = -1
        // a = left s = left -100
        // a = right s = right -100
        // else 10
        for (int ai = 0; ai < actionSize; ai++)
        {
            ArrayList<Double> tmptmp = new ArrayList<Double>();
            for (int si = 0; si < stateSize; si++)
            {
                if (ai == 2)
                {
                    tmptmp.add(-1.0);
                }
                else if (ai == 0 || ai == 1)
                {
                    if (si == 0 && ai == 0)
                    {
                        tmptmp.add(-100.0);
                    }
                    else if (si == 1 && ai == 1)
                    {
                        tmptmp.add(-100.0);
                    }
                    else
                    {
                        tmptmp.add(10.0);
                    }
                }
            }
            R.add(tmptmp);
        }

        this.Rewards = R;

        // observation function
        // P(o|a,s')
        // P(o|left or right, s')=0.5
        // P(o=tLeft|a=listen, s'=left)=P(o=rRight|a=listen, s'=right)=0.85
        for (int ai = 0; ai < actionSize; ai++)
        {
            ArrayList<ArrayList<Double>> tmptmptmp = new ArrayList<ArrayList<Double>>(); 
            for (int nexSi = 0; nexSi < stateSize; nexSi++)
            {
                ArrayList<Double> tmptmp = new ArrayList<Double>(); 
                for (int oi = 0; oi < obsSize; oi++)
                {
                    if (ai == 0 || ai == 1)
                    {
                        tmptmp.add(0.5);
                    }   
                    else if (ai == 2)
                    {
                        if (this.States.get(nexSi) == 0 && this.Observations.get(oi) == 0)
                        {
                            tmptmp.add(0.85);
                        }
                        else if (this.States.get(nexSi) == 1 && this.Observations.get(oi) == 1)
                        {
                            tmptmp.add(0.85);
                        }
                        else
                        {
                            tmptmp.add(0.15);
                        }
                    }
                   
                }
                tmptmptmp.add(tmptmp);
            }
            O.add(tmptmptmp);
        }
        this.Obs = O;

        // print observation
        // System.out.println("...Observation...: " + this.Obs.size());
        // for (int si = 0; si < stateSize; si++)
        // {
        //     System.out.println("x1: " + this.States.get(si).get("x1") + " y1: " + this.States.get(si).get("y1") 
        //             + " x2: " + this.States.get(si).get("x2") + " y2: " + this.States.get(si).get("y2")); 
        //     for (int oi = 0; oi < obsSize; oi++)
        //     {
        //         System.out.println("o: " + this.Obs.get(0).get(si).get(oi));
        //     }
        // }
    
        // transition function
        // P(s'|a, s)
        // P(s'|listen, s)=1 if s'=s
        // P(s'|a=left, s)=P(s'|a=right, s)=0.5
        for (int ai = 0; ai < actionSize; ai++)
        {
            ArrayList<ArrayList<Double>> tmptmptmp = new ArrayList<ArrayList<Double>>(); 
            for (int si = 0; si < stateSize; si++)
            {
                ArrayList<Double> tmptmp = new ArrayList<Double>(); 
                
                for (int nexSi = 0; nexSi < stateSize; nexSi++)
                {   
                    if (Actions.get(ai) == "Left" || Actions.get(ai) == "Right")
                    {
                        tmptmp.add(0.5);
                    }
                    else if (Actions.get(ai) == "Listen")
                    {
                        if (States.get(nexSi) == 0 && States.get(si) == 0)
                        {
                            tmptmp.add(1.0);
                        }
                        else if (States.get(nexSi) == 1 && States.get(si) == 1)
                        {
                            tmptmp.add(1.0);
                        }
                        else
                        {
                            tmptmp.add(0.0);
                        }
                    }
                    // System.out.println(tmptmp);
                }       
                tmptmptmp.add(tmptmp);
            }
            T.add(tmptmptmp);
        }
        this.Trans = T;

        // // print transfunction
        // for (int ai = 0; ai < 1; ai++)
        // {
        //     System.out.println("a: " + Actions.get(ai));
        //     for (int si = 0; si < stateSize; si++)
        //     {
        //         System.out.println("x1: " + States.get(si).get("x1") + " y1: " + States.get(si).get("y1") + " x2: " + States.get(si).get("x2") + " y2: " + States.get(si).get("y2")); 
        //         for (int nexsi = 0; nexsi < stateSize; nexsi++)
        //         {
        //             System.out.println("nx1: " + States.get(nexsi).get("x1") + " ny1: " + States.get(nexsi).get("y1") + " nx2: " + States.get(nexsi).get("x2") + " ny2: " + States.get(nexsi).get("y2"));
        //             System.out.println(this.Trans.get(ai).get(si).get(nexsi));
        //         }
        //     }
        // }
    }

    
    @Override
    public int getSizeofStates() {
        return this.stateSize;
    };
    
    @Override
    public int getSizeofActions() {
        return this.actionSize;
    };

    @Override
    public int getSizeObservables() {
        return this.obsSize;
    };

    @Override
    public ArrayList<Double> getInitBelief() {
        return this.belief;
    };

    @Override
    public Double transFunction(int ai, int si, int nexSi) {
        return this.Trans.get(ai).get(si).get(nexSi);
    };
    
    @Override
    public Double obsFunction(int ai, int nexSi, int oi) {
        return this.Obs.get(ai).get(nexSi).get(oi);
    };
    
    @Override
    public Double reward(int ai, int si) {
        return this.Rewards.get(ai).get(si);
    };

    @Override
    public ArrayList<String> getAction() {
        return this.Actions;
    }

    @Override
    public ArrayList<Integer> getObservations() {
        return this.Observations;
    }

    @Override
    public ArrayList<HashMap<String, Integer>> getStates() {
        ArrayList<HashMap<String, Integer>> fakeState = new ArrayList<HashMap<String, Integer>>();
        return fakeState;
    }

    private int obsSize;
    private int actionSize;
    private int stateSize;

    private ArrayList<String> Actions;
    private ArrayList<Integer> Observations;
    // private ArrayList<Integer> Variables;
    // private ArrayList<HashMap<String, Integer>> States;
    private ArrayList<Integer> States;
    private ArrayList<Double> belief;
    private ArrayList<ArrayList<ArrayList<Double>>> Trans;
    private ArrayList<ArrayList<ArrayList<Double>>> Obs;
    private ArrayList<ArrayList<Double>> Rewards;


}