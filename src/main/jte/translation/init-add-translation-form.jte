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
md:gap-1
bg-tertiary flex flex-col p-3 rounded gap-2"
id="translation-form"
hx-on:click="event.stopPropagation()">
    <div class="flex justify-end">
        <span
        class="md:text-md
        text-sm text-red-400 font-bold cursor-pointer"
        _="on click remove <div#translation-form/>">❌</span>
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
                <span class="block text-accent text-wrap">${word}</span>
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
                    "sourceLanguage": "${attribute.translation().ownerInfo().sourceLanguage()}",
                    "targetLanguage": "${attribute.translation().ownerInfo().targetLanguage()}",
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
                        <label
                        for="sw-input"
                        class="hidden"></label>
                        <input
                        id="sw-input"
                        class="
                        md:max-w-50
                        max-w-40 outline-none border border-2
                        border-primary rounded text-neutral text-md grow-1
                        focus:border-accent"
                        type="text"
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
            md:bg-tertiary md:text-accent md:cursor-normal md:justify-start md:gap-1
            bg-accent text-primary cursor-pointer justify-center flex font-bold rounded size-6  items-center
            hover:text-primary"
            hx-trigger="click"
            hx-post="/translations/"
            hx-headers='{
                "X-XSRF-TOKEN": "${baseAttribute.csrf()}"
            }'
            hx-params="id,targetWord,familiarity,sourceLanguage,targetLanguage,documentId,sourceWords,isPhrase"
            hx-vals='{
                "targetWord": "${attribute.translation().processedTargetWord()}",
                "familiarity": "${level.getValue().name()}",
                "sourceLanguage": "${attribute.translation().ownerInfo().sourceLanguage().name()}",
                "targetLanguage": "${attribute.translation().ownerInfo().targetLanguage().name()}",
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
