package fr.elevator.projetelevator.model.elevator.ai;

import fr.elevator.projetelevator.model.Floor;
import fr.elevator.projetelevator.model.building.Building;
import fr.elevator.projetelevator.model.elevator.Elevator;
import fr.elevator.projetelevator.model.elevator.ElevatorController;

/** Intelligence donnant la priorité aux gens à l'intérieur de l'ascenseur */
public class AIElevatorInsideFirst implements AIElevator {
    @Override
    public int findNextDestination(Elevator elevator) {
        Building building = Building.getInstance();
        ElevatorController controller = elevator.getController();

        // Cas vide : ascenseur -> on monte le plus haut possible ou il y a des gens
        if (elevator.getResidentCount() == 0) {
            // On cherche là ou il y a des gens
            for (int floorNum = building.getFloorCount()-1; floorNum >= 0; floorNum--) {
                Floor floor = building.getFloor(floorNum);
                if (floor.queueSize() > 0) {
                    return floorNum;
                }
            }
        } else {
            return elevator.getResident(0).getDestinationFloor();
        }

        return controller.currentFloor();
    }
}
