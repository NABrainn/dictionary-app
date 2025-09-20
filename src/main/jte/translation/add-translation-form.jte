@import lule.dictionary.controllerAdvice.data.BaseAttribute
@import lule.dictionary.translations.data.TranslationLocalizationKey
@import lule.dictionary.translations.data.attribute.TranslationAttribute
@import java.util.Map

@param BaseAttribute baseAttribute 
@param TranslationAttribute attribute
@param Map<String, String> errors
@param Map<TranslationLocalizationKey, String> messages

<div
hx-on::load="formPositioner.handleOverflow(htmx.find('#translation-form-container-${attribute.id()}'), 3)"
hx-on:click="event.stopPropagation()"
class="
md:gap-0.75
bg-tertiary flex flex-col p-3 rounded animate-fade-10 gap-4"
id="translation-form">
    <div class="flex justify-end">
        <span
        class="md:text-md
        text-sm text-red-400 font-bold cursor-pointer"
        hx-on:click="
        event.stopPropagation()
        documentPage.closeAllTranslationForms()">❌</span>
    </div>
    <span class="font-bold text-lg text-accent text-wrap">${attribute.translation().targetWord()}</span>
    <div class="flex flex-col gap-2 text-wrap">
        @if(attribute.translation().sourceWords().isEmpty())
            <input
            id="no-source-words"
            type="hidden"
            name="sourceWords"
            value="">
        @endif
        @for(String word : attribute.translation().sourceWords())
            <div
            class="flex gap-2 items-center min-h-8">
                <input
                name="sourceWords"
                type="hidden"
                value="${word}">
                <span class="block text-center text-accent text-wrap">${word}</span>
            </div>
        @endfor
        <div class="flex flex-col gap-2">
            <div id="source-word-form" class="flex">
                <form
                class="flex flex-col gap-2"
                hx-params="id,targetWord,familiarity,sourceLanguage,targetLanguage,documentId,sourceWords,isPhrase,_csrf"
                hx-vals='
                {
                    "id": "${attribute.id()}",
                    "targetWord": "${attribute.translation().targetWord()}",
                    "familiarity": "UNKNOWN",
                    "sourceLanguage": "${attribute.translation().sourceLanguage()}",
                    "targetLanguage": "${attribute.translation().targetLanguage()}",
                    "documentId": "${attribute.documentId()}",
                    "_csrf": "${baseAttribute._csrf().getToken()}",
                    "isPhrase": "${attribute.isPhrase()}"
                }'
                hx-post="/translations/new"
                hx-target="#translation-form-container-${attribute.id()}"
                hx-swap="innerHTML"
                hx-on::before-request="
                if(htmx.find('#no-source-words') === null) {
                    documentPage.handlePersistWord('${attribute.translation().targetWord()}', 'unknown')
                }">
                    @for(String word : attribute.translation().sourceWords())
                        <input
                        name="sourceWords"
                        type="hidden"
                        value="${word}">
                    @endfor
                    <div class="flex gap-2">
                        <input
                        id="input-source-word"
                        class="
                        max-w-50 outline-none border border-2 ${errors.get("sourceWord") != null ? "border-error" : "border-primary"} rounded text-neutral text-md grow-1
                        focus:border-accent"
                        type="text"
                        placeholder=""
                        name="sourceWords">
                        <button
                        id="submit-source-word"
                        class="text-3xl cursor-pointer text-accent"
                        type="submit">+</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div class="
    md:flex md:flex-col md:gap-0.25
    flex flex-row justify-center gap-3">
        @for(var level : attribute.familiarityLevels().entrySet())
            <form
            hx-on::before-request="
            if(htmx.find('#no-source-words') === null) {
                documentPage.handlePersistWord('${attribute.translation().targetWord()}', '${level.getValue().name().toLowerCase()}')
            }"
            class="
            md:bg-tertiary md:text-accent md:size-auto md:gap-1 md:cursor-normal md:justify-start
            cursor-pointer bg-accent text-primary flex gap-4 hover:text-primary font-bold rounded size-8  justify-center items-center"
            id="translation-form-${attribute.id()}"
            hx-trigger="click"
            hx-post="/translations/new"
            hx-params="targetWord,familiarity,sourceLanguage,targetLanguage,id,isPhrase,sourceWords,documentId,_csrf"
            hx-vals='{
                "targetWord": "${attribute.translation().targetWord()}",
                "familiarity": "${level.getValue().name()}",
                "sourceLanguage": "${attribute.translation().sourceLanguage().name()}",
                "targetLanguage": "${attribute.translation().targetLanguage().name()}",
                "documentId": "${attribute.documentId()}",
                "id": "${attribute.id()}",
                "isPhrase": "${attribute.isPhrase()}",
                "_csrf": "${baseAttribute._csrf().getToken()}"
            }'
            hx-target="closest #translation-form-container-${attribute.id()}"
            hx-swap="innerHTML">
                @if(attribute.translation().sourceWords().isEmpty())
                    <input
                    id="no-source-words"
                    type="hidden"
                    name="sourceWords"
                    value="">
                @endif
                @for(var word : attribute.translation().sourceWords())
                    <input type="hidden" name="sourceWords" value="${word}">
                @endfor
                <span>${level.getKey()}<span class="md:inline hidden">: </span></span>
                <input
                class="md:inline
                hidden cursor-pointer"
                value="${messages.get(TranslationLocalizationKey.valueOf(level.getValue().name().toUpperCase()))}"
                type="submit">
            </form>
        @endfor
    </div>
</div>
