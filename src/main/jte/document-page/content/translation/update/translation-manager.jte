@import lule.dictionary.translations.data.attribute.TranslationAttribute
@import java.util.Map

@param TranslationAttribute translationAttribute
@param Map<String, String> translationLocalization

<div id="source-words-list">
    @template.document-page.content.translation.source-words-list(
        sourceWords = translationAttribute.translation().sourceWords(),
        targetWord = translationAttribute.translation().targetWord(),
        isUpdateForm = true,
        selectedWordId = translationAttribute.selectedWordId())
</div>
<div class="flex flex-col gap-2">
    <div id="source-word-form" class="flex">
        @template.document-page.content.translation.update.update-source-words-form(
        placeholderText = translationLocalization.get("add_translation"),
        translationAttribute = translationAttribute)
    </div>
</div>