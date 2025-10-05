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
gap-4 bg-tertiary flex flex-col p-3 rounded select-none"
id="translation-form">
    <div
    class="flex justify-end">
        <span
        class="md:text-md
        text-sm text-red-400 font-bold cursor-pointer"
        _="on click remove <div#translation-form/>">❌</span>
    </div>
    <span
    class="font-bold text-lg text-accent text-wrap">${attribute.translation().processedTargetWord()}</span>
    <div
    class="flex flex-col gap-2 text-wrap">
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
                hx-delete="/translations/sourceWords"
                hx-target="#form-container-${attribute.id()}"
                hx-swap="innerHTML"
                hx-headers='{
                    "X-XSRF-TOKEN": "${baseAttribute.csrf()}"
                }'
                hx-params="sourceWord,targetWord,id"
                hx-vals='{
                    "targetWord": "${attribute.translation().processedTargetWord()}",
                    "id": "${attribute.id()}",
                    "sourceWord": "${word}"
                }'>
                    <button
                    class="cursor-pointer text-md block"
                    type="submit">🗑️</button>
                </form>
            </div>
        @endfor
        <div class="flex flex-col gap-2">
            <div id="source-word-form" class="flex">
                <form
                class="flex flex-col gap-2"
                hx-headers='{
                    "X-XSRF-TOKEN": "${baseAttribute.csrf()}"
                }'
                hx-params="id,targetWord,familiarity,sourceWords"
                hx-vals='{
                    "targetWord": "${attribute.translation().processedTargetWord()}",
                    "familiarity": "${attribute.translation().familiarity()}",
                    "id": "${attribute.id()}"
                }'
                hx-put="/translations/sourceWords/update"
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
                        id="input-source-word"
                        class="
                        outline-none border border-2 ${errors.get("sourceWord") != null ? "border-error" : "border-primary"} rounded text-neutral text-md grow-1
                        focus:border-accent max-w-50"
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
            @if(level.getKey() == attribute.currentFamiliarity())
                <div class="
                flex flex-row md:bg-tertiary md:text-primary md:gap-1 md:size-auto md:justify-start
                font-bold bg-primary text-accent size-8 rounded justify-center items-center"
                >
                    <span>${level.getKey()}<span class="md:inline hidden">: </span></span>
                    <span class="
                    md:inline
                    hidden">${messages.get(TranslationLocalizationKey.valueOf(level.getValue().name().toUpperCase()))}</span>
                </div>
            @else
            <form
            class="
            md:bg-tertiary md:text-accent md:size-auto md:gap-1 md:cursor-normal md:justify-start
            cursor-pointer bg-accent text-primary flex hover:text-primary font-bold rounded size-8 justify-center items-center"
            id="translation-form-${attribute.id()}"
            hx-trigger="click"
            hx-put="/translations/familiarity/update"
            hx-headers='{
                "X-XSRF-TOKEN": "${baseAttribute.csrf()}"
            }'
            hx-params="targetWord,familiarity,sourceLanguage,targetLanguage,id,isPhrase"
            hx-vals='
            {
                "targetWord": "${attribute.translation().processedTargetWord().toLowerCase()}",
                "familiarity": "${level.getValue().name()}",
                "sourceLanguage": "${attribute.translation().ownerInfo().sourceLanguage().name()}",
                "targetLanguage": "${attribute.translation().ownerInfo().targetLanguage().name()}",
                "id": "${attribute.id()}",
                "isPhrase": "${attribute.isPhrase()}"
            }'
            hx-swap="outerHTML"
            hx-target="#translation-form">
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
