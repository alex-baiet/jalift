package fr.elevator.projetelevator.model;

/** Permet la mesure du temps pour le simulateur. */
public class Timer {
    /** Début de mesure du temps. */
    private static long lastUpdate = -1;
    /** Mesure du temps actuel. */
    private static double elapsedTime;
    /** Vitesse d'écoulement du temps */
    private static double timeSpeed = 1;
    /** Indique que le Timer est actif ou non. */
    private static boolean isPlaying = false;

    static {
        // Ce bloc s'exécute à l'initialisation de la classe statique, avant l'exécution du main() du projet.
        restart();
    }

    /** Juste une fonction de test. */
    public static void test() {
        System.out.println("Test Timer");
        setTimeSpeed(5f);
        try { Thread.sleep(1000); }
        catch (InterruptedException e) { e.printStackTrace(); }
        setTimeSpeed(2f);
        try { Thread.sleep(1000); }
        catch (InterruptedException e) { e.printStackTrace(); }

        System.out.println("time = " + getTime());
    }

    public static void play() {
        if (lastUpdate == -1) restart();
        isPlaying = true;
    }

    public static void pause() {
        isPlaying = false;
    }

    /** Recommence de zéro la mesure du temps. */
    public static void restart() {
        restart(1f);
    }

    /** Recommence de zéro la mesure du temps, avec comme vitesse d'avancement du temps le paramètre donné. */
    public static void restart(double timeSpeed) {
        setTimeSpeed(timeSpeed);
        lastUpdate = System.currentTimeMillis();
        elapsedTime = 0;
        isPlaying = true;
    }

    /** Met à jour le Timer. */
    public static void updateTime() {
        if (isPlaying) {
            elapsedTime += (System.currentTimeMillis() - lastUpdate) / 1000.0 * timeSpeed;
        }
        lastUpdate = System.currentTimeMillis();
    }

    public static boolean isPlaying() { return isPlaying; }

    /** Renvoie le temps écoulé en secondes depuis le dernier appel de updateTime(). */
    public static double getTime() {
        return elapsedTime;
    }

    public static double getTimeSpeed() { return timeSpeed; }

    /** Change la vitesse d'ecoulement du temps */
    public static void setTimeSpeed(double newSpeed) {
        if (newSpeed < 0) throw new IllegalArgumentException("\"newSpeed\" doit être > 0. Valeur actuelle : "+newSpeed);
        if (newSpeed > 100000) {
            System.out.println("Valeur de la nouvelle vitesse trop élevé : Modification de la valeur annulé.");
            return;
        }
        timeSpeed = newSpeed;
    }
}
