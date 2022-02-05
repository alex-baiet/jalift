package fr.elevator.projetelevator.model.elevator.ai;

import fr.elevator.projetelevator.model.building.Building;
import fr.elevator.projetelevator.model.elevator.Elevator;
import fr.elevator.projetelevator.model.resident.Resident;

import java.util.List;

/**
 * Intelligence d'asceneur faisant des aller retours dans l'immeuble en s'arrêtant à chaque étage.
 * Ne prend pas en compte les besoins des résidents.
 */
public class AIElevatorBus implements AIElevator {
    private int direction = 1;

    @Override
    public int findNextDestination(Elevator elevator) {
        Building building = Building.getInstance();

        int oldPos = elevator.getController().currentFloor();
        int newPos = oldPos;
        boolean checkedOneWay = false;

        for (int tryCount = 1; tryCount < building.getFloorCount(); tryCount++) {
            // Extrémité atteinte, on teste dans l'autre sens.
            if (newPos == building.getFloorCount()-1) {
                direction = -1;
                newPos = oldPos;
            }
            if (newPos == 0) {
                direction = 1;
                newPos = oldPos;
            }

            newPos += direction;

            // Test est-ce que c'est utile de s'arrêter à l'étage ?
            if (building.getFloor(newPos).queueSize() > 0) {
                return newPos;
            }
            List<Resident> residents = elevator.getResidents();
            for (Resident res : residents) {
                if (res.getDestinationFloor() == newPos) {
                    return newPos;
                }
            }
        }

        return oldPos;
    }
}
