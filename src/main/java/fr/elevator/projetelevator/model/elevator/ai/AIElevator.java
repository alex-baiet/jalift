package fr.elevator.projetelevator.model.elevator.ai;

import fr.elevator.projetelevator.model.elevator.Elevator;

public interface AIElevator {
    /**
     * Renvoie la prochaine destination de l'ascenseur.
     *
     * @return Prochaine destination.
     */
    int findNextDestination(Elevator elevator);

    static AIElevator stringToAI(String name) {
        switch (name) {
            case "basic": { return new AIElevatorBasic(); }
            case "bus": { return new AIElevatorBus(); }
            case "metro": { return new AIElevatorMetro(); }
            case "insideFirst": { return new AIElevatorInsideFirst(); }
//            case "synchronous": { return new AIElevatorSynchronous(); }
            default: { throw new IllegalArgumentException(name+" ne correspond a aucune AIElevator."); }
        }
    }
}
