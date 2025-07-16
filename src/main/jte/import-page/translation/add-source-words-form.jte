@import lule.dictionary.service.imports.importService.dto.FormPositionData
@import lule.dictionary.service.translation.dto.attribute.TranslationPair

@param String sourceLanguage
@param String targetLanguage
@param int importId
@param int selectedWordId
@param int page
@param TranslationPair translationPair
@param FormPositionData formPositionData 

<form
class="flex flex-col gap-2"
hx-vals='
{
    "targetWord": "${translationPair.targetWord()}",
    "familiarity": "UNKNOWN",
    "sourceLanguage": "${sourceLanguage}",
    "targetLanguage": "${targetLanguage}",
    "importId": "${importId}",
    "selectedWordId": "${selectedWordId}",
    "page": "${page}"
}'
hx-post="/translations/new"
hx-target="#page-content"
hx-swap="outerHTML">
    <div id="source-words-list">
        @template.import-page.translation.source-words-list(translationPair = translationPair, isUpdateForm = false)
    </div>
    <div class="flex gap-2">
        <input
        id="input-source-word"
        class="
        outline-none border border-2 border-primary rounded text-neutral text-sm max-w-35
        focus:border-accent"
        type="text"
        placeholder="Custom translation"
        name="sourceWords">
        <button
        id="submit-source-word"
        class="text-2xl cursor-pointer"
        type="submit">+</button>
    </div>
    @if(formPositionData != null)
        <input type="hidden" name="left" class="left-${selectedWordId}" value="${selectedWordId}">
        <input type="hidden" name="right" class="right-${selectedWordId}" value="${selectedWordId}">
        <input type="hidden" name="top" class="top-${selectedWordId}" value="${selectedWordId}">
        <input type="hidden" name="bottom" class="bottom-${selectedWordId}" value="${selectedWordId}">
    @else
        <input type="hidden" name="left" class="left-${selectedWordId}">
        <input type="hidden" name="right" class="right-${selectedWordId}">
        <input type="hidden" name="top" class="top-${selectedWordId}">
        <input type="hidden" name="bottom" class="bottom-${selectedWordId}">
    @endif
</form>