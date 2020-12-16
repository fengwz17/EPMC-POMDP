package epmc.pomdp.model.convert;

import epmc.jani.model.ModelJANI;
import epmc.prism.model.ModelPRISM;

import epmc.pomdp.model.ModelPOMDP;

public interface SystemConverter {
    void setPRISMModel(ModelPOMDP modelPomdp);

    void setJANIModel(ModelJANI modelJani);

    void convert();
}
