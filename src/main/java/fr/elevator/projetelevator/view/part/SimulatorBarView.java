package fr.elevator.projetelevator.view.part;

import fr.elevator.projetelevator.MainSimulator;
import fr.elevator.projetelevator.controller.SimulatorController;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/** Gère la vue de la barre d'options du simulateur. */
public class SimulatorBarView {
    private HBox bar;

    /** Initialise la barre d'outils. */
    public SimulatorBarView() {
        SimulatorController controller = SimulatorController.getInstance();

        Label labTimeSpeed = controller.getLabTimeSpeed();
        HBox boxSpeed = new HBox(controller.getButLessSpeed(), controller.getButMoreSpeed(), labTimeSpeed);

        // Positionnement du label
        labTimeSpeed.setPrefWidth(200);
        labTimeSpeed.setAlignment(Pos.CENTER_LEFT);
        labTimeSpeed.prefHeightProperty().bind(boxSpeed.heightProperty());

        // Initialisation de la bar contenant tous les boutons
        bar = new HBox(controller.getButPlay(), boxSpeed);
        bar.setSpacing(20);
    }

    public HBox getBar() { return bar; }

}
