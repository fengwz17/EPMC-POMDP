package epmc.pomdp.model;

import static epmc.error.UtilError.ensure;

import java.io.InputStream;
import epmc.graph.Semantics;
import epmc.jani.model.ModelJANI;
import epmc.jani.model.ModelJANIConverter;
import epmc.modelchecker.Model;
import epmc.modelchecker.Property;
import epmc.prism.model.ModelPRISM;

import epmc.pomdp.error.ProblemsPRISM;


// TODO probably better to do multiplicity checking just here, not in parser
// TODO can already include some checks on probabilities, though not in all case
// TODO there are almost no checks for validity of Markov automata

/**
 * PRISM model representation.
 * This file represents a PRISM high-level model. The semantical models which
 * can be represented are the ones originally from PRISM, plus Markov automata.
 * Quantum Markov chains are <emph>not</emph> handled by this model. Instead,
 * they are handled by the according file in the QMC plugin.
 * 
 * Extend to pomdp model.
 * 
 * @author Weizhi Feng
 */

public final class ModelPOMDP implements Model, ModelJANIConverter {
    public final static String IDENTIFIER = "pomdp";
    private ModelPRISM modelPRISM = new ModelPRISM();

    @Override
    public Semantics getSemantics() {
        return modelPRISM.getSemantics();
    }

    @Override
    public Properties getPropertyList() {
        return modelPRISM.getPropertyList();
    }

    @Override
    public void read(Object part, InputStream... inputs) {
        assert inputs != null;
        ensure(inputs.length == 1, ProblemsPOMDP.PRISM_POMDP_ONE_MODEL_FILE, inputs.length);
        POMDPParser parser = new POMDPParser(inputs[0]);    
        parser.setModel(modelPRISM);
        parser.parseModel();
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public ModelJANI toJANI(boolean forExporting) {
        return modelPRISM.toJANI(forExporting);
    }
    
    public ModelPRISM getModelPRISM() {
        return modelPRISM;
    }
}
    }
    