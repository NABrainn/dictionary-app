@import lule.dictionary.service.translation.dto.attribute.TranslationAttribute
@import java.util.Map

@param TranslationAttribute translationAttribute
@param String targetWord
@param Map<String, String> translationLocalization

!{var processedWord = translationAttribute.translation().targetWord().replaceAll("[^\\p{L}\\p{N}\\s-]", "").toLowerCase();}

<div
onclick="event.stopPropagation()"
class="translation-form-${translationAttribute.selectedWordId()} bg-tertiary flex flex-col gap-0.75 p-3 rounded form max-w-45 pointer-events-auto animate-fade"
id="translation-form">
    <div class="flex justify-end">
        <span
        class="md:text-md
        text-sm text-red-400 font-bold cursor-pointer"
        onclick="htmx.find('.form').outerHTML = ''">❌</span>
    </div>
    <span class="font-bold text-lg text-accent text-wrap">${processedWord}</span>
    <div id="translation-manager">
        @template.document-page.content.translation.add.translation-manager(
            translationAttribute = translationAttribute,
            translationLocalization = translationLocalization
        )
    </div>
    <div class="flex flex-col gap-0.25">
        @for(var level : translationAttribute.familiarityLevels().entrySet())
            <form
            class="flex gap-1 cursor-pointer font-bold text-neutral hover:text-primary"
            id="translation-form-${translationAttribute.selectedWordId()}"
            hx-post="/translations/new"
            hx-vals='{
                "targetWord": "${processedWord}",
                "familiarity": "${level.getValue().name()}",
                "sourceLanguage": "${translationAttribute.translation().sourceLanguage().name()}",
                "targetLanguage": "${translationAttribute.translation().targetLanguage().name()}",
                "documentId": "${translationAttribute.documentId()}",
                "selectedWordId": "${translationAttribute.selectedWordId()}",
                "isPhrase": "${translationAttribute.isPhrase()}"
            }'
            hx-target="#translation-form-container-${translationAttribute.selectedWordId()}"
            hx-headers='js:{
                timeZoneOffset: new Date().getTimezoneOffset()
            }'
            hx-swap="innerHTML"
            hx-on::after-request="
            const familiarityColors = new Map([
                ['unknown', ['bg-accent', 'text-primary']],
                ['recognized', ['bg-accent/80', 'text-primary']],
                ['familiar', ['bg-accent/60', 'text-primary']],
                ['known', ['bg-primary', 'text-neutral']],
                ['ignored', ['bg-primary', 'text-neutral']]
            ])
            if('${translationAttribute.translation().targetWord()}'.split(' ').length > 1) {
                util.findAllByData({ key: 'is-phrase', value: 'true' })
                .filter(phrase => data.get(phrase, 'value') === '${translationAttribute.translation().targetWord()}')
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
                        .flatMap(word => word.firstElementChild)
                        .forEach(span => {
                            util.replaceClasses(span, {
                                toRemove: ['text-accent', 'text-primary'],
                                toAdd: ['text-accent']
                            })
                        })
                    }
                    else {
                        util.replaceClasses(savedPhrase, {
                            toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary'],
                            toAdd: familiarityColors.get('${level.getValue().name().toLowerCase()}')
                        })
                    }
                })
            }
            else {
                util.findAllByData({ key: 'is-word', value: 'true' })
                .filter(word => data.get(word, 'value') === '${translationAttribute.translation().targetWord()}')
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
                <span class="cursor-pointer">${level.getKey()}: </span>
                @for(var word : translationAttribute.translation().sourceWords())
                    <input type="hidden" name="sourceWords" value="${word}">
                @endfor

                <input
                class=" cursor-pointer"
                value="${translationLocalization.get(level.getValue().name().toLowerCase())}"
                type="submit">
            </form>
        @endfor
    </div>
</div>
