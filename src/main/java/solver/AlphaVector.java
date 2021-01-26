package solver;

import java.util.ArrayList;

class AlphaVector {
    private ArrayList<Double> values;
    private int actionIndex;

    public AlphaVector (ArrayList<Double> values, int actionIndex) {
        this.values = values;
        this.actionIndex = actionIndex;
    }

    public int getSize() {
        return this.values.size();
    }

    public ArrayList<Double> getValues() {
        return this.values;
    }

    public int getActionIndex() {
        return this.actionIndex;
    }

    public Double getIndex(int i) {
        return this.values.get(i);
    }

    public void changeValue(int sI, double v) {
        this.values.set(sI, v);
    }

    public void printAlphaVector() {
        String out = "The action index in the alpha vector: ";
        out += this.actionIndex;
        out += "<";
        for (Double values : getValues()) {
            out += (values + " "); 
        }
        out += ">";
        System.out.println(out);
    }
}

    




