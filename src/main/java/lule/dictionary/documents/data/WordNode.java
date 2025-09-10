package lule.dictionary.documents.data;

import lombok.NonNull;

public record WordNode(int id,
                       @NonNull String formattedText,
                       @NonNull String renderedText) {
    public static WordNode of(int id, String formattedText, String renderedText) {
        return new WordNode(id, formattedText, renderedText);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof WordNode iterable)) return false;
        return this.id == iterable.id();
    }

    public WordNode withRenderedText(String renderedText) {
        return new WordNode(this.id(), this.formattedText(), renderedText);
    }
}
