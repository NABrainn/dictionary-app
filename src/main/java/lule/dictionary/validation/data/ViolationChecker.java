package lule.dictionary.validation.data;

@FunctionalInterface
public interface ViolationChecker {
    boolean run();
}
