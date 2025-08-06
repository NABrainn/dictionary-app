@param String targetWords
@param int selectableId

<div
class="inline-flex relative rounded text-lg gap-1 cursor-pointer border border-3 border-secondary selected-phrase-form selectable selectable-${targetWords.replace(" ", "-")}"
id="selectable-${selectableId}"
data-selectable-value="${targetWords.replace(" ", "-")}">
    <div
    onclick="event.stopPropagation()"
    class="
    cursor-default z-2 absolute inline left-2 top-10"
    id="translation-form-container-${selectableId}"
    hx-on::after-swap="htmx.find('import-page').updatePositions(${selectableId})">
    </div>
    @for(String word : targetWords.split(" "))
        @template.document-page.content.word(
        wordId = 0,
        processedWord = word,
        documentId = 0,
        word = word,
        familiarity = lule.dictionary.enumeration.Familiarity.IGNORED,
        selectable = false
        )
    @endfor
</div>