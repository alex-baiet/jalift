package fr.elevator.projetelevator.model.resident;

/**
 * Action futur d'un Resident.
 *
 * @param time Moment du déplacement.
 * @param destination Destination cible du déplacement.
 */
public record Schedule(double time, int destination) implements Comparable<Schedule> {
    @Override
    public int compareTo(Schedule o) {
        return Double.compare(time, o.time);
    }
}
