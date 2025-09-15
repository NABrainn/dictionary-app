package lule.dictionary.validation.data;

import lombok.NonNull;

public record Constraint(@NonNull String name,
                         @NonNull ValidationRunner validationRunner,
                         @NonNull String message) {

    public static Constraint define(String name, ValidationRunner validationRunner, String message) {
        return new Constraint(name, validationRunner, message);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) {
            return false;
        }
        if(o instanceof Constraint constraint) {
            return this.name().equals(constraint.name());
        }
        return false;
    }
}
