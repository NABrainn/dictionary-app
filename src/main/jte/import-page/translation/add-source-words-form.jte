@import java.util.List

@param String targetWord
@param String sourceLanguage
@param String targetLanguage
@param int importId
@param int selectedWordId
@param List<String> sourceWords

<form
class="flex flex-col gap-2"
hx-vals='
{
    "targetWord": "${targetWord}",
    "familiarity": "UNKNOWN",
    "sourceLanguage": "${sourceLanguage}",
    "targetLanguage": "${targetLanguage}",
    "importId": "${importId}",
    "selectedWordId": "${selectedWordId}"
}'
hx-post="/translations/new"
hx-target="#page-content"
hx-swap="outerHTML">
    <div id="source-word-container" class="flex flex-col">
        @template.import-page.translation.source-words-list(sourceWords =  sourceWords, targetWord = targetWord)
    </div>
    <div class="flex gap-2">
        <input
        id="input-source-word"
        class="
        outline-none border border-2 border-primary rounded text-neutral text-sm
        focus:border-accent"
        type="text" placeholder="Custom translation"
        name="sourceWords">
        <button
        id="submit-source-word"
        class="text-2xl cursor-pointer"
        type="submit">+</button>
    </div>
</form>