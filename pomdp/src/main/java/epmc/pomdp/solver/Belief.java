package epmc.pomdp.solver;

import java.util.List;
import java.util.ArrayList;

class Belief {
    private ArrayList<Double> states;

    public Belief(ArrayList<Double> b) {
        this.states = b;
    }

    public ArrayList<Double> getBelief() {
        return this.states;
    }

    public long getSize() 
    {
        return this.states.size();
    }

    public double get(int i)
    {
        return this.states.get(i);
    }

    public void printBelief() {
        String out = "";
        out += "<";
        for (Double belief: getBelief()) {
            out += (belief + " ");  
        }
        out += ">";
        System.out.println(out);
    }
}