package fr.elevator.projetelevator.view.item;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/** Affichage d'un objet de la simulation. */
public interface ItemView {
    /** Change la position d'affichage. */
    void setPosition(double x, double y);

    /** Affiche l'objet dans la fenêtre. */
    void show(Pane pane);

    /** Retire l'objet de la fenêtre. */
    void remove();

    /** Change la mise en forme de l'objet pour montrer qu'il est sélectionné. */
    void selectHighlight();

    /** Retire la mise en forme pour la sélection. */
    void deselectHighlight();

    /** Change l'action lorsque l'objet est cliqué. */
    void setOnMouseClicked(EventHandler<? super MouseEvent> eventHandler);
}
