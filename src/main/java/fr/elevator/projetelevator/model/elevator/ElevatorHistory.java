package fr.elevator.projetelevator.model.elevator;

import fr.elevator.projetelevator.model.Timer;
import fr.elevator.projetelevator.model.building.Building;
import fr.elevator.projetelevator.model.json.JsonManager;
import fr.elevator.projetelevator.model.resident.Resident;

import static fr.elevator.projetelevator.model.elevator.ElevatorActionChoice.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ElevatorHistory {
    private final Elevator elevator;
    private final ArrayList<ElevatorAction> history = new ArrayList<>();

    private double energyUsed = 0;
    private List<Resident> previousContent;

    public ElevatorHistory(Elevator elevator) {
        this.elevator = elevator;
    }

    public void addAction(ElevatorActionChoice act, int floor, int nbResident) {
        history.add(new ElevatorAction(act, Timer.getTime(), floor, nbResident));

        // Maj valeurs pour calculer l'énergie utilisé
        if (act == DEPART) {
            previousContent = elevator.getResidents();
        }
        if (act == ARRIVE) {
            // Ajout de l'énergie consommé
            Building building = Building.getInstance();
            ElevatorAction depart = history.get(history.size()-2);
            energyUsed += elevator.energyUse(
                    floor * building.getFloorHeight() - depart.floor() * building.getFloorHeight(),
                    previousContentWeight());
        }
    }

    /**
     * Récupère les dernières actions depuis l'historique.
     *
     * @param countActions Nombre d'action à récupérer.
     */
    public List<ElevatorAction> getLastActions(int countActions) {
        ArrayList<ElevatorAction> actions = new ArrayList<>();

        for (int i=history.size()-1; i>= history.size()-countActions && i>=0; i--) {
            actions.add(history.get(i));
        }

        return actions;
    }

    public List<ElevatorAction> getActions() { return new ArrayList<>(history); }

    public void saveAsJson(String pathDir) {
        File dir = new File(pathDir);
        int newNum = 0;

        for (File file : dir.listFiles()) {
            // Récupération des numéros des fichiers
            String name = file.getName();
            String numStr = name.replace("elevator_", "").replace(".json", "");
            try {
                int num = Integer.parseUnsignedInt(numStr);
                if (num >= newNum) newNum = num+1;
            }
            catch (NumberFormatException e) { }
        }

        JsonManager.writeJson(pathDir+"/elevator_"+newNum+".json", history);
    }

    /** Renvoie l'énergie total utilisé par l'ascenseur. */
    public double energyUsed() {
        Building building = Building.getInstance();
        ElevatorController controller = elevator.getController();

        if (history.size() == 0) return 0;

        ElevatorAction lastAct = history.get(history.size() -1);
        // Cas ou l'ascenseur est en plein trajet
        if (lastAct.act() == DEPART) {
            return energyUsed + elevator.energyUse(
                    controller.estimatePosition() - lastAct.floor() * building.getFloorHeight(),
                    previousContentWeight());
        }

        return energyUsed;

    }

    private double previousContentWeight() {
        double weight = 0;
        for (Resident resident : previousContent) weight += resident.getWeight();
        return weight;
    }
}
