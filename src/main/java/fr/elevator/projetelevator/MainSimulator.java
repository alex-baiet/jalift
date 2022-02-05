package fr.elevator.projetelevator;

import fr.elevator.projetelevator.controller.SimulatorController;
import fr.elevator.projetelevator.model.building.Building;
import fr.elevator.projetelevator.model.json.BuildingJson;
import fr.elevator.projetelevator.model.json.JsonManager;
import fr.elevator.projetelevator.view.SimulatorView;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainSimulator extends Application {
    /** Layout principal de l'application. */
    private static final BorderPane root = new BorderPane();
    private static Stage stage;

    /** Contient la boucle du simulateur. */
    private AnimationTimer loop;

    public static void main(String[] args) { launch(); }

    @Override
    public void start(Stage stage) {
        MainSimulator.stage = stage;
        // Création du node principale
        Scene scene = new Scene(root, 640, 480  );
        stage.setTitle("Ascensueur");
        stage.setScene(scene);

        SimulatorController controller = SimulatorController.getInstance();

        // Chargement du Building depuis le fichier de configuration
        BuildingJson buildingJson = JsonManager.readJson("config/building.json", BuildingJson.class);
        new Building(buildingJson);

        SimulatorView.createInstance();

        controller.prepareOnClick();

        // Définition de la boucle infini + Exécution de la boucle
        loop = new AnimationTimer() {
            @Override
            public void handle(long l) { controller.loopAction(l); }
        };

        loop.start();

        test();
    }

    /** Fait des trucs de tests. */
    private static void test() {

    }

    public static Stage getStage() { return stage; }
    public static BorderPane getRoot() { return root; }
}