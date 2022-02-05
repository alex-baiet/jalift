package fr.elevator.projetelevator.model.json;

import java.util.ArrayList;

/** Objet de configuration d'un Building. */
public class BuildingJson {
    private int floorCount;
    private double floorHeight;
    private int residentCount;
    private ArrayList<ElevatorJson> elevators;
    private ArrayList<Range> exitRanges;
    private ArrayList<Range> enterRanges;

    public int getFloorCount() { return floorCount; }
    public void setFloorCount(int floorCount) { this.floorCount = floorCount; }
    public double getFloorHeight() { return floorHeight; }
    public void setFloorHeight(double floorHeight) { this.floorHeight = floorHeight; }
    public int getResidentCount() { return residentCount; }
    public void setResidentCount(int residentCount) { this.residentCount = residentCount; }
    public ArrayList<ElevatorJson> getElevators() { return elevators; }
    public void setElevators(ArrayList<ElevatorJson> elevators) { this.elevators = elevators; }
    public ArrayList<Range> getExitRanges() { return exitRanges; }
    public void setExitRanges(ArrayList<Range> exitRanges) { this.exitRanges = exitRanges; }
    public ArrayList<Range> getEnterRanges() { return enterRanges; }
    public void setEnterRanges(ArrayList<Range> enterRanges) { this.enterRanges = enterRanges; }

    public static void main(String[] args) {
        BuildingJson data = new BuildingJson();
        ArrayList<ElevatorJson> elevators = new ArrayList<>();
        elevators.add(new ElevatorJson());
        elevators.add(new ElevatorJson());
        data.setElevators(elevators);

        JsonManager.writeJson("config/building.json", data);
    }
}

