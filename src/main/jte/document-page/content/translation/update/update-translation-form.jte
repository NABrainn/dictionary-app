@import lule.dictionary.translations.service.dto.attribute.TranslationAttribute
@import java.util.Map

@param TranslationAttribute translationAttribute
@param Map<String, String> translationLocalization

!{var processedWord = translationAttribute.translation().targetWord().replaceAll("[^\\p{L}\\p{N}\\s-]", "").toLowerCase();}

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
    <span class="font-bold text-lg text-accent text-wrap">${translationAttribute.translation().targetWord()}</span>
    <div id="translation-manager">
        @template.document-page.content.translation.update.translation-manager(
            translationAttribute = translationAttribute,
            translationLocalization = translationLocalization
        )
    </div>
    <div class="
    md:flex md:flex-col md:gap-0.25
    flex flex-row justify-around">
        @for(var level : translationAttribute.familiarityLevels().entrySet())
            @if(level.getKey() == translationAttribute.currentFamiliarity())
                <div class="
                flex flex-row md:bg-tertiary md:text-primary md:size-auto md:justify-start
                font-bold bg-primary text-accent gap-1  size-8 rounded justify-center items-center">
                    <span class="
                    md:inline
                    block">${level.getKey()}<span class="md:inline hidden">: </span></span>
                    <span class="
                    md:inline
                    hidden">${translationLocalization.get(level.getValue().name().toLowerCase())}</span>
                </div>
            @else
            <form
            class="
            md:bg-tertiary md:text-accent md:size-auto md:gap-1 md:cursor-normal md:justify-start
            cursor-pointer bg-accent text-primary flex gap-4 hover:text-primary font-bold rounded size-8  justify-center items-center"
            id="translation-form-${translationAttribute.selectedWordId()}"
            hx-trigger="click"
            hx-put="/translations/familiarity/update"
            hx-params="targetWord,familiarity,sourceLanguage,targetLanguage,selectedWordId,isPhrase"
            hx-vals='
            {
                "targetWord": "${translationAttribute.translation().targetWord().replaceAll("\\p{P}", "").toLowerCase()}",
                "familiarity": "${level.getValue().name()}",
                "sourceLanguage": "${translationAttribute.translation().sourceLanguage().name()}",
                "targetLanguage": "${translationAttribute.translation().targetLanguage().name()}",
                "selectedWordId": "${translationAttribute.selectedWordId()}",
                "isPhrase": "${translationAttribute.isPhrase()}"
            }'
            hx-headers='js:{
                timeZoneOffset: new Date().getTimezoneOffset()
            }'
            hx-target="#translation-form-container-${translationAttribute.selectedWordId()}"
            hx-swap="innerHTML"
            hx-on:click="
            const familiarityColors = new Map([
                ['unknown', ['bg-accent', 'text-primary']],
                ['recognized', ['bg-accent/80', 'text-primary']],
                ['familiar', ['bg-accent/60', 'text-primary']],
                ['known', ['bg-primary', 'text-neutral']],
                ['ignored', ['bg-primary', 'text-neutral']]
            ])

            if('${processedWord}'.split(' ').length > 1) {
                util.findAllByData({ key: 'is-phrase', value: 'true' })
                .filter(phrase => data.get(phrase, 'value') === '${processedWord}')
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
                .filter(word => data.get(word, 'value') === '${processedWord}')
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
                value="${translationLocalization.get(level.getValue().name().toLowerCase())}"
                type="submit">
            </form>
            @endif
        @endfor
    </div>
</div>
