package epmc.pomdp.plugin;

import java.util.Map;

import epmc.graph.LowLevel;
import epmc.modelchecker.options.OptionsModelChecker;
import epmc.options.OptionTypeBoolean;
import epmc.options.OptionTypeMap;
import epmc.options.Options;
import epmc.plugin.AfterOptionsCreation;
import epmc.pomdp.model.LowLevelPOMDPBuilder;
import epmc.pomdp.model.ModelPOMDP;
import epmc.pomdp.model.PropertyPRISM;
import epmc.pomdp.model.convert.UtilPrismConverter;
// import epmc.pomdp.solver.POMDPSolver;
import epmc.prism.options.OptionsPRISM;


public final class AfterOptionsCreationPOMDP implements AfterOptionsCreation {
    public final static String IDENTIFIER = "after-options-creation-pomdp";

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public void process(Options options) {

        assert options != null;
        OptionTypeMap<Class<?>> modelInputType = options.getType(OptionsModelChecker.MODEL_INPUT_TYPE);
        
        assert modelInputType != null;
        modelInputType.put(ModelPOMDP.IDENTIFIER, ModelPOMDP.class);
        
        Map<String,Class<?>> propertyClasses = options.get(OptionsModelChecker.PROPERTY_CLASS);
        assert propertyClasses != null;
        propertyClasses.put(PropertyPRISM.IDENTIFIER, PropertyPRISM.class);
        OptionTypeBoolean typeBoolean = OptionTypeBoolean.getInstance();
        options.addOption().setBundleName(OptionsPRISM.PRISM_OPTIONS)
        .setIdentifier(OptionsPRISM.PRISM_FLATTEN)
        .setType(typeBoolean).setDefault(true)
        .setCommandLine().setGui().setWeb().build();
        
        Map<String,Class<? extends LowLevel.Builder>> map = 
                options.get(OptionsModelChecker.LOW_LEVEL_ENGINE_CLASS);

        map.put(LowLevelPOMDPBuilder.IDENTIFIER, LowLevelPOMDPBuilder.class);

        UtilPrismConverter.addOptions(options);
    }
}
