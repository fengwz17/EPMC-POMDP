package solver;

import java.util.List;
import java.util.ArrayList;

class Belief {
    private ArrayList<Double> prob_states;

    public Belief(ArrayList<Double> b) {
        this.prob_states = b;
    }

    public ArrayList<Double> getBelief() {
        return this.prob_states;
    }

    public long getSize() 
    {
        return this.prob_states.size();
    }

    public double get(int i)
    {
        return this.prob_states.get(i);
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