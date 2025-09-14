@import lule.dictionary.translations.data.TranslationLocalizationKey
@import lule.dictionary.translations.data.attribute.TranslationAttribute
@import java.util.Map

@param TranslationAttribute attribute
@param Map<TranslationLocalizationKey, String> localization
@param String _csrf

<div id="source-words-list">
    @template.document-page.content.translation.source-words-list(
        sourceWords = attribute.translation().sourceWords(),
        targetWord = attribute.translation().targetWord(),
        isUpdateForm = false,
        selectedWordId = attribute.selectedWordId()
    )
</div>
<div class="flex flex-col gap-2">
    <div id="source-word-form" class="flex">
        @template.document-page.content.translation.add.add-source-words-form(
        _csrf = _csrf,
        placeholderText = localization.get(TranslationLocalizationKey.ADD_TRANSLATION),
        sourceLanguage = attribute.translation().sourceLanguage().name(),
        targetLanguage = attribute.translation().targetLanguage().name(),
        documentId = attribute.documentId(),
        selectedWordId = attribute.selectedWordId(),
        sourceWords = attribute.translation().sourceWords(),
        targetWord = attribute.translation().targetWord())
    </div>
</div>