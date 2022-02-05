package fr.elevator.projetelevator.model.json;

/** Objet de configuration d'un Elevator. */
public class ElevatorJson {
    private double maxContentWeight;
    private double acceleration;
    private double maxSpeed;
    private double stopDuration;
    private String ai;
    private double weight;
    private double energyCost;
    private double alphaEnergyRecovery;
    private int floorPosition;

    public double getAcceleration() { return acceleration; }
    public void setAcceleration(double acceleration) { this.acceleration = acceleration; }
    public double getMaxSpeed() { return maxSpeed; }
    public void setMaxSpeed(double maxSpeed) { this.maxSpeed = maxSpeed; }
    public double getStopDuration() { return stopDuration; }
    public void setStopDuration(double stopDuration) { this.stopDuration = stopDuration; }
    public double getMaxContentWeight() { return maxContentWeight; }
    public void setMaxContentWeight(double maxContentWeight) { this.maxContentWeight = maxContentWeight; }
    public String getAi() { return ai; }
    public void setAi(String ai) { this.ai = ai; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    public double getEnergyCost() { return energyCost; }
    public void setEnergyCost(double energyCost) { this.energyCost = energyCost; }
    public double getAlphaEnergyRecovery() { return alphaEnergyRecovery; }
    public void setAlphaEnergyRecovery(double alphaEnergyRecovery) { this.alphaEnergyRecovery = alphaEnergyRecovery; }
    public int getFloorPosition() { return floorPosition; }
    public void setFloorPosition(int floorPosition) { this.floorPosition = floorPosition; }

}
