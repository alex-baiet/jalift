package fr.elevator.projetelevator.view.part.side;

import fr.elevator.projetelevator.view.SimulatorView;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public abstract class InfosViewAbstract implements InfosView {
    private final VBox view;

    private Button btnShowStats;

    private boolean shown;

    public InfosViewAbstract() {
        btnShowStats = new Button("Afficher les statistiques");
        btnShowStats.setOnMouseClicked(event -> SimulatorView.getInstance().getSide().displayStatsInfos());
        view = new VBox(btnShowStats);
    }

    /** Ajoute les informations à l'affichage. */
    public void show(Pane pane) {
        if (!shown) {
            pane.getChildren().add(view);
            shown = true;
        }
    }

    /** Retire les informations de l'affichage. */
    public void remove(Pane pane) {
        if (shown) {
            pane.getChildren().remove(view);
        }
    }

    /** Pane contenant tous les éléments à afficher. */
    protected VBox getView() { return view; }

}
