package lule.dictionary.service.imports.importService.dto;

public record Word(String value) implements Selectable {
    public static Word of(String value) {
        return new Word(value);
    }
}
