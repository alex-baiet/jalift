package fr.elevator.projetelevator.view.part.side;

import fr.elevator.projetelevator.model.elevator.Elevator;
import fr.elevator.projetelevator.model.elevator.ElevatorAction;
import fr.elevator.projetelevator.model.elevator.ElevatorActionChoice;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

public class ElevatorInfosView extends InfosViewAbstract {
    private final Elevator elevator;

    // Toutes les affichages de valeurs
    private final Label labTitle;
    private final Label labFullWeight;
    private final Label labContentWeight;
    private final Label labContentMaxWeight;
    private final Label labElevatorWeight;
    private final Label labResidentCount;
    private final Label labYPos;
    private final Label labSpeed;
    private final Label labAcceleration;
    private final Label labMaxSpeed;
    private final Label labStopDuration;
    private final Label labEnergyUsed;
    private final Label labEnergyCost;
    private final Label labAlphaEnergyRecovery;
    private final Label labHistory;
    private final Button btnSave;

    public ElevatorInfosView(Elevator elevator) {
        this.elevator = elevator;

        labTitle = new Label("Ascenseur n°" + elevator.getId());
        labFullWeight = new Label();
        labContentWeight = new Label();
        labContentMaxWeight = new Label("- contenu max : " + elevator.getMaxContentWeight() + "kg");
        labElevatorWeight = new Label("- ascenseur : " + elevator.getWeight() + "kg");
        labResidentCount = new Label();
        labYPos = new Label();
        labSpeed = new Label();
        labAcceleration = new Label("- acceleration : " + elevator.getAcceleration());
        labMaxSpeed = new Label("- vitesse max : " + elevator.getAcceleration());
        labStopDuration = new Label("temps d'arrêt : " + elevator.getStopDuration());
        labEnergyUsed = new Label();
        labEnergyCost = new Label("- coût : " + elevator.getEnergyCost() + "J/m");
        labAlphaEnergyRecovery = new Label("- ratio récup. : " + elevator.getAlphaEnergyRecovery());
        labHistory = new Label();
        btnSave = new Button("Sauvegarder");
        btnSave.setOnMouseClicked(event -> elevator.getElevatorHistory().saveAsJson("out/elevators"));

        getView().getChildren().addAll(
                labTitle,
                labFullWeight,
                labContentWeight,
                labContentMaxWeight,
                labElevatorWeight,
                labResidentCount,
                labYPos,
                labSpeed,
                labAcceleration,
                labMaxSpeed,
                labStopDuration,
                labEnergyUsed,
                labEnergyCost,
                labAlphaEnergyRecovery,
                labHistory,
                btnSave
        );

        update();
    }

    @Override
    public void update() {
        labFullWeight.setText("Poids : " + (elevator.getWeight() + elevator.getContentWeight()) + "kg");
        labContentWeight.setText("- contenu : " + elevator.getContentWeight() + "kg");
        labResidentCount.setText("Résidents nbr : " + elevator.getResidentCount());
        labYPos.setText(String.format("Position y : %.2f", elevator.getController().estimatePosition()));
        labSpeed.setText(String.format("Vitesse : %.2fm/s", elevator.getController().estimateSpeed()));
        labEnergyUsed.setText(String.format("Energie cons. : %.1fJ", elevator.getElevatorHistory().energyUsed()));

        // Définition du texte pour l'historique
        List<ElevatorAction> lastActions = elevator.getElevatorHistory().getLastActions(2);
        StringBuilder builder = new StringBuilder("\n________\nHistorique");
        for (ElevatorAction act : lastActions) {
            builder
                    .append("\n"+act.act())
                    .append(String.format("\n instant : %f", act.instant()))
                    .append("\n étage : " + act.floor())
                    .append("\n nb résident : " + act.nbResident());
        }
        labHistory.setText(builder.toString());
    }
}
