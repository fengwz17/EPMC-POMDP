package epmc.pomdp.model;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.print.attribute.standard.Destination;

import epmc.expression.standard.ExpressionIdentifier;
import epmc.expression.standard.ExpressionIdentifierStandard;
import epmc.expression.standard.RewardSpecification;
import epmc.expression.standard.RewardSpecificationImpl;
import epmc.graph.LowLevel;
import epmc.graph.LowLevel.Builder;
import epmc.jani.model.ModelJANI;
import epmc.jani.model.*;
import epmc.modelchecker.Engine;
import epmc.modelchecker.EngineDD;
import epmc.modelchecker.EngineExplicit;
import epmc.modelchecker.EngineExplorer;
import epmc.modelchecker.Model;
import epmc.modelchecker.UtilModelChecker;

import epmc.pomdp.model.convert.POMDP2JANIConverter;
// import epmc.prism.model.convert.POMDP2JANIConverter;
// TODO: add the LowLevel definition of POMDP
public final class LowLevelPOMDPBuilder implements LowLevel.Builder {
    public final static String IDENTIFIER = "prism";
    
    private Model model;
    private Engine engine;
    private Set<Object> graphProperties = new LinkedHashSet<>();
    private Set<Object> nodeProperties = new LinkedHashSet<>();
    private Set<Object> edgeProperties = new LinkedHashSet<>();

    @Override
    public Builder setModel(Model model) {
        this.model = model;
        return this;
    }

    @Override
    public Builder setEngine(Engine engine) {
        this.engine = engine;
        return this;
    }

    @Override
    public Builder addGraphProperties(Set<Object> graphProperties) {
        this.graphProperties.addAll(graphProperties);
        return this;
    }

    @Override
    public Builder addNodeProperties(Set<Object> nodeProperties) {
        this.nodeProperties.addAll(nodeProperties);
        return this;
    }

    @Override
    public Builder addEdgeProperties(Set<Object> edgeProperties) {
        this.edgeProperties.addAll(edgeProperties);
        return this;
    }

    private Set<Object> fixProperties(Set<Object> properties) {
        Set<Object> fixed = new LinkedHashSet<>(properties.size());
        for (Object property : properties) {
            if (property instanceof RewardSpecification) {
                RewardSpecification rewardSpecification = (RewardSpecification) property;
                RewardStructure rewardStructure = ((ModelPOMDP) model).getReward(rewardSpecification);
                ExpressionIdentifier rewardName = new ExpressionIdentifierStandard.Builder()
                        .setName(rewardStructure.getName())
                        .build();
                RewardSpecification fixedRewardSpecification = new RewardSpecificationImpl(rewardName);             
                fixed.add(fixedRewardSpecification);
            } else {
                fixed.add(property);
            }
        }
        return fixed;
    }

    public ModelJANI toJANI(boolean forExporting) {
        POMDP2JANIConverter converter = new POMDP2JANIConverter((ModelPOMDP) model, forExporting);
        return converter.convert();
    }

    @Override
    public LowLevel build() {
        if (!(model instanceof ModelPOMDP)) {
            return null;
        }
        graphProperties = fixProperties(graphProperties);
        nodeProperties = fixProperties(nodeProperties);
        edgeProperties = fixProperties(edgeProperties);

        if (engine instanceof EngineExplorer
                || engine instanceof EngineExplicit) {
            ModelJANI jani = toJANI(false);

            System.out.println("jani actions: " + jani.getActions());
            System.out.println("jani observables: " + jani.getObservables());
            for(Automaton auto : jani.getAutomata())
            {
                System.out.println("DEBUG: automaton");
                int i = 0;
                for(Edge e : auto.getEdges()){
                    i++;
                    for(epmc.jani.model.Destination d : e.getDestinations()){
                        System.out.println("DEBUG: Edge " + i);
                        System.out.println("DEBUG: edge: " + e.getLocation().getName() + ", " + e.getAction().getName()  + ", " + d.getLocation().getName());
                        //+ ", " + e.getGuard().getExp().toString() + ", " + e.getRate().toString()
                    }
                }
            }
            
            System.out.println("jani getGlobalVariables: " + jani.getGlobalVariables());

            return UtilModelChecker.buildLowLevel(jani, engine,
                    graphProperties, nodeProperties, edgeProperties);
        } else if (engine instanceof EngineDD){
            ((ModelPOMDP) model).prepareAndCheckReady();
            return new GraphDDPRISM((ModelPOMDP) model, nodeProperties, edgeProperties);
        } else {
            return null;
        }
    }

}
