package fr.elevator.projetelevator.model.elevator;

import fr.elevator.projetelevator.model.building.Building;
import fr.elevator.projetelevator.model.elevator.ai.AIElevator;
import fr.elevator.projetelevator.model.json.ElevatorJson;
import fr.elevator.projetelevator.model.resident.Resident;
import fr.elevator.projetelevator.model.resident.ResidentAction;
import fr.elevator.projetelevator.model.Timer;
import fr.elevator.projetelevator.model.resident.ResidentActionChoice;

import java.util.ArrayList;
import java.util.List;

/** Model de l'ascenseur contenant toutes ses caractéristiques. */
public class Elevator {
    private static int lastGivenId = 0;
    private final int id;

    private final ElevatorController controller;
    private final ElevatorHistory elevatorHistory = new ElevatorHistory(this);

    // Variables de gestion des gens à l'intérieur
    private double contentWeight = 0;
    private final double maxContentWeight;
    private final ArrayList<Resident> residents = new ArrayList<>();

    // Variables de vitesse et de temps
    private final double acceleration;
    private final double maxSpeed;
    private final double stopDuration;

    // Variables concernant l'énergie
    private final double weight;
    private final double energyCost;
    private final double alphaEnergyRecovery;

    public Elevator(
            double maxContentWeight,
            double acceleration,
            double maxSpeed,
            double stopDuration,
            AIElevator ai,
            double weight,
            double energyCost,
            double alphaEnergyRecovery,
            int floorPosition
    ) {
        this.id = lastGivenId++;
        this.maxContentWeight = maxContentWeight;
        this.acceleration = acceleration;
        this.maxSpeed = maxSpeed;
        this.stopDuration = stopDuration;
        controller = new ElevatorController(this, ai, floorPosition);
        this.weight = weight;
        this.energyCost = energyCost;
        this.alphaEnergyRecovery = alphaEnergyRecovery;
    }

    public Elevator(ElevatorJson jsonObj) {
        this(
                jsonObj.getMaxContentWeight(),
                jsonObj.getAcceleration(),
                jsonObj.getMaxSpeed(),
                jsonObj.getStopDuration(),
                AIElevator.stringToAI(jsonObj.getAi()),
                jsonObj.getWeight(),
                jsonObj.getEnergyCost(),
                jsonObj.getAlphaEnergyRecovery(),
                jsonObj.getFloorPosition()
        );
    }

    public static List<Elevator> createElevators(List<ElevatorJson> jsonsObj) {
        ArrayList<Elevator> res = new ArrayList<>();
        for (ElevatorJson json : jsonsObj) {
            res.add(new Elevator(json));
        }
        return res;
    }

    public double timeTravel(int floorCount){
        Building building = Building.getInstance();

        if (floorCount >= building.getFloorCount()) {
            throw new IllegalStateException("the requested floor does not exist, max floor:"+(building.getFloorCount()-1));
        }
        // calcule du temps d'accélération
        double accelerationTime = this.maxSpeed/this.acceleration;
        // calcule du nombre de mètre parcouru en cours d'accélération
        double accelerationTravel = (this.acceleration/2) * Math.pow(accelerationTime, 2);
        // Distance total à parcourir
        double totalHeight = building.getFloorHeight()*floorCount;
        // Si la distance à parcourir est trop courte pour arriver à pleine vitesse, ...
        if (accelerationTravel*2 > totalHeight) {
            // Temps nécessaire pour atteindre la vitesse maximal sur la distance donnée
            accelerationTime = Math.sqrt((totalHeight) / (this.acceleration));
            return accelerationTime*2;
        }

        // calcule de la distance total a parcourir en étant en vitesse maximal.
        double maxSpeedTravel = (building.getFloorHeight()*floorCount)-(accelerationTravel*2);
        // calcule du temps nécéssaire à parcourir les étages demandés
        return (accelerationTime*2)+(maxSpeedTravel/this.maxSpeed);
    }

    public double energyUse(double distance, double contentWeight) {
        double energyUse = distance * energyCost * (weight + contentWeight);
        if (energyUse < 0) energyUse *= alphaEnergyRecovery;
        return energyUse;
    }

    /**
     * Renvoie l'énergie utilisé sur la distance indiqué, en fonction du contenu actuel de l'ascenseur.
     *
     * @param distance Distance en mètres. Une valeur positive indique un parcourt vers le haut, et vice-versa.
     * @return L'énergie nécessaire. Une valeur négative signifie que l'ascenseur récupère de l'énergie sur le trajet.
     */
    public double energyUse(double distance) {
        return energyUse(distance, contentWeight);
    }

    /** Ajoute un résident dans l'ascenseur. */
    public void addResident(Resident resident) {
        if (resident.getWeight() + contentWeight > maxContentWeight) {
            throw new IllegalStateException("Impossible d'ajouter un resident dans l'ascenseur : le resident est trop lourd.");
        }

        contentWeight += resident.getWeight();
        residents.add(resident);

        resident.getHistory().addAction(ResidentActionChoice.EMBARK, controller.currentFloor());
    }

    /** Fait sortir tous les résidents ayant comme destination l'étage indiqué. */
    public ArrayList<Resident> removeResident(int floor) {
        ArrayList<Resident> removed = new ArrayList<>();
        for (int i = residents.size()-1; i>=0; i--) {
            if (residents.get(i).getDestinationFloor() == floor) {
                // Sortie du résident
                Resident resident = residents.remove(i);
                resident.getHistory().addAction(ResidentActionChoice.UNLOAD, floor);
                contentWeight -= resident.getWeight();
                resident.setPositionFloor(floor);
                removed.add(resident);
            }
        }

        return removed;
    }

    public boolean containsResident(Resident resident) {
        return residents.contains(resident);
    }

    public void addToHistory(ElevatorActionChoice act) {
        elevatorHistory.addAction(act, controller.currentFloor(), getResidentCount());
    }

    public int getId() { return id; }
    public ElevatorController getController() { return controller; }
    public double getMaxSpeed() { return maxSpeed; }
    public double getAcceleration() { return acceleration; }
    public double getStopDuration() { return stopDuration; }
    public double getContentWeight() { return contentWeight; }
    public double getMaxContentWeight() { return maxContentWeight; }
    public int getResidentCount() { return residents.size(); }
    public List<Resident> getResidents() { return new ArrayList<>(residents); }
    public Resident getResident(int index) { return residents.get(index); }
    /** Poids de l'ascenseur, sans prendre en compte son contenu. */
    public double getWeight() { return weight; }
    public double getEnergyCost() { return energyCost; }
    public double getAlphaEnergyRecovery() { return alphaEnergyRecovery; }
    public ElevatorHistory getElevatorHistory() {
        return elevatorHistory;
    }

    @Override
    public String toString() {
        return "Elevator{" +
                "weight=" + weight +
                ", maxContentWeight=" + maxContentWeight +
                ", acceleration=" + acceleration +
                ", maxSpeed=" + maxSpeed +
                ", stopDuration=" + stopDuration +
                ", energyCost=" + energyCost +
                ", alphaEnergyRecovery=" + alphaEnergyRecovery +
                '}';
    }
}
