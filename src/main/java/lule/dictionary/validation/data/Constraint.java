package lule.dictionary.validation.data;

import lombok.NonNull;

public record Constraint(@NonNull String name,
                         @NonNull ViolationChecker violationChecker,
                         @NonNull String message) {

    public static Constraint of(String name, ViolationChecker violationChecker, String message) {
        return new Constraint(name, violationChecker, message);
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
