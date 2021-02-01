package solver;

import java.util.ArrayList;

class AlphaVector {
    private ArrayList<Double> values;
    private int actionIndex;

    public AlphaVector () {
        this.values = new ArrayList<Double>();
        this.actionIndex = 0;
    }

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

    public void setActionIndex(int ai) {
        this.actionIndex = ai;
    }

    public void addValue(double v) {
        this.values.add(v);
    }

    public void changeValue(int i, double v) {
        this.values.set(i, v);
    }

    public boolean equal(AlphaVector a) {
        if (this.actionIndex == a.actionIndex)
        {
            for (int i = 0; i < this.values.size(); i++)
            {
                if (Math.abs(this.values.get(i) - a.getIndex(i)) >= 0.0001)
                {
                    return false;
                }
            }
        }
        else
        {
            return false;
        }
        return true;
    }

    public void printAlphaVector() {
        String out = "The action index in the alpha vector: ";
        out += this.actionIndex;
        out += " < ";
        for (Double values : getValues()) {
            out += (values + " "); 
        }
        out += " > ";
        System.out.println(out);
    }
}

    




