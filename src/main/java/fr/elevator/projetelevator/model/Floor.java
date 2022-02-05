package fr.elevator.projetelevator.model;

import fr.elevator.projetelevator.model.resident.Resident;
import fr.elevator.projetelevator.model.resident.ResidentAction;
import fr.elevator.projetelevator.model.resident.ResidentActionChoice;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/** Gère un étage spécifique de l'ascenseur. */
public class Floor {
    private final ArrayList<Resident> residents = new ArrayList<>();
    private final ArrayDeque<Resident> queue = new ArrayDeque<>();
    private final int floorPosition;

    public Floor(int position) {
        this.floorPosition = position;
    }


    public Floor(int position, List<Resident> residents) {
        this(position);
        this.residents.addAll(residents);
    }

    /** Indique la taille de la file d'attente. */
    public int queueSize() { return queue.size(); }

    /** Met à jour les queue en ajoutant les residents qui veulent sortir. */
    public void updateQueue() {
        for (int i = residents.size()-1; i >= 0; i--) {
            Resident resident = residents.get(i);
            if (resident.move()) {
                residents.remove(i);
                queue.addLast(resident);

                resident.getHistory().addAction(ResidentActionChoice.PRESENTATION, floorPosition);
            }
        }
    }

    /** Retire le nombre le personne indiqué de la file d'attente, ou toutes les personnes si le nombre est trop élevé. */
    public Resident removeFromQueue() { return queue.removeFirst(); }

    /** Donne le Resident tête de queue. */
    public Resident seeFromQueue() { return queue.getFirst(); }

    /** Ajoute un résident à l'étage. */
    public void addResident(Resident resident) { residents.add(resident); }
    /** Ajoute des résidents à l'étage. */
    public void addResident(List<Resident> residents) { this.residents.addAll(residents); }

    /** Renvoie le nombre de résident à l'étage. */
    public int residentCount() { return residents.size() + queue.size(); }

    /** Renvoie le nombre de résident à l'étage, sans les gens dans la file d'attente */
    public int residentHomeCount() { return residents.size(); }

    /** Renvoie le résident de l'étage hors queue. */
    public Resident getResidentHome(int index) { return residents.get(index); }

    public int getFloorPosition() { return floorPosition; }
    public ArrayDeque<Resident> getQueue() { return new ArrayDeque<>(queue); }

}
