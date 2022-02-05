package fr.elevator.projetelevator.view.part.side;

import fr.elevator.projetelevator.model.resident.Resident;
import fr.elevator.projetelevator.model.resident.ResidentAction;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.util.List;

public class ResidentInfosView extends InfosViewAbstract {
    private Resident resident;
    // Tous les composants d'affichage
    private final Label labTitle;
    private final Label labName;
    private final Label labWeight;
    private final Label labHomeFloor;
    private final Label labPositionFloor;
    private final Label labDestinationFloor;
    private final Label labHistory;
    private final Button btnSave;

    public ResidentInfosView(Resident resident) {
        this.resident = resident;

        labTitle = new Label("Resident n°" + resident.getId());
        labName = new Label(resident.getFirstName() + " " + resident.getLastName());
        labWeight = new Label("Poids : " + resident.getWeight());
        labHomeFloor = new Label("Etage domicile : " + resident.getHomeFloor());
        labPositionFloor = new Label();
        labDestinationFloor = new Label();
        labHistory = new Label();
        btnSave = new Button("Sauvegarder");
        btnSave.setOnMouseClicked(event -> resident.getHistory().saveAsJson("out/residents"));

        getView().getChildren().addAll(
                labTitle,
                labName,
                labWeight,
                labHomeFloor,
                labPositionFloor,
                labDestinationFloor,
                labHistory,
                btnSave
        );

        update();
    }

    @Override
    public void remove(Pane pane) {
        super.remove(pane);
    }

    /** Met à jour les informations du Resident. */
    @Override
    public void update() {
        int pos = resident.getPositionFloor();
        int destination = resident.getDestinationFloor();

        if (resident.isInsideElevator()) labPositionFloor.setText("Est dans un ascenseur");
        else labPositionFloor.setText("Position : étage " + pos);

        if (pos == destination) labDestinationFloor.setText("Destination : aucun");
        else labDestinationFloor.setText("destination : " + destination);

        // Affichage historique
        List<ResidentAction> lastActions = resident.getHistory().getLastActions(3);
        StringBuilder builder = new StringBuilder("________\nHistorique");
        for (ResidentAction act : lastActions) {
            builder
                    .append("\n"+act.act())
                    .append(String.format("\n - temps : %f", act.instant()))
                    .append("\n - étage : "+act.floor());
        }
        builder.append("\n________");
        labHistory.setText(builder.toString());

    }
}
