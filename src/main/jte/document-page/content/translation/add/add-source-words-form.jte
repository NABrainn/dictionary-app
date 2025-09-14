@import java.util.List

@param String placeholderText
@param String sourceLanguage
@param String targetLanguage
@param int selectedWordId
@param int documentId
@param List<String> sourceWords
@param String targetWord
@param String _csrf

<form
class="flex flex-col gap-2"
hx-params="selectedWordId,targetWord,familiarity,sourceLanguage,targetLanguage,documentId,sourceWords,_csrf"
hx-vals='
{
    "selectedWordId": "${selectedWordId}",
    "targetWord": "${targetWord}",
    "familiarity": "UNKNOWN",
    "sourceLanguage": "${sourceLanguage}",
    "targetLanguage": "${targetLanguage}",
    "documentId": "${documentId}",
    "_csrf": "${_csrf}"
}'
hx-post="/translations/new"
hx-target="#translation-form-container-${selectedWordId}"
hx-swap="innerHTML">
    @for(String word : sourceWords)
        <input
        name="sourceWords"
        type="hidden"
        value="${word}">
    @endfor
    <div class="flex gap-2">
        <input
        id="input-source-word"
        class="
        outline-none border border-2 border-primary rounded text-neutral text-md grow-1
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