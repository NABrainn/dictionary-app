@import gg.jte.support.ForSupport
@import lule.dictionary.translations.data.Translation
@import lule.dictionary.translations.data.Familiarity
@import lule.dictionary.documents.data.selectable.Phrase
@import java.util.Map
@import java.util.regex.Pattern

@param Phrase phrase
@param Pattern cleanWordPattern
@param int selectableId
@param int documentId
@param Map<String, Translation> translations

!{var processedWord = String.join(" ", phrase.targetWords().stream()
                                                .map(word -> cleanWordPattern.matcher(word)
                                                        .replaceAll("")
                                                        .replace("-", " ")
                                                        .toLowerCase())
                                                .toList());}
!{var wordBgColor = switch (phrase.familiarity()) {
    case UNKNOWN -> "bg-accent";
    case RECOGNIZED -> "bg-accent/80";
    case FAMILIAR -> "bg-accent/60";
    default -> "bg-primary";
};}
!{var wordTextColor = switch (phrase.familiarity()) {
    case UNKNOWN -> "text-primary";
    case RECOGNIZED -> "text-primary";
    case FAMILIAR -> "text-primary";
    default -> "text-neutral";
};}
<div
class="inline-flex relative rounded text-lg gap-1 cursor-pointer border border-2 border-secondary ${wordTextColor} ${wordBgColor}"

data-id="${selectableId}"
data-value="${processedWord}"
data-familiarity="${phrase.familiarity() != null ? phrase.familiarity().name().toLowerCase() : null}"

data-is-phrase="true"
data-is-saved="true"
data-is-selectable="true"
data-is-selected="false"

hx-get="/translations/find"
hx-target="#translation-form-container-${selectableId}"
hx-swap="innerHTML"
hx-params="documentId,targetWord,id,isPhrase,isPersisted"
hx-vals='{
    "documentId": "${documentId}",
    "targetWord": "${processedWord}",
    "id": "${selectableId}",
    "isPhrase": "true",
    "isPersisted": "true"
}'
hx-trigger="click"
hx-on:click="
documentPage.closeAllTranslationForms()
documentPage.cleanupSelectedWord()
documentPage.cleanupSelectedPhrase()
documentPage.handlePhraseClick(${selectableId})">
    <div
    class="
    cursor-default z-2 absolute inline"
    id="translation-form-container-${selectableId}"
    data-is-translation-form-container="true"
    hx-on::after-swap="formAdjuster.updatePositions(${selectableId})">
    </div>
    @for(var phraseEntry : ForSupport.of(phrase.targetWords()))
        !{var word = phraseEntry.get();}
        !{var cleanWord = cleanWordPattern.matcher(word)
                                                .replaceAll("")
                                                .replace("-", " ")
                                                .toLowerCase();}
        @if(!cleanWord.isEmpty())
            @if(translations.get(cleanWord) != null)
                @template.document.word(
                wordId = -1,
                processedWord = cleanWord,
                documentId = documentId,
                word = word,
                familiarity = translations.get(cleanWord).familiarity(),
                isSelectable = false,
                isWrapped = true)
            @else
                @template.document.word(
                wordId = -1,
                processedWord = cleanWord,
                documentId = documentId,
                word = word,
                isSelectable = true,
                isWrapped = true,
                familiarity = phrase.familiarity())
            @endif
        @else
            @template.document.word(
            wordId = -1,
            processedWord = cleanWord,
            documentId = documentId,
            word = word,
            textColor = wordTextColor,
            isSelectable = false,
            isWrapped = true,
            familiarity = phrase.familiarity())
        @endif
    @endfor
</div>
