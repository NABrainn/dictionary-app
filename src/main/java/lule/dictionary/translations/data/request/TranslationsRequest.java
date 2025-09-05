package lule.dictionary.translations.data.request;

public sealed interface TranslationsRequest permits AddTranslationRequest, DeleteSourceWordRequest, FindByTargetWordRequest, UpdateSourceWordsRequest {
}
