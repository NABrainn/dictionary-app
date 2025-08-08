package lule.dictionary.service.translateAPI.libreTranslate.dto;

public record TranslateResponse(String translatedText) {

    @Override
    public String translatedText() {
        if(translatedText == null) {
            return "";
        }
        return translatedText.replace("-", " ");
    }
}
