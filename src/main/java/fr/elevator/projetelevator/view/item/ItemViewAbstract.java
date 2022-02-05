package fr.elevator.projetelevator.view.item;

public abstract class ItemViewAbstract implements ItemView {

    private static ItemView lastSelectedItem = null;

    protected ItemView getLastSelectedItem() { return lastSelectedItem; }

    /** Déselectionne (visuellement uniquement) le dernier élément sélectionné. */
    public static void stopHighlight() {
        if (lastSelectedItem != null) lastSelectedItem.deselectHighlight();
    }

    @Override
    public void selectHighlight() {
        if (lastSelectedItem != null) {
            lastSelectedItem.deselectHighlight();
        }
        lastSelectedItem = this;
    }

    @Override
    public void deselectHighlight() {
        if (lastSelectedItem == this) {
            lastSelectedItem = null;
        }
    }
}
