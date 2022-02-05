package fr.elevator.projetelevator.model.elevator.ai;

import fr.elevator.projetelevator.model.Floor;
import fr.elevator.projetelevator.model.building.Building;
import fr.elevator.projetelevator.model.elevator.Elevator;
import fr.elevator.projetelevator.model.elevator.ElevatorController;
import fr.elevator.projetelevator.model.resident.Resident;

import java.util.HashSet;
import java.util.List;

/**
 * Intelligence classique d'ascenseur évitant les destinations communes avec d'autres ascenseurs.
 * NON FONCTIONNEL : il semblerait que le code soit asynchrone, ce qui entraîne des bugs trop complexes à résoudre.
 */
public class AIElevatorSynchronous implements AIElevator {
    private int direction = 1;
    /** Destination des autres ascenseurs */
    private HashSet<Integer> destinationOthers = new HashSet<>();

    @Override
    public int findNextDestination(Elevator elevator) {
        Building building = Building.getInstance();
        ElevatorController controller = elevator.getController();
        retrieveDestinations(elevator);

        if (elevator.getResidentCount() == 0) {
            // Cas vide : ascenseur -> on monte le plus haut possible ou il y a des gens
            for (int floorNum = building.getFloorCount()-1; floorNum >= 0; floorNum--) {
                Floor floor = building.getFloor(floorNum);
                if (floor.queueSize() > 0 /*&& !isDestinationTaken(floorNum)*/) {
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
            if (building.getFloor(newPos).queueSize() > 0 && direction == -1 /*&& !isDestinationTaken(newPos)*/) {
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

    /** Récupère les destinations de tous les autres ascenseurs. Fait aussi tout bugger. */
    private void retrieveDestinations(Elevator parentElevator) {
        destinationOthers.clear();
        Building building = Building.getInstance();
        for (int i = 0; i<building.elevatorCount(); i++) {
            Elevator elevator = building.getElevator(i);
            if (elevator != parentElevator && !elevator.getController().isWaiting()) {
                // Ajout de la destination de l'ascenseur
                System.out.println("start");
                destinationOthers.add(elevator.getController().findNextDestination());
                System.out.println("end");
            }
            System.out.println(i++);
        }

        System.out.println("Elevator n°"+parentElevator.getId()+" destinationsOthers : "+destinationOthers);
    }

    /** true si un autre ascenseur à comme destination l'étage indiqué. */
    private boolean isDestinationTaken(int floor) {
        return destinationOthers.contains(floor);
    }
}
