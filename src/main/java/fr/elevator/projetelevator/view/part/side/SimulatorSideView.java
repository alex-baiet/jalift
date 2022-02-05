package fr.elevator.projetelevator.view.part.side;

import fr.elevator.projetelevator.model.elevator.Elevator;
import fr.elevator.projetelevator.model.resident.Resident;
import fr.elevator.projetelevator.view.item.ItemViewAbstract;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/** Affichage des informations affichés sur le côté de la fenêtre. */
public class SimulatorSideView {
    /** Largeur de la boite */
    private final double width = 240;

    private final VBox box;
    /** Contient l'affichage concernant les informations d'un resident. */
    private InfosView infos;

    public SimulatorSideView() {
        box = new VBox();
        box.setPrefWidth(width);
        box.setBackground(new Background(new BackgroundFill(new Color(0.8, 1, 0.8, 1), null, null)));
        box.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, BorderWidths.DEFAULT)));

        // Ajout de la vue des statistiques par défaut
        displayStatsInfos();
    }

    /** Affiche plus d'informations sur le Resident cliqué. */
    public void displayResidentInfos(Resident resident) {
        // Suppression de l'ancien affichage et ajout du nouveau
        if (infos != null) infos.remove(box);
        infos = new ResidentInfosView(resident);
        infos.show(box);

        update();
    }

    /** Affiche plus d'informations sur l'Elevator cliqué. */
    public void displayElevatorInfos(Elevator elevator) {
        // Suppression de l'ancien affichage et ajout du nouveau
        if (infos != null) infos.remove(box);
        infos = new ElevatorInfosView(elevator);
        infos.show(box);

        update();
    }

    public void displayStatsInfos() {
        // Suppression de l'ancien affichage et ajout du nouveau
        if (infos != null) {
            infos.remove(box);
        }
        ItemViewAbstract.stopHighlight();
        infos = new StatsView();
        infos.show(box);

        update();
    }

    /** Met à jour le composant. */
    public void update() {
        if (infos != null) infos.update();
    }

    public VBox getBox() { return box; }

}
