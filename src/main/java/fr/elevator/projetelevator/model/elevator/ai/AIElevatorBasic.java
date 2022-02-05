package fr.elevator.projetelevator.model.elevator.ai;

import fr.elevator.projetelevator.model.Floor;
import fr.elevator.projetelevator.model.building.Building;
import fr.elevator.projetelevator.model.elevator.Elevator;
import fr.elevator.projetelevator.model.elevator.ElevatorController;
import fr.elevator.projetelevator.model.resident.Resident;

import java.util.List;

/** Intelligence classique d'ascenseur souvent utilisé dans les immeubles. */
public class AIElevatorBasic implements AIElevator {
    private int direction = 1;

    @Override
    public int findNextDestination(Elevator elevator) {
        Building building = Building.getInstance();
        ElevatorController controller = elevator.getController();

        if (elevator.getResidentCount() == 0) {
            // Cas vide : ascenseur -> on monte le plus haut possible ou il y a des gens
            for (int floorNum = building.getFloorCount()-1; floorNum >= 0; floorNum--) {
                Floor floor = building.getFloor(floorNum);
                if (floor.queueSize() > 0) {
                    return floorNum;
                }
            }
        } else {
            // Cas pas vide : on se déplace comme un bus... en plus intelligent.
            return findNextNotEmpty(elevator);
        }

        return controller.currentFloor();
    }

    /** Trouve la prochaine destination dans le cas d'un ascenseur non vide. */
    private int findNextNotEmpty(Elevator elevator) {
        Building building = Building.getInstance();

        int oldPos = elevator.getController().currentFloor();
        int newPos = oldPos;

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
            if (building.getFloor(newPos).queueSize() > 0 && direction == -1) {
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
