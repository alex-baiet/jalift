package fr.elevator.projetelevator.model.elevator;

/**
 * @param floor
 */
public record ElevatorAction(ElevatorActionChoice act, double instant, int floor, int nbResident) {

}
