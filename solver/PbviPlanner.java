package solver;

import java.util.ArrayList;

import java.lang.Throwable;
import java.lang.reflect.Array;

public class PbviPlanner {
    private ArrayList<Belief> beliefSet;
    private ArrayList<AlphaVector> alphaVectors;
    private ArrayList<ArrayList<Double>> rewardVectors;
    private Double gamma;
    // private Double err = Double.MIN_VALUE;
    private Double err = 1e-6;
    private int MAX_BP; // max Belief set size
    private int MAX_ITER_GLOBAL; // max iteration number in PBVI main alogrithm
    private int MAX_ITER_IMP; // max iteration number during improving process
    private double MIN_B_ACC = 1e-3;
    PomdpInterface Pb;


    public AlphaVector updateAlphaAO(int aI, AlphaVector alpha, int oI, PomdpInterface Pb) {
        ArrayList<Double> tmp = new ArrayList<Double>();
        AlphaVector tmpAlpha = new AlphaVector();
        tmpAlpha.setActionIndex(alpha.getActionIndex());
        for (int i = 0; i < alpha.getValues().size(); i++)
        {
            tmpAlpha.addValue(alpha.getValues().get(i));
        } 
        for (int sI = 0; sI < alpha.getSize(); sI++)
        {
            double probState = 0;
            for (int nSI = 0; nSI < alpha.getSize(); nSI++)
            {
                // System.out.println(alpha.getIndex(nSI) + " " + Pb.obsFunction(aI, nSI, oI) + " " + Pb.transFunction(aI, sI, nSI));
                probState += alpha.getIndex(nSI) * Pb.obsFunction(aI, nSI, oI) * Pb.transFunction(aI, sI, nSI);
                // System.out.println("prob: " + probState);
            }
            tmp.add(probState);
            // System.out.println("tmp: " + tmp.get(sI));
        }

        for (int sI = 0; sI < alpha.getSize(); sI++)
        {
            // System.out.println("tmp: " + tmp.get(sI));
            tmpAlpha.changeValue(sI, tmp.get(sI));
        }

        return tmpAlpha;
    }

    // compute all the alpha_a_o values and store them to use in different belief point
    public ArrayList<ArrayList<ArrayList<AlphaVector>>> computeAllAlphaAOValues(ArrayList<AlphaVector> alphaVectors, PomdpInterface Pb){
        ArrayList<ArrayList<ArrayList<AlphaVector>>> AlphaAOVecs = new ArrayList<ArrayList<ArrayList<AlphaVector>>>();
        // System.out.println("........");
        // alphaVectors.get(0).printAlphaVector();
        // System.out.println("........");
        for (int aI = 0; aI < Pb.getSizeofActions(); aI++)
        {
            ArrayList<ArrayList<AlphaVector>> tmptmp = new ArrayList<ArrayList<AlphaVector>>();
            for (int oI = 0; oI < Pb.getSizeObservables(); oI++)
            {
                ArrayList<AlphaVector> tmp = new ArrayList<AlphaVector>(alphaVectors);
                tmptmp.add(tmp);
            }
            AlphaAOVecs.add(tmptmp);
        }

        // System.out.println("<<<<<<<<");
        // alphaVectors.get(0).printAlphaVector();
        // System.out.println(">>>>>>>>");

        for (int aI = 0; aI < Pb.getSizeofActions(); aI++)
        {
            // System.out.println("compute: " + alphaVectors.size());
            for (int alphaIndex = 0; alphaIndex < alphaVectors.size(); alphaIndex++)
            {
                for (int oI = 0; oI < Pb.getSizeObservables(); oI++)
                {
                    AlphaVector tmpAlpha = updateAlphaAO(aI, alphaVectors.get(alphaIndex), oI, Pb);
                    // System.out.println("+++++++");
                    // alphaVectors.get(0).printAlphaVector();
                    // System.out.println("+++++++");
                    // tmpAlpha.printAlphaVector();
                    AlphaAOVecs.get(aI).get(oI).set(alphaIndex, tmpAlpha);
                }
            }   
        }
        return AlphaAOVecs;
    }

    public double dot(Belief b, AlphaVector alpha) {
        // check the dimension equality
        if (b.getSize() != alpha.getSize())
        {
            System.err.println("b_size: " + b.getSize() + " and alpha_size: " + alpha.getSize() + " error dimension not equal.\n");
            System.exit(0);
           //  throw new Exception("Dimension error!");
        }
        double result = 0;
        for (int i = 0; i < b.getSize(); i++)
        {
            result += b.get(i) * alpha.getIndex(i);
        }
        return result;
    }

    public AlphaVector updateAlphaAB(int aI, ArrayList<ArrayList<ArrayList<AlphaVector>>> AlphaAOVecs, Belief b, 
        ArrayList<ArrayList<Double>> rewardVectors, double gamma, PomdpInterface Pb) {
            // reward vector for an action a 
            ArrayList<Double> rewards = rewardVectors.get(aI);
            ArrayList<Double> values = new ArrayList<Double>();
            for (int sI = 0; sI < Pb.getSizeofStates(); sI++)
            {
                values.add(0.0);
            }
            
            // find different alpha vectors(different aI, the same oI), which can have max (b * alphaAO)
            for (int oI = 0; oI < Pb.getSizeObservables(); oI++)
            {
                // update alpha vector according to oI and find argmax (b * alphaAO)
                double maxRes = - Double.MAX_VALUE;
                int selectedI = 0;

                // for each alpha vector (alphaAO), select a best alpha index
                // System.out.println("alphaAOVecs SIZE: " + AlphaAOVecs.get(aI).get(oI).size());
                for (int i = 0; i < AlphaAOVecs.get(aI).get(oI).size(); i++)
                {
                    // System.out.println("i: " + i);
                    // AlphaAOVecs.get(aI).get(oI).get(i).printAlphaVector();
                    if (dot(b, AlphaAOVecs.get(aI).get(oI).get(i)) > maxRes)
                    {
                        maxRes = dot(b, AlphaAOVecs.get(aI).get(oI).get(i));
                        selectedI = i;
                    }
                }
                // System.out.println("MAXRES: " + maxRes);
                // System.out.println("selecID: " + selectedI);
                // sum of argmax (b * alphaAO)
                for (int sI = 0; sI < Pb.getSizeofStates(); sI++)
                {
                    double tmp = AlphaAOVecs.get(aI).get(oI).get(selectedI).getIndex(sI);
                    // System.out.println(AlphaAOVecs.get(aI).get(oI).get(selectedI).getIndex(sI));
                    values.set(sI, values.get(sI) + tmp);
                }
            } 

            for (int sI = 0; sI < Pb.getSizeofStates(); sI++)
            {
                values.set(sI, rewards.get(sI) + gamma * values.get(sI));            
            }
            AlphaVector alphaAB = new AlphaVector(values, aI);
            return alphaAB;
        }

   

    public AlphaVector backup(ArrayList<AlphaVector> alphaVectors, Belief b, ArrayList<ArrayList<ArrayList<AlphaVector>>> AlphaAOVecs, 
        ArrayList<ArrayList<Double>> rewardVectors, double gamma, PomdpInterface Pb) {
            double res = - Double.MAX_VALUE;
            int selectedI = -1;
            ArrayList<AlphaVector> vecAlpha = new ArrayList<AlphaVector>();
            for (int aI = 0; aI < Pb.getSizeofActions(); aI++)
            {
                vecAlpha.add(updateAlphaAB(aI, AlphaAOVecs, b, rewardVectors, gamma, Pb));
                double tmp = dot(b, vecAlpha.get(aI));
                if (tmp > res)
                {
                    res = tmp;
                    selectedI = aI;
                }
            }
            return vecAlpha.get(selectedI);
        }
    
    public boolean checkAlpha(ArrayList<AlphaVector> alphaVectors, AlphaVector alpha) {
        if (alphaVectors.size() == 0)
        {
            return false;
        }
        for (int i = 0; i < alphaVectors.size(); i++)
        {
            // System.out.println("............check.........");
            // alphaVectors.get(i).printAlphaVector();
            // alpha.printAlphaVector();
            // System.out.println("............check.........");
            if (alphaVectors.get(i).equal(alpha))
            {
                // System.out.println("<<<<<<<<<<<<< true >>>>>>>>>>>>>>>>>>");
                return true;
            }
        }
        // System.out.println("<<<<<<<<<<<<< false >>>>>>>>>>>>>>>>>>");
        return false;
    }

    public double DistanceAlpha(AlphaVector a, AlphaVector b) {
        double result = 0;
        if (a.getSize() != b.getSize())
        {
            System.out.println("The size of alpha vectors are not equal.\n");
            System.exit(0);
            // throw new Exception("");
        }
        else
        {
            for (int i = 0; i < a.getSize(); i++)
            {
                result += Math.abs(a.getIndex(i) - b.getIndex(i));
            }
        }
        return result;
    }

    public boolean checkConvergence(ArrayList<AlphaVector> vectorsA, ArrayList<AlphaVector> vectorsB, double err) {
        // check dimension
        if (vectorsA.size() != vectorsB.size())
        {
            return false;
        }
        else
        {
            for (int i = 0; i < vectorsA.size(); i++)
            {
                // System.out.println("dis: " + DistanceAlpha(vectorsA.get(i), vectorsB.get(i)));
                // System.out.println("err: " + err);
                if (DistanceAlpha(vectorsA.get(i), vectorsB.get(i)) > err)
                {
                    return false;
                }
            }
        }
        return true;
    }

    

    public void improve(ArrayList<AlphaVector> alphaVectors, ArrayList<Belief> beliefSet, ArrayList<ArrayList<Double>> rewardVectors, 
        double gamma, int MAX_ITER_IMP, PomdpInterface Pb) {
            Double err = Double.MIN_VALUE;
            // System.out.println("err: " + err);
            ArrayList<AlphaVector> tmpVectors = new ArrayList<AlphaVector>(alphaVectors);
            // for (int i = 0; i < alphaVectors.size(); i++)
            // {
            //     tmpVectors.add(alphaVectors.get(i));
            // }
            alphaVectors.clear();
            // tmpVectors.get(0).printAlphaVector();
            for (int i = 0; i < MAX_ITER_IMP; ++i)
            {
                // compute a vector to store alpha_a_o values
                ArrayList<ArrayList<ArrayList<AlphaVector>>> AlphaAOVecs = computeAllAlphaAOValues(tmpVectors, Pb);
                // beliefSet.get(0).printBelief(); 
                for (int bi = 0; bi < beliefSet.size(); bi++)
                {
                    AlphaVector alpha = backup(tmpVectors, beliefSet.get(bi), AlphaAOVecs, rewardVectors, gamma, Pb);
                    // alpha.printAlphaVector();
                    if (!checkAlpha(alphaVectors, alpha))
                    {
                        alphaVectors.add(alpha);
                    }
                }

                // check converged
                // System.out.println("----check improve-----");
                if (checkConvergence(tmpVectors, alphaVectors, err))
                {
                    System.out.println("Converged in improvement at iteration: " + i + "\n");
                    return;
                }
                else
                {
                    tmpVectors = new ArrayList<AlphaVector>(alphaVectors);
                    // System.out.println("tmp size: " + tmpVectors.size());
                }
            }

        }

    public double probOBA(int oI, Belief b, int aI, PomdpInterface Pb) {
        double probOBA = 0;
        for (int newSI = 0; newSI < Pb.getSizeofStates(); newSI++)
        {
            double probNewSI = 0;
            for (int sI = 0; sI < Pb.getSizeofStates(); sI++)
            {
                probNewSI += Pb.transFunction(aI, sI, newSI) * Pb.obsFunction(aI, newSI, oI) * b.get(sI);
            }
            probOBA += probNewSI;
        }
        return probOBA;
    }
    
    public Belief update(int oI, int aI, Belief b, PomdpInterface Pb) {
        ArrayList<Double> bAindexValue = new ArrayList<Double>();
        double probOBA = probOBA(oI, b, aI, Pb);
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
        Belief bAO = new Belief(bAindexValue);
        return bAO;
    }

    public ArrayList<Belief> succBelief(Belief b, PomdpInterface Pb) {
        ArrayList<Belief> succBelief = new ArrayList<Belief>();
        for (int aI = 0; aI < Pb.getSizeofActions(); aI++)
        {
            for (int oI = 0; oI < Pb.getSizeObservables(); oI++)
            {
                Belief bAO = update(oI, aI, b, Pb);
                succBelief.add(bAO);
            }
        }
        return succBelief;
    }

    public double compAvgL1Dis(Belief b, ArrayList<Belief> beliefSets, int NrB) {
        double result = 0;
        ArrayList<Double> beliefValue = b.getBelief();
        
        // for each belief point int the belief set
        for (int i = 0; i < NrB; i++)
        {
            ArrayList<Double> tmpBelief = beliefSets.get(i).getBelief();
            double tmpSum = 0;
            for (int j = 0; j < beliefValue.size(); j++)
            {
                tmpSum += Math.abs(beliefValue.get(j) - tmpBelief.get(j));
            }
            
            // if tmpSum = 0, b is already in the beliefSets, return 0
            if (tmpSum < MIN_B_ACC)
            {
                return 0;
            }
            result += tmpSum;
        }
        result = result / beliefSets.size();
        return result;
    }


    // find the most far distance belief point in tmp belief set and add it to current belief set.
    public void expandBeliefSets(ArrayList<Belief> beliefSets, ArrayList<Belief> tmpBeliefSets, int NrB) {
        if (beliefSets.size() >= NrB) 
        {
            int selectedIndex = -1;
            double dis = 0;
            for (int i = 0; i < tmpBeliefSets.size(); i++)
            {
                double tmpDis = compAvgL1Dis(tmpBeliefSets.get(i), beliefSets, NrB);
                if (tmpDis > dis)
                {
                    dis = tmpDis;
                    selectedIndex = i;
                }
            }

            // System.out.println("dis: " + dis + " MIN_B_ACC: " + MIN_B_ACC);
            if (dis > MIN_B_ACC && selectedIndex > -1)
            {
                beliefSets.add(tmpBeliefSets.get(selectedIndex));
            }
        }
        else
        {
            System.out.println("The size of belief sets and the given number of belief point do not match.\n");
            System.exit(0);
        }
    }

    public ArrayList<Belief> expand(ArrayList<Belief> beliefSets, int MAX_BP, PomdpInterface Pb) {
        int NrB = beliefSets.size();

        // for each belief point in beliefSets
        if (beliefSets.size() < MAX_BP)
        {
            for(int i = 0; i < NrB; i++)
            {
                ArrayList<Belief> succBelief = succBelief(beliefSets.get(i), Pb);
                expandBeliefSets(beliefSets, succBelief, NrB);
            }
            return beliefSets;
        }
        else
        {
            return beliefSets;
        }
    }

    public void plan(ArrayList<AlphaVector> alphaVectors, ArrayList<Belief> beliefSet, int maxBP, ArrayList<ArrayList<Double>> rewardVectors, 
        Double gamma, int MAX_ITER_G, int MAX_ITER_I, Double err, PomdpInterface Pb) {
    
        for (int i = 0; i < MAX_ITER_G; i++)
        {
            ArrayList<AlphaVector> tmpVec = new ArrayList<AlphaVector>(alphaVectors);
            improve(alphaVectors, beliefSet, rewardVectors, gamma, MAX_ITER_I, Pb);
          
            expand(beliefSet, MAX_BP, Pb);
            System.out.println("B: " + beliefSet.size());
            System.out.println("alpha size: " + alphaVectors.size());

            // System.out.println(".....tmp.....");
            // for (AlphaVector tmpA : tmpVec)
            // {
            //     tmpA.printAlphaVector();
            // }
            // System.out.println(".....tmp end.....");
            // System.out.println(".....alpha.....");
            // for (AlphaVector alpha : alphaVectors)
            // {
            //     alpha.printAlphaVector();
            // }
            // System.out.println(".....alpha end.....");
            // System.out.println("----check plan-----");
            if (checkConvergence(tmpVec, alphaVectors, err))
            {
                for (Belief belief : beliefSet)
                {
                    belief.printBelief();
                }
                System.out.println("Plan converged at iteration: " + i + "\n");
                return;
            }
        }
        for (Belief belief : beliefSet)
        {
            belief.printBelief();
        }
    }

    public void plan() {
        plan(this.alphaVectors, this.beliefSet, this.MAX_BP, this.rewardVectors, this.gamma, 
            this.MAX_ITER_GLOBAL, this.MAX_ITER_IMP, this.err, this.Pb);
    
    }

    public ArrayList<AlphaVector> getValueFunction() {
        return this.alphaVectors;
    }

    public ArrayList<AlphaVector> initAlphaVecs(double gamma, PomdpInterface Pb) {
        double Rmin = Double.MAX_VALUE;
        ArrayList<AlphaVector> v = new ArrayList<AlphaVector>();
        int select_aI = 0;
        ArrayList<Double> alphaValues = new ArrayList<Double>();
        for (int sI = 0; sI < Pb.getSizeofStates(); sI++)
        {
            for (int aI = 0; aI < Pb.getSizeofActions(); aI++)
            {
                // System.out.println("R: " + Pb.reward(aI, sI));
                if (Pb.reward(aI, sI) < Rmin)
                {
                    Rmin = Pb.reward(aI, sI);
                    select_aI = aI;
                    System.out.println("Rmin: " + Rmin + "aI: " + aI);
                }
            }
            alphaValues.add(Rmin/(1-gamma));
        }
        AlphaVector minAlpha = new AlphaVector(alphaValues, select_aI);
        v.add(minAlpha);
        return v;
    }

    public ArrayList<ArrayList<Double>> initRewardVecs(PomdpInterface Pb) {
        ArrayList<ArrayList<Double>> rewardVectors = new ArrayList<ArrayList<Double>>();
        for (int aI = 0; aI < Pb.getSizeofActions(); aI++)
        {
            ArrayList<Double> rewardAI = new ArrayList<Double>();
            for (int sI = 0; sI < Pb.getSizeofStates(); sI++)
            {
                rewardAI.add(Pb.reward(aI, sI));
            }
            rewardVectors.add(rewardAI);
        }
        return rewardVectors;
    }

    public PbviPlanner(double gamma, int MAX_BP, int MAX_ITER_GLOBAL, int MAX_ITER_IMP, PomdpInterface Pb) {
        this.gamma = gamma;
        this.MAX_BP = MAX_BP;
        this.MAX_ITER_GLOBAL = MAX_ITER_GLOBAL;
        this.MAX_ITER_IMP = MAX_ITER_IMP;
        this.Pb = Pb;
        this.alphaVectors = initAlphaVecs(gamma, this.Pb);
        this.rewardVectors = initRewardVecs(this.Pb);
        // PomdpInterface thisPb = this.Pb;
        ArrayList<Double> tmp = Pb.getInitBelief();
        Belief tmpBelief = new Belief(tmp);
        ArrayList<Belief> tmpBS = new ArrayList<Belief>();
        tmpBS.add(tmpBelief);
        this.beliefSet = tmpBS;
    }



}

