@import gg.jte.support.ForSupport
@import lule.dictionary.enumeration.Familiarity
@import lule.dictionary.service.imports.importService.dto.Phrase
@import lule.dictionary.service.imports.importService.dto.request.DocumentAttribute
@import java.util.regex.Pattern

@param Phrase phrase
@param DocumentAttribute documentAttribute
@param Pattern cleanWordPattern
@param int paragraphId
@param int selectableId

!{var processedWord = String.join(" ", phrase.targetWords().stream()
                                                .map(word -> cleanWordPattern.matcher(word)
                                                        .replaceAll("")
                                                        .replace("-", " ")
                                                        .toLowerCase())
                                                .toList());}
!{selectableId = documentAttribute.importData().content().startIndices().get(paragraphId) + selectableId;}

!{var containerBgColor = "";}
!{var containerTextColor = "";}

@if(phrase.familiarity() == Familiarity.UNKNOWN)
    !{containerBgColor = "bg-accent";}
    !{containerTextColor = "text-primary";}
@elseif(phrase.familiarity() == Familiarity.RECOGNIZED)
    !{containerBgColor = "bg-accent/80";}
    !{containerTextColor = "text-primary";}
@elseif(phrase.familiarity() == Familiarity.FAMILIAR)
    !{containerBgColor = "bg-accent/60";}
    !{containerTextColor = "text-primary";}
@elseif(phrase.familiarity() == Familiarity.KNOWN)
    !{containerBgColor = "bg-primary";}
    !{containerTextColor = "text-neutral";}
@else
    !{containerBgColor = "bg-primary";}
    !{containerTextColor = "text-neutral";}
@endif

<div 
class="${phrase.familiarity().name().toLowerCase()} inline-flex relative rounded text-lg gap-1 cursor-pointer border border-3 border-secondary ${containerBgColor} selectable"
hx-get="/translations?targetWord=${processedWord}&documentId=${documentAttribute.importData().documentId()}&isPhrase=true"
hx-target="#translation-form-container-${selectableId}"
hx-swap="innerHTML"
hx-vals='{"selectedWordId": "${selectableId}"}'
hx-trigger="click"
hx-on::before-request="htmx.find('selectable-highlighter').updateWordId(${selectableId})">

    <div
    onclick="event.stopPropagation()"
    class="
    cursor-default z-2 absolute inline left-2 top-10"
    id="translation-form-container-${selectableId}"
    hx-on::after-swap="htmx.find('import-page').updatePositions(${selectableId})">
    </div>
    @for(var phraseEntry : ForSupport.of(phrase.targetWords()))
        !{var wordId = 0;}
        @if(phrase.targetWords().size() == 1)
            !{wordId = documentAttribute.importData().content().startIndices().get(paragraphId) + (phraseEntry.getIndex());}
        @else
            !{wordId = documentAttribute.importData().content().startIndices().get(paragraphId) + (phraseEntry.getIndex() - 1);}
        @endif
        !{var word = phraseEntry.get();}
        !{var cleanWord = cleanWordPattern.matcher(word)
                                                .replaceAll("")
                                                .replace("-", " ")
                                                .toLowerCase();}
        @if(!cleanWord.isEmpty())
            @if(documentAttribute.importData().translations().get(cleanWord) != null)
                @if(documentAttribute.importData().selectedWordId() == wordId)
                    @if(documentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.UNKNOWN)
                        @template.document-page.content.word(
                            textColor = containerTextColor,
                            familiarity = documentAttribute.importData().translations().get(cleanWord).familiarity(),
                            wordId = wordId,
                            processedWord = cleanWord,
                            documentId = documentAttribute.importData().documentId(),
                            word = word,
                            selectable = false
                        )
                    @elseif(documentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.RECOGNIZED)
                        @template.document-page.content.word(
                            textColor = containerTextColor,
                            wordId = wordId,
                            processedWord = cleanWord,
                            documentId = documentAttribute.importData().documentId(),
                            word = word,
                            familiarity = documentAttribute.importData().translations().get(cleanWord).familiarity(),
                            selectable = false
                        )

                    @elseif(documentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.FAMILIAR)
                        @template.document-page.content.word(
                            wordId = wordId,
                            processedWord = cleanWord,
                            documentId = documentAttribute.importData().documentId(),
                            word = word,
                            textColor = containerTextColor,
                            familiarity = documentAttribute.importData().translations().get(cleanWord).familiarity(),
                            selectable = false
                        )
                    @elseif(documentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.KNOWN)
                        @template.document-page.content.word(
                            wordId = wordId,
                            processedWord = cleanWord,
                            documentId = documentAttribute.importData().documentId(),
                            word = word,
                            textColor = containerTextColor,
                            familiarity = documentAttribute.importData().translations().get(cleanWord).familiarity(),
                            selectable = false
                        )
                    @else
                        @template.document-page.content.word(
                            wordId = wordId,
                            processedWord = cleanWord,
                            documentId = documentAttribute.importData().documentId(),
                            word = word,
                            textColor = containerTextColor,
                            familiarity = documentAttribute.importData().translations().get(cleanWord).familiarity(),
                            selectable = false
                        )
                    @endif
                @else
                    @if(documentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.UNKNOWN)
                        @template.document-page.content.word(
                            wordId = wordId,
                            processedWord = cleanWord,
                            documentId = documentAttribute.importData().documentId(),
                            word = word,
                            textColor = containerTextColor,
                            familiarity = documentAttribute.importData().translations().get(cleanWord).familiarity(),
                            selectable = false
                        )
                    @elseif(documentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.RECOGNIZED)
                        @template.document-page.content.word(
                            wordId = wordId,
                            processedWord = cleanWord,
                            documentId = documentAttribute.importData().documentId(),
                            word = word,
                            textColor = containerTextColor,
                            familiarity = documentAttribute.importData().translations().get(cleanWord).familiarity(),
                            selectable = false
                        )
                    @elseif(documentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.FAMILIAR)
                        @template.document-page.content.word(
                            wordId = wordId,
                            processedWord = cleanWord,
                            documentId = documentAttribute.importData().documentId(),
                            word = word,
                            textColor = containerTextColor,
                            familiarity = documentAttribute.importData().translations().get(cleanWord).familiarity(),
                            selectable = false
                        )
                    @elseif(documentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.KNOWN)
                        @template.document-page.content.word(
                            wordId = wordId,
                            processedWord = cleanWord,
                            documentId = documentAttribute.importData().documentId(),
                            word = word,
                            textColor = containerTextColor,
                            familiarity = documentAttribute.importData().translations().get(cleanWord).familiarity(),
                            selectable = false
                        )
                    @else
                        @template.document-page.content.word(
                            wordId = wordId,
                            processedWord = cleanWord,
                            documentId = documentAttribute.importData().documentId(),
                            word = word,
                            textColor = containerTextColor,
                            familiarity = documentAttribute.importData().translations().get(cleanWord).familiarity(),
                            selectable = false
                        )
                    @endif
                @endif
            @else
                @if(documentAttribute.importData().selectedWordId() == wordId)
                    @template.document-page.content.word(
                        wordId = wordId,
                        processedWord = cleanWord,
                        documentId = documentAttribute.importData().documentId(),
                        word = word,
                        textColor = containerTextColor,
                        selectable = false
                    )
                @else
                    @template.document-page.content.word(
                        wordId = wordId,
                        processedWord = cleanWord,
                        documentId = documentAttribute.importData().documentId(),
                        word = word,
                        textColor = containerTextColor,
                        selectable = false
                    )
                @endif
            @endif

        @else
            @template.document-page.content.word(
                wordId = wordId,
                processedWord = cleanWord,
                documentId = documentAttribute.importData().documentId(),
                word = word,
                textColor = containerTextColor,
                selectable = false
            )
        @endif
    @endfor
</div>