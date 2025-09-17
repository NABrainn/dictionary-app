package lule.dictionary.translations.data.request;

public sealed interface TranslationsRequest permits AddTranslationRequest, DeleteSourceWordRequest, FindTranslationFormRequest, UpdateSourceWordsRequest {
}
