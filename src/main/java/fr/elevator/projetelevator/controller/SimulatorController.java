package fr.elevator.projetelevator.controller;

import fr.elevator.projetelevator.model.building.Building;
import fr.elevator.projetelevator.model.elevator.Elevator;
import fr.elevator.projetelevator.model.Timer;
import fr.elevator.projetelevator.model.resident.Resident;
import fr.elevator.projetelevator.view.SimulatorView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 * Gère toutes les interactions utilisateurs / applications
 * et les actions dans la boucle principale.
 */
public class SimulatorController {
    private static SimulatorController instance = null;

    /** Bouton play/pause de la simulation. */
    private final Button butPlay;

    /** Affichage de la vitesse du temps. */
    private final Label labTimeSpeed;
    /** Bouton pour réduire la vitesse. */
    private final Button butLessSpeed;
    /** Bouton pour augmenter la vitesse .*/
    private final Button butMoreSpeed;
    /** Bouton permettant de sauvegarder l'historique des gens */
    private final Button butSaveHistory;
    private final Button butTest;

    private SimulatorController() {
        instance = this;

        butPlay = new Button("Pause");
        butPlay.setOnMouseClicked(event -> onPlayPauseClick(event, butPlay));

        labTimeSpeed = new Label();
        updateLabelTimeSpeed(labTimeSpeed);

        butLessSpeed = new Button("-");
        butLessSpeed.setOnMouseClicked(event -> onLessSpeedClick(event, labTimeSpeed));

        butMoreSpeed = new Button("+");
        butMoreSpeed.setOnMouseClicked(event -> onMoreSpeedClick(event, labTimeSpeed));

        butSaveHistory = new Button("Sauvegarder");
        butSaveHistory.setOnMouseClicked(SimulatorController::onSaveHistoryClick);

        butTest = new Button("Test");
        butTest.setOnMouseClicked(SimulatorController::onTestClick);
    }

    public static SimulatorController getInstance() {
        if (instance == null) new SimulatorController();
        return instance;
    }

    /** Action exécutée à chaque frame dans le MainSimulator. */
    public void loopAction(long l) {
        Timer.updateTime();
        Building building = Building.getInstance();

        // Mis à jour de la simulation complète
        building.updateQueues();

        for (int i = 0; i<building.elevatorCount(); i++) {
            Elevator elevator = building.getElevator(i);
            elevator.getController().update();
        }

        // Mis à jour de la View
        SimulatorView view = SimulatorView.getInstance();
        view.update();
    }

    /** Met à jour l'affichage de la vitesse d'écoulement du temps. */
    private static void updateLabelTimeSpeed(Label lab) {
        lab.setText(String.format("Vitesse temps : x%.2f", Timer.getTimeSpeed()));
    }

    /** Prépare les handlers pour différents objets. */
    public void prepareOnClick() {
        prepareResidentsOnClick();
        prepareElevatorOnClick();
    }

    /** Prepare les onClick des différents Residents. */
    private void prepareResidentsOnClick() {
        for (int i = 0; i < Resident.countCreatedResident(); i++) {
            Resident resident = Resident.getResidentById(i);
            SimulatorView.getInstance().getResidentView(resident).setOnMouseClicked(event -> {
                onResidentClick(event, resident);
            });
        }
    }

    /** Prepare les onClick des différents Residents. */
    private void prepareElevatorOnClick() {
        Building building = Building.getInstance();
        for (int i = 0; i < building.elevatorCount(); i++) {
            Elevator elevator = building.getElevator(i);
            SimulatorView.getInstance().getElevatorView(elevator).setOnMouseClicked(event -> {
                onElevatorClick(event, elevator);
            });
        }
    }

    private static void onPlayPauseClick(MouseEvent event, Button clickedBtn) {
        if (Timer.isPlaying()) {
            Timer.pause();
            clickedBtn.setText("Play");
            System.out.println("La simulation est maintenant en pause.");
        }
        else {
            Timer.play();
            clickedBtn.setText("Pause");
            System.out.println("La simulation a repris.");
        }
    }

    private static void onLessSpeedClick(MouseEvent event, Label labTimeSpeed) {
        Timer.setTimeSpeed(Timer.getTimeSpeed() / 2);
        updateLabelTimeSpeed(labTimeSpeed);
        System.out.println("Vitesse de simulation actuelle : "+Timer.getTimeSpeed());
    }

    private static void onMoreSpeedClick(MouseEvent event, Label labTimeSpeed) {
        Timer.setTimeSpeed(Timer.getTimeSpeed() * 2);
        updateLabelTimeSpeed(labTimeSpeed);
        System.out.println("Vitesse de simulation actuelle : "+Timer.getTimeSpeed());
    }

    private static void onSaveHistoryClick(MouseEvent event) {
        System.out.println("Sauvegarde de l'historique non implémenté.");
    }

    private static void onResidentClick(MouseEvent event, Resident resident) {
        SimulatorView view = SimulatorView.getInstance();
        view.displayResidentInfos(resident);
        view.getResidentView(resident).selectHighlight();
    }

    private static void onElevatorClick(MouseEvent event, Elevator elevator) {
        SimulatorView view = SimulatorView.getInstance();
        view.displayElevatorInfos(elevator);
        view.getElevatorView(elevator).selectHighlight();
    }

    private static void onTestClick(MouseEvent event) {
        SimulatorView.getInstance().getSide().displayResidentInfos(Resident.getResidentById(0));
    }

    public Button getButPlay() { return butPlay; }
    public Label getLabTimeSpeed() { return labTimeSpeed; }
    public Button getButLessSpeed() { return butLessSpeed; }
    public Button getButMoreSpeed() { return butMoreSpeed; }
    public Button getButSaveHistory() { return butSaveHistory; }
    public Button getButTest() { return butTest; }

}
