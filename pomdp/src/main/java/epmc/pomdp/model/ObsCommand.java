package epmc.pomdp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import epmc.prism.model.Alternative;
import epmc.prism.model.Command;

/**
 * Observables command
 *
 * 
 * @author Andrea Turrini
 */

public final class ObsCommand {
    private final Command command;
    private final List<Alternative> observables = new ArrayList<>();
    
    public ObsCommand(Command command, List<Alternative> observables) {
        this.command = command;
        this.observables.addAll(observables);
    }

    public Command getCommand() {
        return command;
    }

    public List<Alternative> getObservables() {
        return Collections.unmodifiableList(observables);
    }
}
