package fr.elevator.projetelevator.view.item;

import fr.elevator.projetelevator.model.building.Building;
import fr.elevator.projetelevator.view.SimulatorView;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class ElevatorView extends ItemViewAbstract {

    private final double width = 2.0;
    private final double height;

    private Pane parent = null;

    private final Rectangle view;
    private final Label residentCounter;
    private final Color color = Color.BLACK;

    /** Créer une nouvelle vue pour un ascenseur. */
    public ElevatorView() {
        double pixelPerMeter = SimulatorView.pixelPerMeter();
        Building building = Building.getInstance();

        height = pixelPerMeter * building.getFloorHeight();

        // Initialisation de l'ascenseur
        view = new Rectangle(width * pixelPerMeter, height);
        view.setFill(color);
        view.setStrokeWidth(4);
        view.setStrokeType(StrokeType.OUTSIDE);

        // Initialisation du compteur de l'ascenseur
        residentCounter = new Label("?");
        residentCounter.setTextFill(Color.WHITE);
        residentCounter.setAlignment(Pos.CENTER);
        residentCounter.setPrefWidth(width * pixelPerMeter);
        residentCounter.setPrefHeight(height);
    }

    @Override
    public void show(Pane pane) {
        if (parent == pane) return;
        if (parent != null) remove();
        parent = pane;

        ObservableList<Node> children = pane.getChildren();
        children.add(view);
        children.add(residentCounter);
    }

    @Override
    public void remove() {
        if (parent == null) return;

        ObservableList<Node> children = parent.getChildren();
        children.remove(view);
        children.remove(residentCounter);

        parent = null;
    }

    @Override
    public void selectHighlight() {
        super.selectHighlight();
        view.setStroke(new Color(color.getRed(), color.getGreen(), color.getBlue(), 0.5));
    }

    @Override
    public void deselectHighlight() {
        super.deselectHighlight();
        view.setStroke(new Color(0, 0, 0, 0));
    }

    @Override
    public void setPosition(double x, double y) {
        // Positionnement de la forme de l'ascenseur
        view.setX(x);
        view.setY(y);

        // Positionnement du compteur de l'ascenseur
        residentCounter.setTranslateX(x);
        residentCounter.setTranslateY(y);
    }

    /** Change la valeur afficher par le compteur. */
    public void setCounterValue(int value) {
        residentCounter.setText(Integer.toString(value));
    }

    @Override
    public void setOnMouseClicked(EventHandler<? super MouseEvent> eventHandler) {
        view.setOnMouseClicked(eventHandler);
        residentCounter.setOnMouseClicked(eventHandler);
    }

    public double getWidth() { return width; }
    public double getHeight() { return height; }
}
