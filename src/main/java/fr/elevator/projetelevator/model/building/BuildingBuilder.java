package fr.elevator.projetelevator.model.building;

import fr.elevator.projetelevator.model.json.Range;
import fr.elevator.projetelevator.model.elevator.Elevator;

import java.util.ArrayList;

/** Permet de construire plus facilement un Building. */
public class BuildingBuilder {
    private int floorCount;
    private double floorHeight;
    private int residentCount;
    private final ArrayList<Range> enterRanges = new ArrayList<>();
    private final ArrayList<Range> exitRanges = new ArrayList<>();
    private final ArrayList<Elevator> elevators = new ArrayList<>();

    public void setFloorCount(int floorCount) { this.floorCount = floorCount; }
    public void setFloorHeight(double floorHeight) { this.floorHeight = floorHeight; }
    public void setResidentCount(int residentCount) { this.residentCount = residentCount; }
    public void addEnterRange(Range enterRange) { this.enterRanges.add(enterRange); }
    public void addExitRange(Range exitRange) { this.exitRanges.add(exitRange); }
    public void addElevator(Elevator elevator) { this.elevators.add(elevator); }

    public Building build(){
        return new Building(
                floorCount,
                floorHeight,
                residentCount,
                enterRanges,
                exitRanges,
                elevators
        );
    }
}
