package fr.elevator.projetelevator.model.resident;

import fr.elevator.projetelevator.model.Timer;

/**
 * Représente un élément de l'historique d'un résident.
 *
 * @param act Type de l'action.
 * @param floor Etage où s'est déroulé l'action.
 * @param instant Quand s'est déroulé l'action.
 */
public record ResidentAction(ResidentActionChoice act, int floor, double instant) {
    public ResidentAction(ResidentActionChoice act, int floor) {
        this(act, floor, Timer.getTime());
    }

}
