package lule.dictionary.service.libreTranslate.dto;

public record TranslateResponse(String translatedText) {

    @Override
    public String translatedText() {
        if(translatedText == null) {
            return "";
        }
        return translatedText.replaceAll("-", " ");
    }
}
