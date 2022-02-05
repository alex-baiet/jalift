package fr.elevator.projetelevator.model.elevator.ai;

import fr.elevator.projetelevator.model.building.Building;
import fr.elevator.projetelevator.model.elevator.Elevator;

/**
 * Intelligence d'asceneur faisant des aller retours dans l'immeuble en s'arrêtant à chaque étage.
 * Ne prend pas en compte les besoins des résidents.
 */
public class AIElevatorMetro implements AIElevator {
    /** Direction actuelle de l'ascenseur : 1 vers le haut, -1 vers le bas. */
    int direction = 1;

    @Override
    public int findNextDestination(Elevator elevator) {
        Building building = Building.getInstance();

        int position = elevator.getController().currentFloor();

        if (position == building.getFloorCount()-1) direction = -1;
        if (position == 0) direction = 1;

        return position + direction;
    }
}
