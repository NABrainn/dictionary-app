@import gg.jte.support.ForSupport
@import lule.dictionary.enumeration.Familiarity
@import lule.dictionary.service.imports.importService.dto.Phrase
@import lule.dictionary.service.imports.importService.dto.Selectable
@import lule.dictionary.service.imports.importService.dto.request.DocumentAttribute
@import java.util.List
@import java.util.regex.Pattern

@param List<String> phrase
@param DocumentAttribute documentAttribute
@param Pattern cleanWordPattern
@param int paragraphId
@param int selectableId

!{var processedWord = String.join(" ", phrase.stream()
                                                .map(word -> cleanWordPattern.matcher(word)
                                                        .replaceAll("")
                                                        .replace("-", " ")
                                                        .toLowerCase())
                                                .toList());}
!{selectableId = documentAttribute.importData().content().startIndices().get(paragraphId) + selectableId;}
<div 
class="bg-secondary border border-2 border-tertiary inline-flex relative rounded text-lg gap-1 py-0.25 px-0.25 cursor-pointer"
hx-get="/translations?targetWord=${processedWord}&documentId=${documentAttribute.importData().documentId()}"
hx-target="#translation-form-container-${selectableId}"
hx-swap="innerHTML"
hx-vals='{"selectedWordId": "${selectableId}"}'
hx-trigger="click">

    <div
    onclick="event.stopPropagation()"
    class="
    cursor-default z-2 absolute inline left-2 top-10"
    id="translation-form-container-${selectableId}"
    hx-on::after-swap="htmx.find('import-page').updatePositions(${selectableId})">
    </div>
    @for(var phraseEntry : ForSupport.of(phrase))
        !{var wordId = 0;}
        @if(phrase.size() == 1)
            !{wordId = documentAttribute.importData().content().startIndices().get(paragraphId) + selectableId + (phraseEntry.getIndex());}
        @else
            !{wordId = documentAttribute.importData().content().startIndices().get(paragraphId) + selectableId + (phraseEntry.getIndex() - 1);}
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
                        wordId = wordId,
                        processedWord = cleanWord,
                        documentId = documentAttribute.importData().documentId(),
                        word = word,
                        bgColor = "bg-secondary",
                        textColor = "text-neutral",
                        familiarity = documentAttribute.importData().translations().get(cleanWord).familiarity(),
                        pointerEvents = "pointer-events-none",
                        phraseWord = "phrase-word"
                        )
                    @elseif(documentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.RECOGNIZED)
                        @template.document-page.content.word(
                        wordId = wordId,
                        processedWord = cleanWord,
                        documentId = documentAttribute.importData().documentId(),
                        word = word,
                        bgColor = "bg-secondary",
                        textColor = "text-neutral",
                        familiarity = documentAttribute.importData().translations().get(cleanWord).familiarity(),
                        pointerEvents = "pointer-events-none",
                        phraseWord = "phrase-word"
                        )

                    @elseif(documentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.FAMILIAR)
                        @template.document-page.content.word(
                        wordId = wordId,
                        processedWord = cleanWord,
                        documentId = documentAttribute.importData().documentId(),
                        word = word,
                        bgColor = "bg-secondary",
                        textColor = "text-neutral",
                        familiarity = documentAttribute.importData().translations().get(cleanWord).familiarity(),
                        pointerEvents = "pointer-events-none",
                        phraseWord = "phrase-word"
                        )
                    @elseif(documentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.KNOWN)
                        @template.document-page.content.word(
                        wordId = wordId,
                        processedWord = cleanWord,
                        documentId = documentAttribute.importData().documentId(),
                        word = word,
                        bgColor = "bg-secondary",
                        textColor = "text-neutral",
                        familiarity = documentAttribute.importData().translations().get(cleanWord).familiarity(),
                        pointerEvents = "pointer-events-none",
                        phraseWord = "phrase-word"
                        )
                    @else
                        @template.document-page.content.word(
                        wordId = wordId,
                        processedWord = cleanWord,
                        documentId = documentAttribute.importData().documentId(),
                        word = word,
                        bgColor = "bg-secondary",
                        textColor = "text-neutral",
                        familiarity = documentAttribute.importData().translations().get(cleanWord).familiarity(),
                        pointerEvents = "pointer-events-none",
                        phraseWord = "phrase-word"
                        )
                    @endif
                @else
                    @if(documentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.UNKNOWN)
                        @template.document-page.content.word(
                        wordId = wordId,
                        processedWord = cleanWord,
                        documentId = documentAttribute.importData().documentId(),
                        word = word,
                        bgColor = "bg-secondary",
                        textColor = "text-neutral",
                        familiarity = documentAttribute.importData().translations().get(cleanWord).familiarity(),
                        pointerEvents = "pointer-events-none",
                        phraseWord = "phrase-word"
                        )
                    @elseif(documentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.RECOGNIZED)
                        @template.document-page.content.word(
                        wordId = wordId,
                        processedWord = cleanWord,
                        documentId = documentAttribute.importData().documentId(),
                        word = word,
                        bgColor = "bg-secondary",
                        textColor = "text-neutral",
                        familiarity = documentAttribute.importData().translations().get(cleanWord).familiarity(),
                        pointerEvents = "pointer-events-none",
                        phraseWord = "phrase-word"
                        )
                    @elseif(documentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.FAMILIAR)
                        @template.document-page.content.word(
                        wordId = wordId,
                        processedWord = cleanWord,
                        documentId = documentAttribute.importData().documentId(),
                        word = word,
                        bgColor = "bg-secondary",
                        textColor = "text-neutral",
                        familiarity = documentAttribute.importData().translations().get(cleanWord).familiarity(),
                        pointerEvents = "pointer-events-none",
                        phraseWord = "phrase-word"
                        )
                    @elseif(documentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.KNOWN)
                        @template.document-page.content.word(
                        wordId = wordId,
                        processedWord = cleanWord,
                        documentId = documentAttribute.importData().documentId(),
                        word = word,
                        bgColor = "bg-secondary",
                        textColor = "text-neutral",
                        familiarity = documentAttribute.importData().translations().get(cleanWord).familiarity(),
                        pointerEvents = "pointer-events-none",
                        phraseWord = "phrase-word"
                        )
                    @else
                        @template.document-page.content.word(
                        wordId = wordId,
                        processedWord = cleanWord,
                        documentId = documentAttribute.importData().documentId(),
                        word = word,
                        bgColor = "bg-secondary",
                        textColor = "text-neutral",
                        familiarity = documentAttribute.importData().translations().get(cleanWord).familiarity(),
                        pointerEvents = "pointer-events-none",
                        phraseWord = "phrase-word"
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
                    bgColor = "bg-secondary",
                    textColor = "text-neutral",
                    pointerEvents = "pointer-events-none",
                    phraseWord = "phrase-word"
                    )
                @else
                    @template.document-page.content.word(
                    wordId = wordId,
                    processedWord = cleanWord,
                    documentId = documentAttribute.importData().documentId(),
                    word = word,
                    bgColor = "bg-secondary",
                    textColor = "text-neutral",
                    pointerEvents = "pointer-events-none",
                    phraseWord = "phrase-word"
                    )
                @endif
            @endif

        @else
            @template.document-page.content.word(
            wordId = wordId,
            processedWord = cleanWord,
            documentId = documentAttribute.importData().documentId(),
            word = word,
            bgColor = "bg-secondary",
            textColor = "text-neutral",
            selectable = false
            )
        @endif
    @endfor
</div>