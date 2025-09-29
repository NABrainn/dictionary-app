@import lule.dictionary.controllerAdvice.data.BaseAttribute
@import lule.dictionary.translations.data.TranslationLocalizationKey
@import lule.dictionary.translations.data.attribute.TranslationAttribute
@import java.util.Map

@param BaseAttribute baseAttribute 
@param TranslationAttribute attribute
@param Map<String, String> errors
@param Map<TranslationLocalizationKey, String> messages

<div
hx-on:click="event.stopPropagation()"
class="
md:gap-0.75
bg-tertiary flex flex-col p-3 rounded gap-4"
id="translation-form">
    <div class="flex justify-end">
        <span
        class="md:text-md
        text-sm text-red-400 font-bold cursor-pointer"
        hx-on:click="documentPage.closeAllTranslationForms()">❌</span>
    </div>
    <span class="font-bold text-lg text-accent text-wrap">${attribute.translation().processedTargetWord()}</span>
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
            <div class="flex">
                <form
                class="
                flex flex-col gap-2
                translation-form"
                hx-headers='{
                    "X-XSRF-TOKEN": "${baseAttribute.csrf()}"
                }'
                hx-params="id,targetWord,familiarity,sourceLanguage,targetLanguage,documentId,sourceWords,isPhrase"
                hx-vals='{
                    "id": "${attribute.id()}",
                    "targetWord": "${attribute.translation().processedTargetWord()}",
                    "familiarity": "UNKNOWN",
                    "sourceLanguage": "${attribute.translation().sourceLanguage()}",
                    "targetLanguage": "${attribute.translation().targetLanguage()}",
                    "documentId": "${attribute.documentId()}",
                    "isPhrase": "${attribute.isPhrase()}"
                }'
                hx-post="/translations/"
                hx-swap="outerHTML"
                hx-target="#translation-form">
                    @for(String word : attribute.translation().sourceWords())
                        <input
                        name="sourceWords"
                        type="hidden"
                        value="${word}">
                    @endfor
                    <div class="flex gap-2">
                        <input
                        class="
                        max-w-50 outline-none border border-2
                        border-primary rounded text-neutral text-md grow-1
                        focus:border-accent"
                        type="text"
                        placeholder=""
                        name="sourceWords">
                        <button
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
            class="
            md:bg-tertiary md:text-accent md:size-auto md:gap-1 md:cursor-normal md:justify-start
            cursor-pointer bg-accent text-primary flex gap-4 hover:text-primary font-bold rounded size-8 justify-center items-center
            translation-form"
            hx-trigger="click"
            hx-post="/translations/"
            hx-headers='{
                "X-XSRF-TOKEN": "${baseAttribute.csrf()}"
            }'
            hx-params="id,targetWord,familiarity,sourceLanguage,targetLanguage,documentId,sourceWords,isPhrase"
            hx-vals='{
                "targetWord": "${attribute.translation().processedTargetWord()}",
                "familiarity": "${level.getValue().name()}",
                "sourceLanguage": "${attribute.translation().sourceLanguage().name()}",
                "targetLanguage": "${attribute.translation().targetLanguage().name()}",
                "documentId": "${attribute.documentId()}",
                "id": "${attribute.id()}",
                "isPhrase": "${attribute.isPhrase()}"
            }'
            hx-swap="outerHTML"
            hx-target="#translation-form">
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
