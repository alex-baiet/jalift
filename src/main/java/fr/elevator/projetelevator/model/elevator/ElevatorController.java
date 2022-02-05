package fr.elevator.projetelevator.model.elevator;

import fr.elevator.projetelevator.model.*;
import fr.elevator.projetelevator.model.building.Building;
import fr.elevator.projetelevator.model.elevator.ai.AIElevator;
import fr.elevator.projetelevator.model.resident.Resident;

import java.util.ArrayList;

/** Partie logique de l'ascenseur gérant ses mouvements. */
public class ElevatorController {
    private static final boolean DEBUG = false;

    private final Elevator elevator;
    private final AIElevator ai;
    private int lastFloor;
    private int nextFloor;
    /** Instant de la dernière action effectué. */
    private double lastPosTime = 0;
    /** Indique si l'ascenseur fait son temps d'arrêt. */
    private boolean isStopped = false;

    public ElevatorController(Elevator elevator, AIElevator ai, int floorPosition) {
        this.elevator = elevator;
        this.ai = ai;
        this.lastFloor = floorPosition;
        this.nextFloor = floorPosition;
    }

    public Elevator getElevator() { return elevator; }

    /** Retourne la position en étage de l'asceneur à l'arrêt. */
    public int currentFloor() {
        if (lastFloor != nextFloor) throw new IllegalStateException("L'ascenseur doit être à l'arrêt pour pouvoir récupérer sa position.");
        return nextFloor;
    }

    /** Met à jour l'ascenseur. */
    public void update() {
        // Définition / récupération des valeurs nécessaires
        Building building = Building.getInstance();
        Floor floor = building.getFloor(nextFloor);

        // L'ascenseur attend à un étage
        if (lastFloor == nextFloor) {
            if (isStopped) {
                double curTime = Timer.getTime();
                if (curTime - lastPosTime <= elevator.getStopDuration()) {
                    // L'ascenseur continue son temps d'arrêt.
                    return;
                }
            }
            // Entrée des résidents
            while (floor.queueSize() > 0) {
                Resident resident = floor.seeFromQueue();
                if (elevator.getContentWeight() + resident.getWeight() > elevator.getMaxContentWeight()) break;
                elevator.addResident(floor.removeFromQueue());
            }

            // L'ascenseur a terminé son temps d'arrêt et recherche la prochaine destination
            lastPosTime = Timer.getTime();
            isStopped = false;

            findNextDestination();
        }

        // L'ascenseur est en déplacement
        double position = estimatePosition();
        if (lastFloor != nextFloor && position == nextFloor * building.getFloorHeight()) {
            // L'ascenseur est arrivé et s'arrête
            lastFloor = nextFloor;
            lastPosTime = Timer.getTime();
            isStopped = true;

            // Sortie des residents
            ArrayList<Resident> out = elevator.removeResident(nextFloor);
            floor.addResident(out);

            elevator.addToHistory(ElevatorActionChoice.ARRIVE);
        }

    }

    /** Met à jour et renvoie la prochaine destination de l'ascenseur. */
    public int findNextDestination() {
        // Destination déjà défini
        if (lastFloor != nextFloor) return nextFloor;

        int newFloor = ai.findNextDestination(elevator);
        if (newFloor != nextFloor) {
            setNextFloor(newFloor);
        }
        return nextFloor;
    }

    private void setNextFloor(int floor) {
        elevator.addToHistory(ElevatorActionChoice.DEPART);
        nextFloor = floor;
        lastPosTime = Timer.getTime();
    }

    /** Retourne la position sur Y de l'Elevator en mètres. */
    public double estimatePosition() {
        Building building = Building.getInstance();

        // Pas de mouvement
        if (lastFloor == nextFloor || isStopped) return lastFloor * building.getFloorHeight();

        // Durée de parcours de la distance
        double timeTravel = elevator.timeTravel(Math.abs(nextFloor - lastFloor));
        // Durée de parcours déjà fait
        double curDuration = Timer.getTime() - lastPosTime;
        // Destination atteinte
        if (curDuration >= timeTravel) return nextFloor * building.getFloorHeight();

        double maxSpeed = elevator.getMaxSpeed();
        double acceleration = elevator.getAcceleration();

        // calcule du temps d'accélération nécessaire
        double accelerationTime = maxSpeed/acceleration;
        int directionMove = ((nextFloor - lastFloor) / Math.abs(nextFloor - lastFloor));
        double traveledDistance;

        if (accelerationTime*2 > timeTravel) {
            // Cas l'ascenseur n'a pas le temps d'atteindre sa vitesse max
            if (curDuration < timeTravel / 2) {
                // L'ascenseur est dans la 1ere moitié du parcours
                traveledDistance = Helper.travelAcceleration(acceleration, curDuration);
            } else {
                // L'ascenseur est dans la 2nd moitié du parcours
                double distance = Math.abs(nextFloor - lastFloor) * building.getFloorHeight();
                traveledDistance = distance - Helper.travelAcceleration(acceleration, timeTravel - curDuration);
            }
        } else {
            // Cas l'ascenseur atteint sa vitesse max
            if (curDuration < accelerationTime) {
                // Cas en cours d'acceleration
                traveledDistance = Helper.travelAcceleration(acceleration, curDuration);

            } else if (curDuration < timeTravel - accelerationTime) {
                // Cas FULL SPEED TUT TUT
                traveledDistance = Helper.travelAcceleration(acceleration, accelerationTime);
                traveledDistance += maxSpeed * (curDuration - accelerationTime);

            } else {
                // Cas en décélération
                double distance = Math.abs(nextFloor - lastFloor) * building.getFloorHeight();
                traveledDistance = distance - Helper.travelAcceleration(acceleration, timeTravel - curDuration);
            }
        }

        double position = lastFloor * building.getFloorHeight() + traveledDistance * directionMove;

        if (DEBUG) {
            System.out.println("##### ElevatorController");
            System.out.println("durationMove: "+timeTravel);
            System.out.println("curDuration: "+curDuration);
            System.out.println("lastFloor: "+lastFloor);
            System.out.println("nextFloor: "+nextFloor);
            System.out.println("speed: "+maxSpeed);
            System.out.println("directionMove: "+directionMove);
            System.out.println("position: "+position);
        }

        return position;
    }

    public double estimateSpeed() {
        // Pas de mouvement
        if (lastFloor == nextFloor || isStopped) return 0;

        // Durée de parcours de la distance
        double timeTravel = elevator.timeTravel(Math.abs(nextFloor - lastFloor));
        // Durée de parcours déjà fait
        double curDuration = Timer.getTime() - lastPosTime;
        // Destination atteinte
        if (curDuration >= timeTravel) return 0;

        double maxSpeed = elevator.getMaxSpeed();
        double acceleration = elevator.getAcceleration();

        // calcule du temps d'accélération nécessaire
        double accelerationTime = maxSpeed/acceleration;
        double speed;

        if (accelerationTime*2 > timeTravel) {
            // Cas l'ascenseur n'a pas le temps d'atteindre sa vitesse max
            if (curDuration < timeTravel / 2) {
                // L'ascenseur est dans la 1ere moitié du parcours
                speed = acceleration * curDuration;
            } else {
                // L'ascenseur est dans la 2nd moitié du parcours
                speed = acceleration * (timeTravel - curDuration);
            }
        } else {
            // Cas l'ascenseur atteint sa vitesse max
            if (curDuration < accelerationTime) {
                // Cas en cours d'acceleration
                speed = acceleration * curDuration;

            } else if (curDuration < timeTravel - accelerationTime) {
                // Cas FULL SPEED TUT TUT
                speed = maxSpeed;
            } else {
                // Cas en décélération
                speed = acceleration * (timeTravel - curDuration);
            }
        }
        return speed;
    }

    /** true si l'ascenseur n'a rien à faire. */
    public boolean isWaiting() {
        return lastFloor == nextFloor && !isStopped;
    }

}