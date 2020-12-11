package epmc.pomdp.plugin;

import epmc.plugin.BeforeModelCreation;
import epmc.pomdp.value.OperatorEvaluatorPRISMPow;
import epmc.value.operatorevaluator.SimpleEvaluatorFactory;

public final class BeforeModelCreationPOMDP implements BeforeModelCreation {
    public final static String IDENTIFIER = "before-model-creation-pomdp";

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public void process() {
        SimpleEvaluatorFactory.get().add(OperatorEvaluatorPRISMPow.Builder.class);
    }

}
