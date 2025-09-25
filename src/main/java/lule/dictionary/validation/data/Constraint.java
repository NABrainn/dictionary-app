package lule.dictionary.validation.data;

import lombok.NonNull;
import lule.dictionary.validation.data.rule.Rule;

public record Constraint(@NonNull String name,
                         @NonNull Rule rule,
                         @NonNull String message) {

    public static Constraint of(String name, Rule rule, String message) {
        return new Constraint(name, rule, message);
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
