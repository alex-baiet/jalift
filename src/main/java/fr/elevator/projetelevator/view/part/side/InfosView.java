package fr.elevator.projetelevator.view.part.side;

import javafx.scene.layout.Pane;

public interface InfosView {
    /** Ajoute les informations à l'affichage. */
    void show(Pane pane);

    /** Retire les informations de l'affichage. */
    void remove(Pane pane);

    /** Met à jour les informations. */
    void update();
}
