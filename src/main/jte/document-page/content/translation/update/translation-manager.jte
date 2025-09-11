@import lule.dictionary.translations.data.TranslationLocalizationKey
@import lule.dictionary.translations.data.attribute.TranslationAttribute
@import java.util.Map

@param TranslationAttribute attribute
@param Map<TranslationLocalizationKey, String> translationLocalization

<div id="source-words-list">
    @template.document-page.content.translation.source-words-list(
        sourceWords = attribute.translation().sourceWords(),
        targetWord = attribute.translation().targetWord(),
        isUpdateForm = true,
        selectedWordId = attribute.selectedWordId())
</div>
<div class="flex flex-col gap-2">
    <div id="source-word-form" class="flex">
        @template.document-page.content.translation.update.update-source-words-form(
        placeholderText = translationLocalization.get(TranslationLocalizationKey.ADD_TRANSLATION),
        translationAttribute = attribute)
    </div>
</div>