@import lule.dictionary.service.imports.importService.dto.FormPositionData
@import lule.dictionary.service.translation.dto.attribute.TranslationPair

@param String placeholderText
@param String sourceLanguage
@param String targetLanguage
@param int selectedWordId
@param int importId
@param TranslationPair translationPair

<form
class="flex flex-col gap-2"
hx-vals='
{
    "targetWord": "${translationPair.targetWord()}",
    "familiarity": "UNKNOWN",
    "sourceLanguage": "${sourceLanguage}",
    "targetLanguage": "${targetLanguage}".
    "importId": "${importId}"
}'
hx-post="/translations/new"
hx-target="#translation-form-container-${selectedWordId}"
hx-swap="innerHTML">
    @for(String word : translationPair.sourceWords())
        <input
        name="sourceWords"
        type="hidden"
        value="${word}">
    @endfor
    <div class="flex gap-2">
        <input
        id="input-source-word"
        class="
        outline-none border border-2 border-primary rounded text-neutral text-md max-w-35
        focus:border-accent"
        type="text"
        placeholder="${placeholderText}"
        name="sourceWords">
        <button
        id="submit-source-word"
        class="text-3xl cursor-pointer text-accent"
        type="submit">+</button>
    </div>
</form>