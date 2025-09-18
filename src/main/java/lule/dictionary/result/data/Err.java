package lule.dictionary.result.data;

public record Err<T>(Throwable throwable) implements Result<T> {
    public static <T> Err<T> of(Throwable throwable) {
        return new Err<>(throwable);
    }
}
