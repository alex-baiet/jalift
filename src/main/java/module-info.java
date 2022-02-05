module fr.elevator.projetelevator {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    exports fr.elevator.projetelevator;
    exports fr.elevator.projetelevator.controller;
    exports fr.elevator.projetelevator.model;
    exports fr.elevator.projetelevator.model.resident;
    exports fr.elevator.projetelevator.model.elevator;
    exports fr.elevator.projetelevator.model.json;
    exports fr.elevator.projetelevator.view;
    exports fr.elevator.projetelevator.view.item;
    exports fr.elevator.projetelevator.view.part;
    exports fr.elevator.projetelevator.view.part.side;

    opens fr.elevator.projetelevator.controller to javafx.fxml;
    opens fr.elevator.projetelevator.model.json to com.fasterxml.jackson.databind;
}