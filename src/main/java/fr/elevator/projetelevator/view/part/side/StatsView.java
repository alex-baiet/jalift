package fr.elevator.projetelevator.view.part.side;

import fr.elevator.projetelevator.model.Helper;
import fr.elevator.projetelevator.model.building.Building;
import fr.elevator.projetelevator.model.elevator.Elevator;
import fr.elevator.projetelevator.model.resident.Resident;
import fr.elevator.projetelevator.model.resident.ResidentHistory;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class StatsView extends InfosViewAbstract {

    private final Label labTravelAvg;
    private final Label labTravelMax;
    private final Label labWaitAvg;
    private final Label labWaitMax;
    private final Label labInElevatorAvg;
    private final Label labInElevatorMax;
    private final Label labEnergyUsed;

    public StatsView() {
        labTravelAvg = new Label();
        labTravelMax = new Label();
        labWaitAvg = new Label();
        labWaitMax = new Label();
        labInElevatorAvg = new Label();
        labInElevatorMax = new Label();
        labEnergyUsed = new Label();

        getView().getChildren().addAll(
                labTravelAvg,
                labTravelMax,
                labWaitAvg,
                labWaitMax,
                labInElevatorAvg,
                labInElevatorMax,
                labEnergyUsed
        );
    }

    @Override
    public void show(Pane pane) {
        super.show(pane);
    }

    @Override
    public void remove(Pane pane) {
        super.remove(pane);
    }

    @Override
    public void update() {
        updateTravelAvg();
        updateTravelMax();
        updateWaitAvg();
        updateWaitMax();
        updateInElevatorAvg();
        updateInElevatorMax();
        updateEnergyUse();
    }

    private void updateTravelAvg() {
        ArrayList<Double> averages = new ArrayList<>();

        for (int id = 0; id < Resident.countCreatedResident(); id++) {
            ResidentHistory history = Resident.getResidentById(id).getHistory();

            // Calcul temps d'attente moyen de chaque résident
            double res = history.travelAvg();
            if (res != -1) averages.add(res);
        }

        // Calcul du temps moyen général
        double finalAvg = Helper.avg(averages);

        labTravelAvg.setText(String.format("Trajet moyenne : %.2fs", finalAvg));
    }

    private void updateTravelMax() {
        double max = 0;

        for (int id = 0; id < Resident.countCreatedResident(); id++) {
            ResidentHistory history = Resident.getResidentById(id).getHistory();

            // Calcul temps d'attente moyen de chaque résident
            double res = history.travelMax();
            if (res > max) max = res;
        }

        labTravelMax.setText(String.format("Trajet max : %.2fs", max));
    }

    private void updateWaitAvg() {
        ArrayList<Double> averages = new ArrayList<>();

        for (int id = 0; id < Resident.countCreatedResident(); id++) {
            ResidentHistory history = Resident.getResidentById(id).getHistory();

            // Calcul temps d'attente moyen de chaque résident
            double res = history.waitAvg();
            if (res != -1) averages.add(res);
        }

        // Calcul du temps moyen général
        double finalAvg = Helper.avg(averages);

        labWaitAvg.setText(String.format("Attente moyenne : %.2fs", finalAvg));
    }

    private void updateWaitMax() {
        double max = 0;

        for (int id = 0; id < Resident.countCreatedResident(); id++) {
            ResidentHistory history = Resident.getResidentById(id).getHistory();

            // Calcul temps d'attente moyen de chaque résident
            double res = history.waitMax();
            if (res > max) max = res;
        }

        labWaitMax.setText(String.format("Attente max : %.2fs", max));
    }

    private void updateInElevatorAvg() {
        ArrayList<Double> averages = new ArrayList<>();

        for (int id = 0; id < Resident.countCreatedResident(); id++) {
            ResidentHistory history = Resident.getResidentById(id).getHistory();

            // Calcul temps d'attente moyen de chaque résident
            double res = history.inElevatorAvg();
            if (res != -1) averages.add(res);
        }

        // Calcul du temps moyen général
        double finalAvg = Helper.avg(averages);

        labInElevatorAvg.setText(String.format("Dans l'ascen. moy. : %.2fs", finalAvg));
    }

    private void updateInElevatorMax() {
        double max = 0;

        for (int id = 0; id < Resident.countCreatedResident(); id++) {
            ResidentHistory history = Resident.getResidentById(id).getHistory();

            // Calcul temps d'attente moyen de chaque résident
            double res = history.inElevatorMax();
            if (res > max) max = res;
        }

        labInElevatorMax.setText(String.format("Dans l'ascen. max : %.2fs", max));
    }

    private void updateEnergyUse() {
        double sum = 0;

        for (Elevator elevator : Building.getInstance().getElevators()) {
            sum += elevator.getElevatorHistory().energyUsed();
        }

        labEnergyUsed.setText(String.format("Energie utilisé : %.1fJ", sum));
    }

}
