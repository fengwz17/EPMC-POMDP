package epmc.graph;

import epmc.graph.Semantics;


/**
 * Semantics type for Partially Markov Decision (POMDP).
 */
public enum SemanticsPOMDP implements Semantics {
    /** Singleton element. */
    POMDP;

    /**
     * Checks whether this is a Partially Markov Decision (POMDP).
     * 
     * @return whether this is Partially Markov Decision (POMDP)
     */
    public static boolean isPOMDP(Semantics semantics) {
        return semantics instanceof SemanticsPOMDP;
    }

    
}
