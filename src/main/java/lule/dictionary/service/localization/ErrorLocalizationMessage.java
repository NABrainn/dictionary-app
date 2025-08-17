package lule.dictionary.service.localization;

public class ErrorLocalizationMessage {
    public enum DocumentForm {
        INVALID_TITLE_LENGTH("invalid_title_length"),
        EMPTY_TITLE("empty_title"),

        EMPTY_CONTENT("empty_content"),
        INVALID_CONTENT_LENGTH("invalid_content_length"),

        EMPTY_URL("empty_url"),
        INVALID_URL_LENGTH("invalid_url_length"),
        INVALID_URL_FORMAT("invalid_url_format");

        DocumentForm(String code) {}
    }
}
