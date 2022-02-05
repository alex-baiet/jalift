package fr.elevator.projetelevator.model.json;

import fr.elevator.projetelevator.model.Helper;

import java.util.ArrayList;

public class Names {
    private static Names instance;

    private ArrayList<String> firstNames;
    private ArrayList<String> lastNames;

    public Names() {
        if (instance != null) throw new IllegalStateException("Une instance de Names existe déjà.");
        instance = this;
    }

    public static Names getInstance() {
        if (instance == null) JsonManager.readJson("config/names.json", Names.class);
        return instance;
    }

    /** Renvoie un prénom au hasard. */
    public String getRandomFirstName() {
        return firstNames.get(Helper.random.nextInt(firstNames.size()));
    }

    /** Renvoie un nom au hasard. */
    public String getRandomLastName() {
        return lastNames.get(Helper.random.nextInt(lastNames.size()));
    }

    public ArrayList<String> getFirstNames() { return new ArrayList<>(firstNames); }
    public void setFirstNames(ArrayList<String> firstNames) { this.firstNames = new ArrayList<>(firstNames); }
    public ArrayList<String> getLastNames() { return new ArrayList<>(lastNames); }
    public void setLastNames(ArrayList<String> lastNames) { this.lastNames = new ArrayList<>(lastNames); }

    @Override
    public String toString() {
        return "Names{" +
                "firstNames=" + firstNames +
                ", lastNames=" + lastNames +
                '}';
    }
}
