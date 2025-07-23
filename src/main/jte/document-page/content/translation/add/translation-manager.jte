@import lule.dictionary.service.translation.dto.attribute.TranslationAttribute
@import java.util.Map

@param TranslationAttribute translationAttribute
@param Map<String, String> translationLocalization

<div id="source-words-list">
    @template.document-page.content.translation.source-words-list(
        sourceWords = translationAttribute.translation().sourceWords(),
        targetWord = translationAttribute.translation().targetWord(),
        isUpdateForm = false,
        selectedWordId = translationAttribute.selectedWordId()
    )
</div>
<div class="flex flex-col gap-2">
    <div id="source-word-form" class="flex">
        @template.document-page.content.translation.add.add-source-words-form(
            placeholderText = translationLocalization.get("add_translation"),
            sourceLanguage = translationAttribute.translation().sourceLanguage().name(),
            targetLanguage = translationAttribute.translation().targetLanguage().name(),
            importId = translationAttribute.importId(),
            selectedWordId = translationAttribute.selectedWordId(),
            sourceWords = translationAttribute.translation().sourceWords(),
            targetWord = translationAttribute.translation().targetWord()
        )
    </div>
    @template.document-page.content.translation.ai-btn(
        text = translationLocalization.get("ai_translation"),
        targetWord = translationAttribute.translation().targetWord()
    )
</div>