package lule.dictionary.result.data;

public record Ok<T>(T value) implements Result<T> {
    public static <T> Ok<T> of(T value) {
        return new Ok<>(value);
    }
    public static <T> Ok<T> empty() {
        return new Ok<>(null);
    }
    @Override
    public T value() {
        if(value == null) {
            throw new NullPointerException("Result is empty");
        }
        return value;
    }
}
