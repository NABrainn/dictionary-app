@import gg.jte.support.ForSupport
@import lule.dictionary.dto.database.interfaces.translation.Translation
@import lule.dictionary.enumeration.Familiarity
@import lule.dictionary.service.imports.importService.dto.Phrase
@import java.util.List
@import java.util.Map
@import java.util.regex.Pattern

@param Phrase phrase
@param Pattern cleanWordPattern
@param int paragraphId
@param int selectableId
@param int documentId
@param List<Integer> startIndices
@param Map<String, Translation> translations

!{var processedWord = String.join(" ", phrase.targetWords().stream()
                                                .map(word -> cleanWordPattern.matcher(word)
                                                        .replaceAll("")
                                                        .replace("-", " ")
                                                        .toLowerCase())
                                                .toList());}

!{var containerBgColor = "";}
!{var containerTextColor = "";}
!{var familiarity = "";}

@if(phrase.familiarity() != null)
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
    !{familiarity = phrase.familiarity().name().toLowerCase();}
@else
    !{containerBgColor = "bg-primary";}
    !{containerTextColor = "text-neutral";}
@endif
<div
class="${familiarity} inline-flex relative rounded text-lg gap-1 cursor-pointer border border-2 border-secondary ${containerTextColor} ${containerBgColor}"
id="selectable-${selectableId}"

data-type="phrase"
data-is-phrase="true"
data-is-phrase-saved="true"
data-is-selectable="true"
data-familiarity="${familiarity}"

hx-get="/translations/find"
hx-target="#translation-form-container-${selectableId}"
hx-swap="innerHTML"
hx-vals='{
    "documentId": "${documentId}",
    "targetWord": "${processedWord}",
    "selectedWordId": "${selectableId}",
    "isPhrase": "true",
    "isFound": "true"
}'
hx-trigger="click"
hx-on::before-request="
htmx.find('phrase-selector').handlePhraseClick(${selectableId});
htmx.find('phrase-highlighter').highlightPhrases('${processedWord}', '${familiarity}');
">

    <div
    onclick="event.stopPropagation()"
    class="
    cursor-default z-2 absolute inline left-2 top-10"
    id="translation-form-container-${selectableId}"

    data-is-translation-form-container="true"
    hx-on::after-swap="htmx.find('form-position-adjuster').updatePositions(${selectableId})">
    </div>
    @for(var phraseEntry : ForSupport.of(phrase.targetWords()))
        !{var wordId = 0;}
        @if(phrase.targetWords().size() == 1)
            !{wordId = startIndices.get(paragraphId) + (phraseEntry.getIndex());}
        @else
            !{wordId = startIndices.get(paragraphId) + (phraseEntry.getIndex() - 1);}
        @endif
        !{var word = phraseEntry.get();}
        !{var cleanWord = cleanWordPattern.matcher(word)
                                                .replaceAll("")
                                                .replace("-", " ")
                                                .toLowerCase();}
        @if(!cleanWord.isEmpty())
            @if(translations.get(cleanWord) != null)
                @if(translations.get(cleanWord).familiarity() == Familiarity.UNKNOWN)
                    @template.document-page.content.word(
                        wordId = wordId,
                        processedWord = cleanWord,
                        documentId = documentId,
                        word = word,
                        familiarity = translations.get(cleanWord).familiarity(),
                        isSelectable = false,
                        isWrapped = true
                    )
                @elseif(translations.get(cleanWord).familiarity() == Familiarity.RECOGNIZED)
                    @template.document-page.content.word(
                        wordId = wordId,
                        processedWord = cleanWord,
                        documentId = documentId,
                        word = word,
                        familiarity = translations.get(cleanWord).familiarity(),
                        isSelectable = false,
                    isWrapped = true
                    )
                @elseif(translations.get(cleanWord).familiarity() == Familiarity.FAMILIAR)
                    @template.document-page.content.word(
                        wordId = wordId,
                        processedWord = cleanWord,
                        documentId = documentId,
                        word = word,
                        familiarity = translations.get(cleanWord).familiarity(),
                        isSelectable = false,
                    isWrapped = true
                    )
                @elseif(translations.get(cleanWord).familiarity() == Familiarity.KNOWN)
                    @template.document-page.content.word(
                        wordId = wordId,
                        processedWord = cleanWord,
                        documentId = documentId,
                        word = word,
                        familiarity = translations.get(cleanWord).familiarity(),
                        isSelectable = false,
                    isWrapped = true
                    )
                @else
                    @template.document-page.content.word(
                        wordId = wordId,
                        processedWord = cleanWord,
                        documentId = documentId,
                        word = word,
                        familiarity = translations.get(cleanWord).familiarity(),
                        isSelectable = false,
                    isWrapped = true
                    )
                @endif
            @else
                @template.document-page.content.word(
                    wordId = wordId,
                    processedWord = cleanWord,
                    documentId = documentId,
                    word = word,
                    isSelectable = false,
                isWrapped = true
                )
            @endif

        @else
            @template.document-page.content.word(
                wordId = wordId,
                processedWord = cleanWord,
                documentId = documentId,
                word = word,
                textColor = containerTextColor,
                isSelectable = false,
            isWrapped = true
            )
        @endif
    @endfor
</div>
