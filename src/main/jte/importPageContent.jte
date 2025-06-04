@import lule.dictionary.enumeration.Familiarity
@import lule.dictionary.service.application.ImportData
@import gg.jte.support.ForSupport

@param ImportData data
@param int selectedWordId
<div id="page-content">
    <span class="text-neutral font-bold text-4xl">${data.title()}</span>
    <div class="leading-10">
        @for(var word : ForSupport.of(data.content().split(" ")))
            !{var cleanWord = word.get().replaceAll("[^\\p{L}]", "").trim().toLowerCase();}
            @if(data.translations().get(cleanWord) != null)
                @if(selectedWordId == word.getIndex())
                    <span
                            class="bg-tertiary relative text-neutral px-0.1 py-0.1 cursor-pointer rounded"
                            id="target-word-${word.getIndex()}"
                            hx-get="/translations?targetWord=${cleanWord}&importId=${data.importId()}"
                            hx-target="#translation-form-${word.getIndex()}"
                            hx-swap="innerHTML"
                            hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                            hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">${cleanWord}
                        <div
                                class="cursor-default z-2 inline absolute -left-4 top-16"
                                id="translation-form-${word.getIndex()}"
                                onclick="event.stopPropagation();"></div>
                    </span>
                @else
                    @if(data.translations().get(cleanWord).translationDetails().familiarity() == Familiarity.UNKNOWN)
                        <span
                                class="bg-accent/80 relative text-primary px-0.1 py-0.1 cursor-pointer rounded"
                                id="target-word-${word.getIndex()}"
                                hx-get="/translations?targetWord=${cleanWord}&importId=${data.importId()}"
                                hx-target="#translation-form-${word.getIndex()}"
                                hx-swap="innerHTML"
                                hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                                hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">${cleanWord}
                        <div
                                class="cursor-default z-2 inline absolute -left-4 top-16"
                                id="translation-form-${word.getIndex()}"
                                onclick="event.stopPropagation();"></div>
                    </span>
                    @elseif(data.translations().get(cleanWord).translationDetails().familiarity() == Familiarity.RECOGNIZED)
                        <span
                                class="bg-accent/60 relative text-primary px-0.1 py-0.1 cursor-pointer rounded"
                                id="target-word-${word.getIndex()}"
                                hx-get="/translations?targetWord=${cleanWord}&importId=${data.importId()}"
                                hx-target="#translation-form-${word.getIndex()}"
                                hx-swap="innerHTML"
                                hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                                hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">${cleanWord}
                        <div
                                class="cursor-default z-2 inline absolute -left-4 top-16"
                                id="translation-form-${word.getIndex()}"
                                onclick="event.stopPropagation();"></div>
                    </span>
                    @elseif(data.translations().get(cleanWord).translationDetails().familiarity() == Familiarity.FAMILIAR)
                        <span
                                class="bg-accent/40 relative text-neutral px-0.1 py-0.1 cursor-pointer rounded"
                                id="target-word-${word.getIndex()}"
                                hx-get="/translations?targetWord=${cleanWord}&importId=${data.importId()}"
                                hx-target="#translation-form-${word.getIndex()}"
                                hx-swap="innerHTML"
                                hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                                hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">${cleanWord}
                        <div
                                class="cursor-default z-2 inline absolute -left-4 top-16"
                                id="translation-form-${word.getIndex()}"
                                onclick="event.stopPropagation();"></div>
                    </span>
                    @elseif(data.translations().get(cleanWord).translationDetails().familiarity() == Familiarity.KNOWN)
                        <span
                                class="bg-accent/10 relative text-neutral px-0.1 py-0.1 cursor-pointer rounded"
                                id="target-word-${word.getIndex()}"
                                hx-get="/translations?targetWord=${cleanWord}&importId=${data.importId()}"
                                hx-target="#translation-form-${word.getIndex()}"
                                hx-swap="innerHTML"
                                hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                                hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">${cleanWord}
                        <div
                                class="cursor-default z-2 inline absolute -left-4 top-16"
                                id="translation-form-${word.getIndex()}"
                                onclick="event.stopPropagation();"></div>
                    </span>
                    @else
                        <span
                                class="bg-primary relative text-neutral px-0.1 py-0.1 cursor-pointer rounded"
                                id="target-word-${word.getIndex()}"
                                hx-get="/translations?targetWord=${cleanWord}&importId=${data.importId()}"
                                hx-target="#translation-form-${word.getIndex()}"
                                hx-swap="innerHTML"
                                hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                                hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">${cleanWord}
                        <div
                                class="cursor-default z-2 inline absolute -left-4 top-16"
                                id="translation-form-${word.getIndex()}"
                                onclick="event.stopPropagation();"></div>
                    </span>
                    @endif

                @endif
            @else
                @if(selectedWordId == word.getIndex())
                    <span
                            class="bg-tertiary relative text-neutral px-0.1 py-0.1 cursor-pointer rounded"
                            id="target-word-${word.getIndex()}"
                            hx-get="/translations?targetWord=${cleanWord}&importId=${data.importId()}"
                            hx-target="#translation-form-${word.getIndex()}"
                            hx-swap="innerHTML"
                            hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                            hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">${cleanWord}
                        <div
                                class="cursor-default z-2 inline absolute -left-4 top-16"
                                id="translation-form-${word.getIndex()}"
                                onclick="event.stopPropagation();"></div>
                    </span>
                @else
                    <span
                            class="bg-primary relative text-neutral px-0.1 py-0.1 cursor-pointer rounded"
                            id="target-word-${word.getIndex()}"
                            hx-get="/translations?targetWord=${cleanWord}&importId=${data.importId()}"
                            hx-target="#translation-form-${word.getIndex()}"
                            hx-swap="innerHTML"
                            hx-vals='{"selectedWordId": "${word.getIndex()}"}'
                            hx-on::before-request="document.querySelector('import-page').onWordClick(${word.getIndex()})">${cleanWord}
                        <div
                                class="cursor-default z-2 inline absolute -left-4 top-16"
                                id="translation-form-${word.getIndex()}"
                                onclick="event.stopPropagation();"></div>
                    </span>
                @endif
            @endif
        @endfor
    </div>
</div>