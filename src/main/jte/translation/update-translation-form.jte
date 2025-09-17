@import lule.dictionary.controllerAdvice.data.BaseAttribute
@import lule.dictionary.translations.data.TranslationLocalizationKey
@import lule.dictionary.translations.data.attribute.TranslationAttribute
@import java.util.Map

@param BaseAttribute baseAttribute
@param TranslationAttribute attribute
@param Map<String, String> errors
@param Map<TranslationLocalizationKey, String> messages

<div
onclick="event.stopPropagation()"
class="
md:gap-0.75
gap-4 bg-tertiary flex flex-col p-3 rounded animate-fade"
id="translation-form">
    <div class="flex justify-end">
        <span
        class="md:text-md
        text-sm text-red-400 font-bold cursor-pointer"
        hx-on:click="documentPage.closeAllTranslationForms()">❌</span>
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
                        hx-target="#translation-form-container-${attribute.selectedWordId()}"
                        hx-swap="innerHTML"
                        hx-params="sourceWord,targetWord,selectedWordId,_csrf"
                        hx-vals='{
                            "targetWord": "${attribute.translation().targetWord()}",
                            "selectedWordId": "${attribute.selectedWordId()}",
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
                hx-params="selectedWordId,targetWord,familiarity,sourceWords,_csrf"
                hx-vals='{
                    "targetWord": "${attribute.translation().targetWord()}",
                    "familiarity": "${attribute.translation().familiarity()}",
                    "selectedWordId": "${attribute.selectedWordId()}",
                    "_csrf": "${baseAttribute._csrf().getToken()}"
                }'
                hx-put="/translations/sourceWords/update"
                hx-target="#translation-form-container-${attribute.selectedWordId()}"
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
            id="translation-form-${attribute.selectedWordId()}"
            hx-trigger="click"
            hx-put="/translations/familiarity/update"
            hx-params="targetWord,familiarity,sourceLanguage,targetLanguage,selectedWordId,isPhrase,_csrf"
            hx-vals='
            {
                "targetWord": "${attribute.translation().targetWord().toLowerCase()}",
                "familiarity": "${level.getValue().name()}",
                "sourceLanguage": "${attribute.translation().sourceLanguage().name()}",
                "targetLanguage": "${attribute.translation().targetLanguage().name()}",
                "selectedWordId": "${attribute.selectedWordId()}",
                "isPhrase": "${attribute.isPhrase()}",
                "_csrf": "${baseAttribute._csrf().getToken()}"
            }'
            hx-headers='js:{
                timeZoneOffset: new Date().getTimezoneOffset()
            }'
            hx-target="#translation-form-container-${attribute.selectedWordId()}"
            hx-swap="innerHTML"
            hx-on:click="
            const familiarityColors = new Map([
                ['unknown', ['bg-accent', 'text-primary']],
                ['recognized', ['bg-accent/80', 'text-primary']],
                ['familiar', ['bg-accent/60', 'text-primary']],
                ['known', ['bg-primary', 'text-neutral']],
                ['ignored', ['bg-primary', 'text-neutral']]
            ])
            if('${attribute.translation().targetWord()}'.split(' ').length > 1) {
                util.findAllByData({ key: 'is-phrase', value: 'true' })
                .filter(phrase => data.get(phrase, 'value') === '${attribute.translation().targetWord()}')
                .filter(phrase => data.get(phrase, 'is-saved') === 'true')
                .forEach(savedPhrase => {
                    data.set(savedPhrase, { key: 'familiarity', value: '${level.getValue().name().toLowerCase()}' })

                    if(data.get(savedPhrase, 'is-selected') === 'true') {
                        util.replaceClasses(savedPhrase, {
                            toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary'],
                            toAdd: ['bg-tertiary', 'text-accent']
                        })
                        Array.from(savedPhrase.children)
                        .filter(child => data.get(child, 'is-word') === 'true')
                        .flatMap(word => [...word.children])
                        .filter(child => child instanceof HTMLSpanElement)
                        .forEach(span => {
                            util.replaceClasses(span, {
                                toRemove: ['text-accent', 'text-primary'],
                                toAdd: ['text-accent']
                            })
                        })
                    }
                    else {
                        util.replaceClasses(savedPhrase, {
                            toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary', 'text-accent'],
                            toAdd: familiarityColors.get('${level.getValue().name().toLowerCase()}')
                        })
                        Array.from(savedPhrase.children)
                        .filter(child => data.get(child, 'is-word') === 'true')
                        .flatMap(word => [...word.children])
                        .filter(child => child instanceof HTMLSpanElement)
                        .forEach(span => {
                            util.replaceClasses(span, {
                                toRemove: ['text-accent', 'text-primary'],
                                toAdd: familiarityColors.get('${level.getValue().name().toLowerCase()}').at(1)
                            })
                        })
                    }
                })
            }
            else {
                util.findAllByData({ key: 'is-word', value: 'true' })
                .filter(word => data.get(word, 'value') === '${attribute.translation().targetWord()}')
                .forEach(word => {
                    data.set(word, { key: 'familiarity', value: '${level.getValue().name().toLowerCase()}' })
                    if(data.get(word, 'is-selected') === 'true') {
                        const selectedSpan = word.firstElementChild.nextElementSibling
                        util.replaceClasses(selectedSpan, {
                            toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary'],
                            toAdd: ['bg-tertiary', 'text-accent']
                        })
                    }
                    else {
                        const selectedSpan = word.firstElementChild.nextElementSibling
                        util.replaceClasses(selectedSpan, {
                            toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary'],
                            toAdd: familiarityColors.get('${level.getValue().name().toLowerCase()}')
                        })
                    }
                })
            }
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
