@import lule.dictionary.controllerAdvice.data.BaseAttribute
@import lule.dictionary.translations.data.TranslationLocalizationKey
@import lule.dictionary.translations.data.attribute.TranslationAttribute
@import java.util.Map

@param BaseAttribute baseAttribute
@param TranslationAttribute attribute
@param Map<String, String> errors
@param Map<TranslationLocalizationKey, String> messages

<div
class="
md:gap-0.75
gap-4 bg-tertiary flex flex-col p-3 rounded animate-fade-10"
id="translation-form">
    <div class="flex justify-end">
        <span
        class="md:text-md
        text-sm text-red-400 font-bold cursor-pointer"
        hx-on:click="
        event.preventDefault()
        documentPage.closeAllTranslationForms()">❌</span>
    </div>
    <span class="font-bold text-lg text-accent text-wrap">${attribute.translation().targetWord()}</span>
    <div id="translation-manager">
        <div id="source-words-list">
            <div class="flex flex-col gap-2 text-wrap">
                @for(String word : attribute.translation().sourceWords())
                    <div
                    class="flex gap-2 items-center min-h-8">
                        <input
                        name="sourceWords"
                        type="hidden"
                        value="${word}">
                        <span class="block text-center text-accent text-wrap">${word}</span>
                        <form
                        class="flex items-center"
                        hx-delete="/translations/sourceWords/delete"
                        hx-target="#translation-form-container-${attribute.id()}"
                        hx-swap="innerHTML"
                        hx-params="sourceWord,targetWord,id,_csrf"
                        hx-vals='{
                            "targetWord": "${attribute.translation().targetWord()}",
                            "id": "${attribute.id()}",
                            "sourceWord": "${word}",
                            "_csrf": "${baseAttribute._csrf().getToken()}"
                        }'>
                            <button
                            class="cursor-pointer text-md block"
                            type="submit">🗑️</button>
                        </form>
                    </div>
                @endfor
            </div>
        </div>
        <div class="flex flex-col gap-2">
            <div id="source-word-form" class="flex">
                <form
                class="flex flex-col gap-2"
                hx-params="id,targetWord,familiarity,sourceWords,_csrf"
                hx-vals='{
                    "targetWord": "${attribute.translation().targetWord()}",
                    "familiarity": "${attribute.translation().familiarity()}",
                    "id": "${attribute.id()}",
                    "_csrf": "${baseAttribute._csrf().getToken()}"
                }'
                hx-put="/translations/sourceWords/update"
                hx-target="#translation-form-container-${attribute.id()}"
                hx-swap="innerHTML">
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
                        outline-none border border-2 ${errors.get("sourceWord") != null ? "border-error" : "border-primary"} rounded text-neutral text-md grow-1
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
    flex flex-row justify-around">
        @for(var level : attribute.familiarityLevels().entrySet())
            @if(level.getKey() == attribute.currentFamiliarity())
                <div class="
                flex flex-row md:bg-tertiary md:text-primary md:size-auto md:justify-start
                font-bold bg-primary text-accent gap-1  size-8 rounded justify-center items-center">
                    <span class="
                    md:inline
                    block">${level.getKey()}<span class="md:inline hidden">: </span></span>
                    <span class="
                    md:inline
                    hidden">${messages.get(TranslationLocalizationKey.valueOf(level.getValue().name().toUpperCase()))}</span>
                </div>
            @else
            <form
            class="
            md:bg-tertiary md:text-accent md:size-auto md:gap-1 md:cursor-normal md:justify-start
            cursor-pointer bg-accent text-primary flex gap-4 hover:text-primary font-bold rounded size-8  justify-center items-center"
            id="translation-form-${attribute.id()}"
            hx-trigger="click"
            hx-put="/translations/familiarity/update"
            hx-params="targetWord,familiarity,sourceLanguage,targetLanguage,id,isPhrase,_csrf"
            hx-vals='
            {
                "targetWord": "${attribute.translation().targetWord().toLowerCase()}",
                "familiarity": "${level.getValue().name()}",
                "sourceLanguage": "${attribute.translation().sourceLanguage().name()}",
                "targetLanguage": "${attribute.translation().targetLanguage().name()}",
                "id": "${attribute.id()}",
                "isPhrase": "${attribute.isPhrase()}",
                "_csrf": "${baseAttribute._csrf().getToken()}"
            }'
            hx-target="#translation-form-container-${attribute.id()}"
            hx-swap="innerHTML"
            hx-on:click="
            event.preventDefault()
            documentPage.handlePersistWord('${attribute.translation().targetWord()}', '${level.getValue().name().toLowerCase()}')
            ">
                <span>${level.getKey()}<span class="md:inline hidden">: </span></span>
                <input
                class="
                md:inline
                hidden cursor-pointer"
                value="${messages.get(TranslationLocalizationKey.valueOf(level.getValue().name().toUpperCase()))}"
                type="submit">
            </form>
            @endif
        @endfor
    </div>
</div>
