package solver;
import java.util.ArrayList;
import java.util.Random;

public class Simulator {
    PomdpInterface Pb = new Model2x3();
    public ArrayList<Double> sampleInitBelief(int initState) {
        ArrayList<Double> prob_states = new ArrayList<Double>();
        for (int i = 0; i < Pb.getSizeofStates(); i++)
        {
            if (i == initState)
            {
                prob_states.add(1.0);
            }
            else
            {
                prob_states.add(0.0);
            }
        }
        return prob_states;
    }

    public int getAction(Belief currB, ArrayList<AlphaVector> alphaVecs) {
        double max = - Double.MAX_VALUE;
        int optActionIndex = 0;
       
        for (AlphaVector alpha : alphaVecs)
        {
            double tmp = 0;
            if (currB.getSize() != alpha.getSize())
            {
                System.out.println("Wrong belief size and alpha size" + " currB size: " + currB.getSize() + " alpha size: " + alpha.getSize());
            }
            for (int i = 0; i < currB.getSize(); i++)
            {
                tmp += currB.get(i) * alpha.getIndex(i); 
            }
            if (tmp > max)
            {
                max = tmp;
                optActionIndex = alpha.getActionIndex();
            }
            // System.out.println("max: " + max);
        }
        return optActionIndex;
    }

    public double probBeleif(int oI, Belief b, int aI, PomdpInterface Pb) {
        double probOBA = 0;
        for (int newSI = 0; newSI < Pb.getSizeofStates(); newSI++)
        {
            double probNewSI = 0;

            // b(s') = Sigma_s T * O * b(s)
            for (int sI = 0; sI < Pb.getSizeofStates(); sI++)
            {
                // T * O * b(s)
                probNewSI += Pb.transFunction(aI, sI, newSI) * Pb.obsFunction(aI, newSI, oI) * b.get(sI);
            }
            probOBA += probNewSI;
        }
        // if (Math.abs(probOBA - 0.0) < 0.0001)
        // {
        //     System.out.println("oI: " + oI + " aI: " + aI);
        //     for (int nexSI = 0; nexSI < Pb.getSizeofStates(); nexSI++)
        //     {
        //         for (int s = 0; s < Pb.getSizeofStates(); s++)
        //         {
        //             double x = Pb.transFunction(aI, s, nexSI);
        //             double y = Pb.obsFunction(aI, nexSI, oI);
        //             double z = b.get(s);
        //             if (Math.abs(x - 0.0) < 0.000000001 || Math.abs(y - 0.0) < 0.000000001 || Math.abs(z - 0.0) < 0.000000001)
        //             {
        //                 System.out.println("nexSI: " + nexSI + " s: " + s + " " +  x + " " + y + " " + z );
        //             }
                    
        //         }
        //     }
        // }
        return probOBA;
    }
    
    
    public Belief updateBelief(int oI, int aI, Belief b, PomdpInterface Pb) {
        ArrayList<Double> bAindexValue = new ArrayList<Double>();
        double probOBA = probBeleif(oI, b, aI, Pb);
        if(probOBA > 0)
        {
            for (int newSI = 0; newSI < Pb.getSizeofStates(); newSI++)
            {
                double probNewSI = 0;
                for (int sI = 0; sI < Pb.getSizeofStates(); sI++)
                {
                    probNewSI += Pb.transFunction(aI, sI, newSI) * b.get(sI);
                }
                double probNewSAO = (Pb.obsFunction(aI, newSI, oI) * probNewSI) / probOBA;
                bAindexValue.add(probNewSAO);
            }
        }
        // else
        // {
        //     System.out.println("err");
        //     System.out.println("probOBA: " + probOBA);
        //     System.exit(0);
        // }
        Belief bAO = new Belief(bAindexValue);
        return bAO;
    }

    
    public void simulate(Belief initBelief, ArrayList<AlphaVector> alphaVecs, int stepNum) {
        PomdpInterface Pb = new Model2x3();
        int currState = 0;
        double transProb = 0.0;
        double obsProb = 0.0;
        
      
        ArrayList<Double> currBeliefState = new ArrayList<Double>(initBelief.getBelief());
        Belief currBelief = new Belief(currBeliefState);

        for (int stateIndex = 0; stateIndex < currBeliefState.size(); stateIndex++)
        {
            // just choose state that has 1.0 prob
            // should use sample instead
            if (Math.abs(currBeliefState.get(stateIndex) - 1.0) < 0.00001)
            {
                currState = stateIndex;
            }
        }
        double reward = 0.0;
        
        for (int step = 0; step < stepNum; step++)
        {
            // for (AlphaVector alpha : alphaVecs)
            // {
            //     alpha.printAlphaVector();            
            // }
            // currBelief.printBelief();
            int currAction = 0;
            currAction = getAction(currBelief, alphaVecs);
            reward += Pb.reward(currAction, currState);
            String out;
            out = "step: " + step + " action: " + Pb.getAction().get(currAction) + " currState: " + currState ;
            out += " currPosition: " + Pb.getStates().get(currState).get("x1") + " " + Pb.getStates().get(currState).get("y1");
            out += " " + Pb.getStates().get(currState).get("x2") + " " + Pb.getStates().get(currState).get("y2");
            out += " reward: " + reward;
            Random r = new Random();
            int Statechoice = r.nextInt(10000);
            int currentSum = 0;
            int selectNexState = 0;
            for (int nexSi = 0; nexSi < Pb.getSizeofStates(); nexSi++)
            {
                transProb = Pb.transFunction(currAction, currState, nexSi);
                int nextCurrentSum = currentSum + (int)(transProb*10000);
                if(Statechoice >= currentSum && Statechoice < nextCurrentSum)
                {
                    selectNexState = nexSi;
                    break;
                }
                currentSum = nextCurrentSum;
            }  
            out += " nexState: " + selectNexState;
            out += " next position: " + Pb.getStates().get(selectNexState).get("x1") + " " + Pb.getStates().get(selectNexState).get("y1");
            out += " " + Pb.getStates().get(selectNexState).get("x2") + " " + Pb.getStates().get(selectNexState).get("y2");
            currState = selectNexState;
            int Obschoice = r.nextInt(10000);
            int currentObsSum = 0;
            int selectObs = 0;

            for (int o = 0; o < Pb.getSizeObservables(); o++)
            {
                obsProb = Pb.obsFunction(currAction, selectNexState, o);  
                int nextCurrObsSum = currentObsSum + (int)(obsProb*10000);
                if(Obschoice >= currentObsSum && Obschoice < nextCurrObsSum)
                {
                    selectObs = o;
                    break;
                }
                currentObsSum = nextCurrObsSum;
            }  
            out += " obs: " + selectObs; 
            currBelief = updateBelief(selectObs, currAction, currBelief, Pb);
            System.out.println(out);
        }
    }

}