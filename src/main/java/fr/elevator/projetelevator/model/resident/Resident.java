package fr.elevator.projetelevator.model.resident;

import fr.elevator.projetelevator.model.Timer;
import fr.elevator.projetelevator.model.building.Building;
import fr.elevator.projetelevator.model.elevator.Elevator;
import fr.elevator.projetelevator.model.json.Names;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Resident {
    private static int lastGivenId = 0;
    private static final ArrayList<Resident> residents = new ArrayList<>();

    private final int id;
    private final String firstName;
    private final String lastName;
    private final double weight;
    private final int homeFloor;
    private int positionFloor;
    private int destinationFloor;
    private final TreeSet<Schedule> schedules = new TreeSet<>();
    private final ResidentHistory history = new ResidentHistory();

    public Resident(double weight, int homeFloor) {
        this.id = lastGivenId++;
        this.weight = weight;
        this.homeFloor = homeFloor;
        destinationFloor = homeFloor;
        positionFloor = homeFloor;
        Names names = Names.getInstance();
        firstName = names.getRandomFirstName();
        lastName = names.getRandomLastName();

        residents.add(this);
    }

    /** Renvoie le nombre total de résident créé par la classe. */
    public static int countCreatedResident() {
        return lastGivenId;
    }

    /** Renvoie le Résident correspondant à l'id donné. */
    public static Resident getResidentById(int id) {
        return residents.get(id);
    }

    /** Ajoute une action future du résident. */
    public void addSchedule(Schedule schedule) { schedules.add(schedule); }

    /** Permet de savoir si le résident est dans un ascenseur ou non. */
    public boolean isInsideElevator() {
        Building building = Building.getInstance();
        List<Elevator> elevators = building.getElevators();
        for (Elevator elevator : elevators) {
            if (elevator.containsResident(this)) {
                return true;
            }
        }
        return false;
    }

    private Schedule getNextSchedule() { return schedules.iterator().next(); }

    private Schedule removeNexSchedule() {
        Schedule sch = getNextSchedule();
        schedules.remove(sch);
        return sch;
    }

    /**
     * Prépare le résident pour sa prochaine destination selon son emploi du temps.
     * @return true si le résident souhaite faire une déplacement, et false si il ne souhaite rien faire.
     */
    public boolean move() {
        if (schedules.size() == 0) return false;

        Schedule schedule = getNextSchedule();
        if (schedule.time() < Timer.getTime()) {
            removeNexSchedule();
            if (positionFloor == schedule.destination()) {
                // La destination est déjà atteinte : Test pour l'action suivante.
                return move();
            } else {
                destinationFloor = schedule.destination();
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Resident{" +
                "id=" + id +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", weight=" + weight +
                ", homeFloor=" + homeFloor +
                ", destinationFloor=" + destinationFloor +
                '}';
    }

    public double getWeight() { return weight; }
    public int getHomeFloor() { return homeFloor; }
    public int getDestinationFloor() { return destinationFloor; }
    public int getPositionFloor() { return positionFloor; }
    public void setPositionFloor(int position) { this.positionFloor = position; }
    public ResidentHistory getHistory() {
        return history;
    }
    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
}
