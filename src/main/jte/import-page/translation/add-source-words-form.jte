@import java.util.List

@param String targetWord
@param String sourceLanguage
@param String targetLanguage
@param int importId
@param int selectedWordId
@param int page
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
    "selectedWordId": "${selectedWordId}",
    "page": "${page}"
}'
hx-post="/translations/new"
hx-target="#page-content"
hx-swap="outerHTML"
hx-on::before-request="document.querySelector('source-word-field').isEmpty()">
    <div id="source-words-list">
        @template.import-page.translation.source-words-list(sourceWords =  sourceWords, targetWord = targetWord, update = false)
    </div>
    <div class="flex gap-2">
        <input
        id="input-source-word"
        class="
        outline-none border border-2 border-primary rounded text-neutral text-sm
        focus:border-accent"
        type="text"
        placeholder="Custom translation"
        name="sourceWords">
        <button
        id="submit-source-word"
        class="text-2xl cursor-pointer"
        type="submit">+</button>
    </div>
</form>