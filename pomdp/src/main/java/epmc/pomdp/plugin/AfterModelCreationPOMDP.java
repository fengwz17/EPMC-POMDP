package epmc.pomdp.plugin;

// import epmc.imdp.value.OperatorEvaluatorInterval;
import epmc.plugin.AfterModelCreation;
// import epmc.value.TypeInterval;
// import epmc.value.TypeWeightTransition;
import epmc.value.operatorevaluator.SimpleEvaluatorFactory;

/**
 * POMDP plugin class containing method to execute after model creation.
 * 
 * @author Weizhi Feng
 */
public final class AfterModelCreationPOMDP implements AfterModelCreation {
    /** Identifier of this class. */
    public final static String IDENTIFIER = "after-model-loading-pomdp";

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public void process() {
        SimpleEvaluatorFactory.get().add(OperatorEvaluatorInterval.Builder.class);
        TypeWeightTransition.set(TypeInterval.get());
    }
}
