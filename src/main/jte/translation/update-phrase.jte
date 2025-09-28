@import gg.jte.support.ForSupport
@import lule.dictionary.translations.data.Translation

@param Translation translation
@param int selectableId

!{var wordBgColor = switch (translation.familiarity()) {
    case UNKNOWN -> "bg-accent";
    case RECOGNIZED -> "bg-accent/80";
    case FAMILIAR -> "bg-accent/60";
    default -> "bg-primary";
};}
!{var wordTextColor = switch (translation.familiarity()) {
    case UNKNOWN -> "text-primary";
    case RECOGNIZED -> "text-primary";
    case FAMILIAR -> "text-primary";
    default -> "text-neutral";
};}
<div
class="inline-flex relative rounded text-lg gap-1 cursor-pointer border border-2 border-secondary ${wordTextColor} ${wordBgColor}"

data-id="${selectableId}"
data-value="${translation.processedTargetWord()}"
data-familiarity="${translation.familiarity() != null ? translation.familiarity().name().toLowerCase() : null}"

data-is-phrase="true"
data-is-saved="true"
data-is-selectable="true"
data-is-selected="false"

hx-get="/translations/find"
hx-target="#translation-form-container-${selectableId}"
hx-swap="innerHTML"
hx-params="documentId,targetWord,id,isPhrase,isPersisted"
hx-vals='{
    "targetWord": "${translation.processedTargetWord()}",
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
    data-is-translation-form-container="true">
    </div>
    @for(var phraseEntry : ForSupport.of(translation.processedTargetWord().split(" ")))
        !{var word = phraseEntry.get();}
<%--        @template.document.word(--%>
<%--        wordId = -1,--%>
<%--        processedWord = word,--%>
<%--        word = word,--%>
<%--        isSelectable = true,--%>
<%--        isWrapped = true,--%>
<%--        familiarity = translation.familiarity())--%>
    @endfor
</div>
