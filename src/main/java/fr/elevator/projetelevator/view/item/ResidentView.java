package fr.elevator.projetelevator.view.item;

import fr.elevator.projetelevator.model.Helper;
import fr.elevator.projetelevator.model.resident.Resident;
import fr.elevator.projetelevator.view.SimulatorView;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/** Objet de gestion de la vue d'un resident spécifique. */
public class ResidentView extends ItemViewAbstract {
    private final Resident resident;

    private final double height = 1.8;
    private final double width = 0.9;
    private Pane parent = null;

    private final Rectangle view;
    private final Color color;
    private boolean isNameShown = false;
    private final Label labName;

    public ResidentView(Resident resident) {
        this.resident = resident;
        double pixelPerMeter = SimulatorView.pixelPerMeter();
        view = new Rectangle(width * pixelPerMeter, height * pixelPerMeter);

        // Définition d'une couleur au hasard pour pouvoir les différencier
        while (true) {
            double r = Helper.random.nextDouble();
            double g = Helper.random.nextDouble();
            double b = Helper.random.nextDouble();
            if (r+g+b > 2.6) {
                // La couleur est trop claire et difficile à voir, on la change
                continue;
            }
            color = new Color(r, g, b, 1.0);
            view.setFill(color);
            break;
        }

        // Préparation des borders pour la sélection
        view.setStrokeType(StrokeType.OUTSIDE);
        view.setStrokeWidth(3);

        // Préparation du label pour le nom
        labName = new Label(resident.getFirstName() + " " + resident.getLastName());
        view.setOnMouseEntered(event -> displayName());
        view.setOnMouseExited(event -> hideName());
    }

    @Override
    public void setPosition(double x, double y) {
        view.setX(x);
        view.setY(y);

        double xFinal = x + view.getWidth()/2 - labName.getWidth()/2;
        labName.setTranslateX(xFinal < 0 ? 0 : xFinal);
        labName.setTranslateY(y - labName.getHeight());
    }

    @Override
    public void show(Pane pane) {
        if (parent == pane) return;
        if (parent != null) remove();
        parent = pane;

        pane.getChildren().add(view);
    }

    @Override
    public void remove() {
        if (parent == null) return;
        parent.getChildren().remove(view);

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
    public void setOnMouseClicked(EventHandler<? super MouseEvent> eventHandler) {
        view.setOnMouseClicked(eventHandler);
    }

    /** Affiche sur la fenêtre le nom du résident. */
    public void displayName() {
        if (isNameShown) return;
        isNameShown = true;

        parent.getChildren().add(labName);
    }

    /** Cache l'affichage du nom du résident. */
    public void hideName() {
        if (!isNameShown) return;
        isNameShown = false;

        parent.getChildren().remove(labName);
    }
}
