// condering the simplest model
// (0,1) (1,1)
// (0,0) (1,0)
// x \in {0,1} y \in {0,1}
// action \in {goAhead, turnRight, hover} 
// intruder's action \in {left, down}
// observation \in {no-detection, front, right, crash}
// target: (1,1)

package solver;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Model2x2 implements PomdpInterface {

    public Model2x2() {

        ArrayList<String> Actions = new ArrayList<String>();
        Actions.add("goAhead");
        Actions.add("turnRight");
        Actions.add("hover");
        this.Actions = Actions;
        this.actionSize = Actions.size();
        // System.out.println("actionSize: " + this.actionSize);
  
        ArrayList<Integer> Observations = new ArrayList<Integer>();
        Observations.add(0);
        Observations.add(1);
        Observations.add(2);
        Observations.add(3);
        this.Observations = Observations;
        this.obsSize = Observations.size();

        // x1 = {0,1}, y1 = {0,1}
        // {<(x1, 0),(x2, 0),(y1, 0),(y2, 0)>,
        //  <(x1, 0),(x2, 0),(y1, 0),(y2, 1)>,
        //  ... }
    
        ArrayList<HashMap<String, Integer>> States = new ArrayList<HashMap<String, Integer>>();
       // HashMap<String, Integer> tmpState = new HashMap<String, Integer>();
   
        ArrayList<Integer> x1 = new ArrayList<Integer>();
        // for(int i = 0; i <= 1; ++i) { x1.add(i); }

        ArrayList<Integer> x2 = new ArrayList<Integer>();
        // for(int i = 0; i <= 1; ++i) { x2.add(i); }

        ArrayList<Integer> y1 = new ArrayList<Integer>();
        // for(int i = 0; i <= 1; ++i) { y1.add(i); }

        ArrayList<Integer> y2 = new ArrayList<Integer>();
        // for(int i = 0; i <= 1; ++i) { y2.add(i); }
        for (int i = 0; i <= 1; i++)
        {
            for (int j = 0; j <= 1; j++)
            {
                for (int m = 0; m <= 1; m++)
                {
                    for (int n = 0; n <= 1; n++)
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
        for (int ai = 0; ai < actionSize; ai++)
        {
            ArrayList<Double> tmptmp = new ArrayList<Double>();
            for (int si = 0; si < stateSize; si++)
            {
                HashMap<String, Integer> tmp = new HashMap<String, Integer>();
                tmp = States.get(si);
                // System.out.println("tmp: " + tmp);
                // System.out.println("x1: " + tmp.get("x1"));
                // System.out.println("x2: " + tmp.get("x2"));
                // System.out.println("y1: " + tmp.get("y1"));
                // System.out.println("y2: " + tmp.get("y2"));
                // System.out.println("x dis: " + Math.pow(tmp.get("x1") - tmp.get("x2"), 2) + " y dis: " + Math.pow(tmp.get("y1") - tmp.get("y2"), 2));
                
                if (Math.pow(tmp.get("x1") - tmp.get("x2"), 2) + Math.pow(tmp.get("y1") - tmp.get("y2"), 2) < 1 * 1)
                {
                    // System.out.println("x1: " + tmp.get("x1") + " y1: " + tmp.get("y1") + " x2: " + tmp.get("x2") + " y2: " + tmp.get("y2"));
                    tmptmp.add(-10000.0);
                }
                else
                {
                    if (tmp.get("x1") == 1 && tmp.get("y1") == 1)
                    {
                        tmptmp.add(1000.0);
                    }
                    else
                    {
                        tmptmp.add(0.0);
                    }
                }
                
            }
            R.add(tmptmp);
        }

        this.Rewards = R;

        // observation function
        for (int ai = 0; ai < actionSize; ai++)
        {
            ArrayList<ArrayList<Double>> tmptmptmp = new ArrayList<ArrayList<Double>>(); 
            for (int nexSi = 0; nexSi < stateSize; nexSi++)
            {
                HashMap<String, Integer> tmp = new HashMap<String, Integer>();
                tmp = States.get(nexSi);
                int x = tmp.get("x1");
                int xx = tmp.get("x2");
                int y = tmp.get("y1");
                int yy = tmp.get("y2");
                ArrayList<Double> tmptmp = new ArrayList<Double>(); 
                for (int oi = 0; oi < obsSize; oi++)
                {   
                    // crash
                    if (Math.pow((x - xx), 2) + Math.pow((y - yy), 2) < 1 * 1)
                    {
                        // System.out.println("crash:  x1: " + x + " y1: " + y + " x2: " + xx + " y2: " + yy);
                        if (Observations.get(oi) == 3)
                        {
                            tmptmp.add(0.8);
                        }
                        else
                        {
                            tmptmp.add(0.1);
                        }
                    }

                    // detect
                    else if (Math.pow((x - xx), 2) + Math.pow((y - yy), 2) == 1 * 1)
                    {    
                        // front
                        if ((xx == x) && (yy > y))
                        {
                            if (Observations.get(oi) == 1)
                            {
                                tmptmp.add(0.8);
                            }
                            else if (Observations.get(oi) == 2)
                            {
                                tmptmp.add(0.1);
                            }
                            else
                            {
                                tmptmp.add(0.1);
                            }
                        }

                        // right
                        else if ((xx > x) &&  (yy == y))
                        {
                            if (Observations.get(oi) == 2)
                            {
                                tmptmp.add(0.8);
                            }
                            else if (Observations.get(oi) == 1)
                            {
                                tmptmp.add(0.1);
                            }
                            else
                            {
                                tmptmp.add(0.1);
                            }
                        }
                        
                        else
                        {
                            if (Observations.get(oi) == 0)
                            {
                                tmptmp.add(0.8);
                            }
                            else
                            {
                                tmptmp.add(0.1);
                            }
                        }
                    }

                    // no-detection
                    else
                    {
                        if (Observations.get(oi) == 0)
                        {
                            tmptmp.add(0.8);
                        }
                        else if (Observations.get(oi) == 1)
                        {
                            tmptmp.add(0.1);
                        }
                        else
                        {
                            tmptmp.add(0.1);
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
        for (int ai = 0; ai < actionSize; ai++)
        {
            ArrayList<ArrayList<Double>> tmptmptmp = new ArrayList<ArrayList<Double>>(); 
            for (int si = 0; si < stateSize; si++)
            {
                // HashMap<String, Integer> tmp = new HashMap<String, Integer>();
                // tmp = States.get(si);
                int x = this.States.get(si).get("x1");
                int xx = this.States.get(si).get("x2");
                int y = this.States.get(si).get("y1");
                int yy = this.States.get(si).get("y2");
                ArrayList<Double> tmptmp = new ArrayList<Double>(); 
                // System.out.println("nx1: " + x + " y1: " + y 
                //       + " x2: " + xx + " y2: " + yy); 
                for (int nexSi = 0; nexSi < stateSize; nexSi++)
                {   
                    // HashMap<String, Integer> nexTmp = new HashMap<String, Integer>();
                    // nexTmp = States.get(nexSi);
                    int nx = this.States.get(nexSi).get("x1");
                    int nxx = this.States.get(nexSi).get("x2");
                    int ny = this.States.get(nexSi).get("y1");
                    int nyy = this.States.get(nexSi).get("y2");
                    // System.out.println("nx1: " + nx + " ny1: " + ny 
                    //   + " nx2: " + nxx + " ny2: " + nyy); 
                    if (Actions.get(ai) == "goAhead")
                    {
                        if (y == 0)
                        {
                            if ((nx == x) && (ny == y + 1))
                            {
                                if ((xx == 0) && (yy == 0))
                                {
                                    if ((nxx == xx) && (nyy == yy))
                                    {
                                        tmptmp.add(1.0);
                                    }
                                    else
                                    {
                                        tmptmp.add(0.0);
                                    }
                                    
                                }
                                else if ((xx == 0) && (yy == 1))
                                {
                                    if ((nxx == xx) && (nyy == yy - 1))
                                    {
                                        tmptmp.add(1.0);
                                    }
                                    else 
                                    {
                                        tmptmp.add(0.0);
                                    }
                                }
                                else if ((xx == 1) && (yy == 0))
                                {
                                    if ((nxx == xx - 1) && (nyy == yy))
                                    {
                                        tmptmp.add(1.0);
                                    }
                                    else
                                    {
                                        tmptmp.add(0.0);
                                    }
                                }
                                else if ((xx == 1) && (yy == 1))
                                {
                                    if ((nxx == xx - 1) && (nyy == yy))
                                    {
                                        tmptmp.add(0.5);
                                    }
                                    else if ((nxx == xx) && (nyy == yy - 1))
                                    {
                                        tmptmp.add(0.5);
                                    }
                                    else
                                    {
                                        tmptmp.add(0.0);
                                    }
                                }
                                else
                                {
                                    tmptmp.add(0.0);
                                }
                            }
                            else
                            {
                                tmptmp.add(0.0);
                            }
                            
                        }
                        else
                        {
                            tmptmp.add(0.0);
                        }
                    }
                    if (Actions.get(ai) == "turnRight")
                    {
                        if (x == 0)
                        {
                            if ((nx == x + 1) && (ny == y))
                            {
                                if ((xx == 0) && (yy == 0))
                                {
                                    if ((nxx == xx) && (nyy == yy))
                                    {
                                        tmptmp.add(1.0);
                                    }
                                    else
                                    {
                                        tmptmp.add(0.0);
                                    }
                                    
                                }
                                else if ((xx == 0) && (yy == 1))
                                {
                                    if ((nxx == xx) && (nyy == yy - 1))
                                    {
                                        tmptmp.add(1.0);
                                    }
                                    else 
                                    {
                                        tmptmp.add(0.0);
                                    }
                                }
                                else if ((xx == 1) && (yy == 0))
                                {
                                    if ((nxx == xx - 1) && (nyy == yy))
                                    {
                                        tmptmp.add(1.0);
                                    }
                                    else
                                    {
                                        tmptmp.add(0.0);
                                    }
                                }
                                else if ((xx == 1) && (yy == 1))
                                {
                                    if ((nxx == xx - 1) && (nyy == yy))
                                    {
                                        tmptmp.add(0.5);
                                    }
                                    else if ((nxx == xx) && (nyy == yy - 1))
                                    {
                                        tmptmp.add(0.5);
                                    }
                                    else
                                    {
                                        tmptmp.add(0.0);
                                    }
                                }
                                else
                                {
                                    tmptmp.add(0.0);
                                }
                            }
                            else
                            {
                                tmptmp.add(0.0);
                            }
                            
                        }
                        else
                        {
                            tmptmp.add(0.0);
                        }
                    }
                    if (Actions.get(ai) == "hover")
                    {
                        if ((nx == x) && (ny == y))
                        {
                            if ((xx == 0) && (yy == 0))
                            {
                                if ((nxx == xx) && (nyy == yy))
                                {
                                    tmptmp.add(1.0);
                                }
                                else
                                {
                                    tmptmp.add(0.0);
                                }
                                
                            }
                            else if ((xx == 0) && (yy == 1))
                            {
                                if ((nxx == xx) && (nyy == yy - 1))
                                {
                                    tmptmp.add(1.0);
                                }
                                else 
                                {
                                    tmptmp.add(0.0);
                                }
                            }
                            else if ((xx == 1) && (yy == 0))
                            {
                                if ((nxx == xx - 1) && (nyy == yy))
                                {
                                    tmptmp.add(1.0);
                                }
                                else
                                {
                                    tmptmp.add(0.0);
                                }
                            }
                            else if ((xx == 1) && (yy == 1))
                            {
                                if ((nxx == xx - 1) && (nyy == yy))
                                {
                                    tmptmp.add(0.5);
                                }
                                else if ((nxx == xx) && (nyy == yy - 1))
                                {
                                    tmptmp.add(0.5);
                                }
                                else
                                {
                                    tmptmp.add(0.0);
                                }
                            }
                            else
                            {
                                tmptmp.add(0.0);
                            }
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

    private int obsSize;
    private int actionSize;
    private int stateSize;

    private ArrayList<String> Actions;
    private ArrayList<Integer> Observations;
    // private ArrayList<Integer> Variables;
    private ArrayList<HashMap<String, Integer>> States;
    private ArrayList<Double> belief;
    private ArrayList<ArrayList<ArrayList<Double>>> Trans;
    private ArrayList<ArrayList<ArrayList<Double>>> Obs;
    private ArrayList<ArrayList<Double>> Rewards;


}