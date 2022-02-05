package fr.elevator.projetelevator.model.resident;

import fr.elevator.projetelevator.model.Helper;
import fr.elevator.projetelevator.model.Timer;
import fr.elevator.projetelevator.model.json.JsonManager;

import java.io.File;
import java.util.*;
import static fr.elevator.projetelevator.model.resident.ResidentActionChoice.*;

public class ResidentHistory {
    private final ArrayList<ResidentAction> history = new ArrayList<>();

    public ResidentHistory() { }

    /** Ajoute une action à l'historique. */
    public void addAction(ResidentActionChoice act, int floor){
        history.add(new ResidentAction(act, floor, Timer.getTime()));
    }

    /**
     * Récupère les dernières actions depuis l'historique.
     *
     * @param countActions Nombre d'action à récupérer.
     */
    public List<ResidentAction> getLastActions(int countActions) {
        ArrayList<ResidentAction> actions = new ArrayList<>();
        ArrayList<ResidentAction> allActions = new ArrayList(history);

        for (int i=allActions.size()-1; i>=allActions.size()-countActions && i>=0; i--) {
            actions.add(allActions.get(i));
        }

        return actions;
    }

    public List<ResidentAction> getActions() { return new ArrayList<>(history); }

    /** Sauvegarde l'historique dans le dossier indiqué, sans écraser les anciens fichiers existants. */
    public void saveAsJson(String pathDir) {
        File dir = new File(pathDir);
        int newNum = 0;

        for (File file : dir.listFiles()) {
            // Récupération des numéros des fichiers
            String name = file.getName();
            String numStr = name.replace("resident_", "").replace(".json", "");
            try {
                int num = Integer.parseUnsignedInt(numStr);
                if (num >= newNum) newNum = num+1;
            }
            catch (NumberFormatException e) { }

        }

        JsonManager.writeJson(pathDir+"/resident_"+newNum+".json", history);
    }

    /** Renvoie tous les temps d'attentes entre deux actions d'un Résident. */
    private List<Double> getPeriods(ResidentActionChoice start, ResidentActionChoice end) {
        double instantPresent = 0;
        boolean started = false;
        ArrayList<Double> periods = new ArrayList<>();

        for (ResidentAction act : history) {
            if (act.act() == start) {
                instantPresent = act.instant();
                started = true;
            }
            if (act.act() == end) {
                    periods.add(act.instant() - instantPresent);
                    started = false;
            }
        }

        // Cas ou un résident attend mais n'est pas encore entré dans l'ascenseur
        if (started) {
            periods.add(Timer.getTime() - instantPresent);
        }

        return periods;
    }

    /**
     * Renvoie le temps d'attente moyen du Resident.
     * @return -1 si le résident n'a jamais fait l'attente.
     */
    public double waitAvg() {
        List<Double> periods = getPeriods(PRESENTATION, EMBARK);
        if (periods.size() == 0) return -1;
        return Helper.avg(periods);
    }

    /** Renvoie le temps d'attente maximum du Resident. */
    public double waitMax() {
        List<Double> periods = getPeriods(PRESENTATION, EMBARK);
        if (periods.size() == 0) return 0;
        return Collections.max(periods);
    }

    /**
     * Renvoie le temps de trajet moyen.
     * @return -1 si le résident n'a jamais fait l'attente.
     */
    public double travelAvg() {
        List<Double> periods = getPeriods(PRESENTATION, UNLOAD);
        if (periods.size() == 0) return -1;
        return Helper.avg(periods);
    }

    /** Renvoie le temps de trajet maximum. */
    public double travelMax() {
        List<Double> periods = getPeriods(PRESENTATION, UNLOAD);
        if (periods.size() == 0) return 0;
        return Collections.max(periods);
    }

    /**
     * Renvoie le temps dans l'ascenseur moyen par voyage.
     * @return -1 si le résident n'a jamais fait l'attente.
     */
    public double inElevatorAvg() {
        List<Double> periods = getPeriods(EMBARK, UNLOAD);
        if (periods.size() == 0) return -1;
        return Helper.avg(periods);
    }

    /** Renvoie le temps en ascenseur maximum lors d'un voyage. */
    public double inElevatorMax() {
        List<Double> periods = getPeriods(EMBARK, UNLOAD);
        if (periods.size() == 0) return 0;
        return Collections.max(periods);
    }

}
