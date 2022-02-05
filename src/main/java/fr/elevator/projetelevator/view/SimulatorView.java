package fr.elevator.projetelevator.view;

import fr.elevator.projetelevator.MainSimulator;
import fr.elevator.projetelevator.controller.SimulatorController;
import fr.elevator.projetelevator.model.building.Building;
import fr.elevator.projetelevator.model.elevator.Elevator;
import fr.elevator.projetelevator.model.Floor;
import fr.elevator.projetelevator.model.Timer;
import fr.elevator.projetelevator.model.resident.Resident;
import fr.elevator.projetelevator.view.item.ElevatorView;
import fr.elevator.projetelevator.view.item.ResidentView;
import fr.elevator.projetelevator.view.part.SimulatorBarView;
import fr.elevator.projetelevator.view.part.side.SimulatorSideView;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class SimulatorView {
    private static SimulatorView instance;

    private final Pane display;
    private final SimulatorBarView bar;
    private final SimulatorSideView side;

    /** Nombre de pixel par mètre. */
    private static double pixelPerMeter = 12;

    // Valeurs pour l'ascenseur
    private final double ELEVATOR_DISTANCE = 3.0;
    private final ArrayList<ElevatorView> elevatorViews = new ArrayList<>();
    private final double allElevatorsWidth;

    // Valeurs d'affichages des résidents
    /** Les vue des résidents, par Identifiant. */
    private final ArrayList<ResidentView> residentViews;
    private final double residentHeight = 1.8;
    private final double residentDistance = 1.2;

    /** Affichage des étages. */
    private final ArrayList<Line> floorsView = new ArrayList<>();

    /** Liste des compteurs de résidents par étages. */
    private final ArrayList<Label> floorsCounter = new ArrayList<>();

    private Label timeView;

    public static void createInstance() {
        if (instance != null) throw new IllegalStateException("Une instance existe déjà.");
        instance = new SimulatorView();
    }

    public static SimulatorView getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Pas d'instance défini.");
        }
        return instance;
    }

    public static double pixelPerMeter() { return pixelPerMeter; }

    /**
     * Initialise la view et tous les objets à rendre visible.
     * Nécessite d'avoir déjà initialisé un Building et le Controller.
     */
    private SimulatorView() {
        display = new Pane();
        MainSimulator.getRoot().setCenter(display);
        Building building = Building.getInstance();
        BorderPane root = MainSimulator.getRoot();

        MainSimulator.getStage().show();

        // Initialisation de la barre des boutons
        bar = new SimulatorBarView();
        root.setTop(bar.getBar());

        // Initialisation de la barre des informations complémentaires
        side = new SimulatorSideView();
        root.setRight(side.getBox());

        // Création des vues d'ascenseurs
        for (int i = 0; i < building.elevatorCount(); i++) {
            ElevatorView elevatorView = new ElevatorView();
            elevatorView.show(display);
            elevatorViews.add(elevatorView);
        }
        updateElevators();
        allElevatorsWidth = elevatorViews.size() * ELEVATOR_DISTANCE * pixelPerMeter;

        for (int i = 0; i < building.getFloorCount(); i++) {
            // Initisalisation affichage des étages
            Line line = new Line(0, 0, display.getWidth(), 0);
            floorsView.add(line);
            display.getChildren().add(line);

            // Initialisation des compteurs de résidents par étages
            Label label = new Label("???");
            label.setTranslateY(display.getHeight() - (i+1) * building.getFloorHeight() * pixelPerMeter);
            floorsCounter.add(label);
            display.getChildren().add(label);
        }

        // Initialisation des résidents
        residentViews = new ArrayList<>();
        int countResident = Resident.countCreatedResident();
        for (int i = 0; i < countResident; i++) {
            residentViews.add(new ResidentView(Resident.getResidentById(i)));
        }

        // Initialisation de l'affichage du temps.
        timeView = new Label("Heure : ??h ??m ??s");
        display.getChildren().add(timeView);

    }

    /** Met à jour l'affichage de l'application. */
    public void update() {
        double time = Timer.getTime();

        updateElevators();
        updateFloors();
        updateFloorsCounter();
        updateResidents();
        updateTime();

        side.update();
    }

    /** Met à jour l'affichage de l'ascenseur */
    private void updateElevators() {
        Building building = Building.getInstance();

        for (int i = 0; i < building.elevatorCount(); i++) {
            Elevator elevator = building.getElevator(i);
            ElevatorView elevatorView = elevatorViews.get(i);

            double xPos = display.getWidth() - (i+1) * ELEVATOR_DISTANCE * pixelPerMeter;
            double yPos = elevator.getController().estimatePosition();
            // Position en pixels sur l'écran
            yPos = display.getHeight() - elevatorView.getHeight() - yPos * pixelPerMeter;

            elevatorView.setPosition(xPos, yPos);

            elevatorView.setCounterValue(elevator.getResidentCount());
        }
    }

    /** Met à jour l'affichage des étages. */
    private void updateFloors() {
        Building building = Building.getInstance();

        for (int i = 0; i < floorsView.size(); i++) {
            double yPos = display.getHeight() - building.getFloorHeight() * pixelPerMeter * (i+1);
            Line floor = floorsView.get(i);
            floor.setTranslateY(yPos);
            floor.setEndX(display.getWidth());
        }

    }

    /** Met à jour l'affichage des compteurs de residents par étages. */
    private void updateFloorsCounter() {
        Building building = Building.getInstance();
        double elevatorHeight = pixelPerMeter * building.getFloorHeight();
        double elevatorY = display.getHeight() - elevatorHeight;

        Floor floor;
        Label counter;
        for (int i = 0; i < floorsCounter.size(); i++) {
            floor = building.getFloor(i);
            counter = floorsCounter.get(i);
            counter.setText(""+floor.residentCount());

            counter.setTranslateY(display.getHeight() - (i+1) * building.getFloorHeight() * pixelPerMeter);
        }
    }

    /** Met à jour l'affichage des gens hors des files d'attentes. */
    private void updateResidents() {
        Building building = Building.getInstance();

        for (int floorIndex = 0; floorIndex < building.getFloorCount(); floorIndex++) {
            Floor floor = building.getFloor(floorIndex);

            // Affichage des residents dans les queues
            ArrayDeque<Resident> queue = floor.getQueue();
            int residentIndex = 0;
            for (Resident resident : queue) {
                residentIndex++;

                ResidentView view = residentViews.get(resident.getId());
                view.show(display);

                double yPos = display.getHeight() - (floorIndex * building.getFloorHeight() + residentHeight) * pixelPerMeter;
                double xPos = display.getWidth() - allElevatorsWidth - (residentIndex+1) * residentDistance * pixelPerMeter;
                view.setPosition(xPos, yPos);

            }

            // Affichage des residents hors des queues
            int residentCount = floor.residentHomeCount();
            residentIndex = 0;
            for (residentIndex = 0; residentIndex < residentCount; residentIndex++) {
                Resident resident = floor.getResidentHome(residentIndex);
                ResidentView view = residentViews.get(resident.getId());
                view.show(display);

                double yPos = display.getHeight() - (floorIndex * building.getFloorHeight() + residentHeight) * pixelPerMeter;
                double xPos = (residentIndex + 1) * residentDistance * pixelPerMeter;
                view.setPosition(xPos, yPos);
            }
        }

        // Cacher les résidents dans les ascenseurs
        List<Elevator> elevators = building.getElevators();
        for (Elevator elevator : elevators) {
            List<Resident> residents = elevator.getResidents();
            for (Resident resident : residents) {
                ResidentView view = residentViews.get(resident.getId());
                view.remove();
            }
        }
    }

    /** Met à jour l'affichage du temps. */
    private void updateTime() {
        double time = Timer.getTime();
        int seconds = (int)time % 60;
        int minutes = (int)(time/60)%60;
        int hours = (int)(time/3600);

        timeView.setText(String.format("Heure : %dh %dm %ds", hours, minutes, seconds));
    }

    public void displayResidentInfos(Resident resident) { side.displayResidentInfos(resident); }

    public void displayElevatorInfos(Elevator elevator) { side.displayElevatorInfos(elevator); }

    public Pane getDisplay() { return display; }
    public SimulatorBarView getBar() { return bar; }
    public SimulatorSideView getSide() { return side; }

    /** Renvoie la vue correspondant au Resident indiqué. */
    public ResidentView getResidentView(Resident resident) { return residentViews.get(resident.getId()); }
    public ElevatorView getElevatorView(Elevator elevator) { return elevatorViews.get(elevator.getId()); }
}
