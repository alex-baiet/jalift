package fr.elevator.projetelevator.model.json;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/** Indique une période délimité par deux dates. */
@JsonSerialize
public record Range(double start, double end) {
    public Range {
        if (start > end) throw new IllegalArgumentException("start doit être inférieur à end.");
    }

    @Override
    public double start() {
        return start;
    }

    @Override
    public double end() {
        return end;
    }
}
