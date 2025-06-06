@import lule.dictionary.controller.imports.dto.DictionaryModel
@import lule.dictionary.enumeration.Familiarity
@import gg.jte.support.ForSupport

@param DictionaryModel dictionaryModel

<div id="page-content" class="flex flex-col gap-8 max-w-3/5 mx-auto">
    <span class="text-neutral font-bold text-4xl">${dictionaryModel.title()}</span>
    <div class="leading-10">
        @for(var word : ForSupport.of(dictionaryModel.content()))
            !{var cleanWord = word.get().replaceAll("[^\\p{L}\\p{N}]", "").trim().toLowerCase();}
            @if(dictionaryModel.translations().get(cleanWord) != null)
                @if(dictionaryModel.selectedWordId() == word.getIndex())
                    @if(dictionaryModel.translations().get(cleanWord).translationDetails().familiarity() == Familiarity.UNKNOWN)
                        <span
                        class="bg-tertiary bg-accent relative text-neutral px-0.1 py-0.1 cursor-pointer rounded whitespace-nowrap"
                        id="target-word-${word.getIndex()}"
                        hx-get="/translations?targetWord=${cleanWord}&importId=${dictionaryModel.importId()}"
                        hx-target="#translation-form-${word.getIndex()}"
                        hx-swap="innerHTML"
                        hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                        hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">
                        ${word.get()}<div class="cursor-default z-2 inline absolute -left-4 top-16" id="translation-form-${word.getIndex()}" onclick="event.stopPropagation();"></div></span>
                    @elseif(dictionaryModel.translations().get(cleanWord).translationDetails().familiarity() == Familiarity.RECOGNIZED)
                        <span
                        class="bg-tertiary bg-accent/80 relative text-neutral px-0.1 py-0.1 cursor-pointer rounded whitespace-nowrap"
                        id="target-word-${word.getIndex()}"
                        hx-get="/translations?targetWord=${cleanWord}&importId=${dictionaryModel.importId()}"
                        hx-target="#translation-form-${word.getIndex()}"
                        hx-swap="innerHTML"
                        hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                        hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">
                        ${word.get()}<div class="cursor-default z-2 inline absolute -left-4 top-16" id="translation-form-${word.getIndex()}" onclick="event.stopPropagation();"></div></span>
                    @elseif(dictionaryModel.translations().get(cleanWord).translationDetails().familiarity() == Familiarity.FAMILIAR)
                        <span
                        class="bg-tertiary bg-accent/60 relative text-neutral px-0.1 py-0.1 cursor-pointer rounded whitespace-nowrap"
                        id="target-word-${word.getIndex()}"
                        hx-get="/translations?targetWord=${cleanWord}&importId=${dictionaryModel.importId()}"
                        hx-target="#translation-form-${word.getIndex()}"
                        hx-swap="innerHTML"
                        hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                        hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">
                        ${word.get()}<div class="cursor-default z-2 inline absolute -left-4 top-16" id="translation-form-${word.getIndex()}" onclick="event.stopPropagation();"></div></span>
                    @elseif(dictionaryModel.translations().get(cleanWord).translationDetails().familiarity() == Familiarity.KNOWN)
                        <span
                        class="bg-tertiary bg-accent/40 relative text-neutral px-0.1 py-0.1 cursor-pointer rounded whitespace-nowrap"
                        id="target-word-${word.getIndex()}"
                        hx-get="/translations?targetWord=${cleanWord}&importId=${dictionaryModel.importId()}"
                        hx-target="#translation-form-${word.getIndex()}"
                        hx-swap="innerHTML"
                        hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                        hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">
                        ${word.get()}<div class="cursor-default z-2 inline absolute -left-4 top-16" id="translation-form-${word.getIndex()}" onclick="event.stopPropagation();"></div></span>
                    @else
                        <span
                        class="bg-tertiary bg-primary relative text-neutral px-0.1 py-0.1 cursor-pointer rounded whitespace-nowrap"
                        id="target-word-${word.getIndex()}"
                        hx-get="/translations?targetWord=${cleanWord}&importId=${dictionaryModel.importId()}"
                        hx-target="#translation-form-${word.getIndex()}"
                        hx-swap="innerHTML"
                        hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                        hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">
                        ${word.get()}<div class="cursor-default z-2 inline absolute -left-4 top-16" id="translation-form-${word.getIndex()}" onclick="event.stopPropagation();"></div></span>
                    @endif
                @else
                    @if(dictionaryModel.translations().get(cleanWord).translationDetails().familiarity() == Familiarity.UNKNOWN)
                        <span
                        class="bg-accent relative text-primary px-0.1 py-0.1 cursor-pointer rounded whitespace-nowrap"
                        id="target-word-${word.getIndex()}"
                        hx-get="/translations?targetWord=${cleanWord}&importId=${dictionaryModel.importId()}"
                        hx-target="#translation-form-${word.getIndex()}"
                        hx-swap="innerHTML"
                        hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                        hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">
                        ${word.get()}<div class="cursor-default z-2 inline absolute -left-4 top-16" id="translation-form-${word.getIndex()}" onclick="event.stopPropagation();"></div></span>
                    @elseif(dictionaryModel.translations().get(cleanWord).translationDetails().familiarity() == Familiarity.RECOGNIZED)
                        <span
                        class="bg-accent/80 relative text-primary px-0.1 py-0.1 cursor-pointer rounded whitespace-nowrap"
                        id="target-word-${word.getIndex()}"
                        hx-get="/translations?targetWord=${cleanWord}&importId=${dictionaryModel.importId()}"
                        hx-target="#translation-form-${word.getIndex()}"
                        hx-swap="innerHTML"
                        hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                        hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">
                        ${word.get()}<div class="cursor-default z-2 inline absolute -left-4 top-16" id="translation-form-${word.getIndex()}" onclick="event.stopPropagation();"></div></span>
                    @elseif(dictionaryModel.translations().get(cleanWord).translationDetails().familiarity() == Familiarity.FAMILIAR)
                        <span
                        class="bg-accent/60 relative text-primary px-0.1 py-0.1 cursor-pointer rounded whitespace-nowrap"
                        id="target-word-${word.getIndex()}"
                        hx-get="/translations?targetWord=${cleanWord}&importId=${dictionaryModel.importId()}"
                        hx-target="#translation-form-${word.getIndex()}"
                        hx-swap="innerHTML"
                        hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                        hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">
                        ${word.get()}<div class="cursor-default z-2 inline absolute -left-4 top-16" id="translation-form-${word.getIndex()}" onclick="event.stopPropagation();"></div></span>
                    @elseif(dictionaryModel.translations().get(cleanWord).translationDetails().familiarity() == Familiarity.KNOWN)
                        <span
                        class="bg-accent/40 relative text-primary px-0.1 py-0.1 cursor-pointer rounded whitespace-nowrap"
                        id="target-word-${word.getIndex()}"
                        hx-get="/translations?targetWord=${cleanWord}&importId=${dictionaryModel.importId()}"
                        hx-target="#translation-form-${word.getIndex()}"
                        hx-swap="innerHTML"
                        hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                        hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">
                        ${word.get()}<div class="cursor-default z-2 inline absolute -left-4 top-16" id="translation-form-${word.getIndex()}" onclick="event.stopPropagation();"></div></span>
                    @else
                        <span
                        class="bg-primary relative text-neutral px-0.1 py-0.1 cursor-pointer rounded whitespace-nowrap"
                        id="target-word-${word.getIndex()}"
                        hx-get="/translations?targetWord=${cleanWord}&importId=${dictionaryModel.importId()}"
                        hx-target="#translation-form-${word.getIndex()}"
                        hx-swap="innerHTML"
                        hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                        hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">
                        ${word.get()}<div class="cursor-default z-2 inline absolute -left-4 top-16" id="translation-form-${word.getIndex()}" onclick="event.stopPropagation();"></div></span>
                    @endif
                @endif
            @else
                @if(dictionaryModel.selectedWordId() == word.getIndex())
                    <span
                    class="bg-tertiary relative text-neutral px-0.1 py-0.1 cursor-pointer rounded whitespace-nowrap"
                    id="target-word-${word.getIndex()}"
                    hx-get="/translations?targetWord=${cleanWord}&importId=${dictionaryModel.importId()}"
                    hx-target="#translation-form-${word.getIndex()}"
                    hx-swap="innerHTML"
                    hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                    hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">
                    ${word.get()}<div class="cursor-default z-2 inline absolute -left-4 top-16" id="translation-form-${word.getIndex()}" onclick="event.stopPropagation();"></div></span>
                @else
                    <span
                    class="bg-primary relative text-neutral px-0.1 py-0.1 cursor-pointer rounded whitespace-nowrap"
                    id="target-word-${word.getIndex()}"
                    hx-get="/translations?targetWord=${cleanWord}&importId=${dictionaryModel.importId()}"
                    hx-target="#translation-form-${word.getIndex()}"
                    hx-swap="innerHTML"
                    hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                    hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">
                    ${word.get()}<div class="cursor-default z-2 inline absolute -left-4 top-16" id="translation-form-${word.getIndex()}" onclick="event.stopPropagation();"></div></span>
                @endif
            @endif
        @endfor
    </div>
</div>