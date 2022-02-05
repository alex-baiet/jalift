package fr.elevator.projetelevator.model;

import java.util.Collection;
import java.util.Random;

/** Contient des fonctions d'aide de tous types. */
public class Helper {
    public static final Random random = new Random();

    private Helper() { }

    /** Calcule la distance parcourue selon l'accélération et la durée. */
    public static double travelAcceleration(double acceleration, double duration) {
        return acceleration * Math.pow(duration, 2) / 2;
    }

    /** Retourne une heure sous forme d'une valeur de type double. */
    public static double timeToValue(int hour, int minutes, double seconds) {
        return hour * 3600 + minutes * 60 + seconds;
    }

    /** Renvoie la moyenne de la collection. */
    public static double avg(Collection<Double> values) {
        if (values.size() == 0) return 0;
        return sum(values) / values.size();
    }

    /** Renvoie la somme de la collection. */
    public static double sum(Collection<Double> values) {
        if (values.size() == 0) return 0;

        double total = 0;
        for (Double waitTime : values) {
            total += waitTime;
        }

        return total;
    }

}
