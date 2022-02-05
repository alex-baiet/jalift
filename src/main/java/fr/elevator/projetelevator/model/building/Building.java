package fr.elevator.projetelevator.model.building;

import fr.elevator.projetelevator.model.Floor;
import fr.elevator.projetelevator.model.Helper;
import fr.elevator.projetelevator.model.json.Range;
import fr.elevator.projetelevator.model.json.BuildingJson;
import fr.elevator.projetelevator.model.resident.Schedule;
import fr.elevator.projetelevator.model.elevator.Elevator;
import fr.elevator.projetelevator.model.resident.Resident;

import java.util.ArrayList;
import java.util.List;

public class Building {
    private final ArrayList<Floor> floors = new ArrayList<>();
    private final double floorHeight;
    private final ArrayList<Elevator> elevators = new ArrayList<>();

    private static Building instance;

    public static Building getInstance() { return instance; }

    public Building(int floorCount, double floorHeight, int residentCount, List<Range> enterRanges, List<Range> exitRanges, List<Elevator> elevators) {
        if (instance != null) {
            throw new IllegalStateException("Un Building existe déjà.");
        }
        if (floorCount <= 0) {
            throw new IllegalArgumentException("Un immeuble doit contenir au moins un étage.");
        }

        instance = this;
        this.floorHeight = floorHeight;
        this.elevators.addAll(elevators);

        // Création du rez-de-chaussée vide
        floors.add(new Floor(0));
        // Création des étages
        for (int i = 1; i < floorCount; i++) {
            int nbrResident = residentCount / (floorCount-1);
            if (residentCount % (floorCount-1) > i) nbrResident++;

            floors.add(initFloor(i, nbrResident, enterRanges, exitRanges));
        }
    }

    public Building(BuildingJson jsonObj) {
        this(
                jsonObj.getFloorCount(),
                jsonObj.getFloorHeight(),
                jsonObj.getResidentCount(),
                jsonObj.getEnterRanges(),
                jsonObj.getExitRanges(),
                Elevator.createElevators(jsonObj.getElevators())
                );
    }

    /** Initialise un étage et ses habitants. */
    private Floor initFloor(int num, int nbrResident, List<Range> enterRanges, List<Range> exitRanges) {
        ArrayList<Resident> residents = new ArrayList<>();
        for (int j = 0; j < nbrResident; j++) {
            residents.add(initResident(num, enterRanges, exitRanges));
        }
        return new Floor(num, residents);
    }

    /** Initialise un résident de l'immeuble et ses habitudes. */
    private Resident initResident(int floorHome, List<Range> enterRanges, List<Range> exitRanges) {
        Resident resident = new Resident(70, floorHome);

        // Ajout des dates d'entrée dans l'immeuble.
        for (Range range : enterRanges) {
            double time = range.start() + (range.end() - range.start()) * Helper.random.nextDouble();
            resident.addSchedule(new Schedule(time, floorHome));
        }

        // Ajout des dates de sortie de l'immeuble.
        for (Range range : exitRanges) {
            double time = range.start() + (range.end() - range.start()) * Helper.random.nextDouble();
            resident.addSchedule(new Schedule(time, 0));
        }

        return resident;
    }

    /** Ajoute tous les residents qui veulent sortir de l'immeuble dans les files d'attentes. */
    public void updateQueues() {
        for (Floor floor : floors) floor.updateQueue();
    }

    public double getFloorHeight() { return floorHeight; }
    public int getFloorCount() { return floors.size(); }
    public Floor getFloor(int index) { return floors.get(index); }

    public int elevatorCount() { return elevators.size(); }
    public Elevator getElevator(int index) { return elevators.get(index); }
    public List<Elevator> getElevators() { return new ArrayList<>(elevators); }

    @Override
    public String toString() {
        return "Building{" +
                "floorCount=" + getFloorCount() +
                ", floorHeight=" + floorHeight +
                ", elevatorCount=" + elevatorCount() +
                '}';
    }
}
