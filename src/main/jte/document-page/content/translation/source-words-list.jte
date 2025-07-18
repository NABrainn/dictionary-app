@import lule.dictionary.service.translation.dto.attribute.TranslationPair

@param TranslationPair translationPair
@param boolean isUpdateForm = true

<div class="flex flex-col gap-2 text-wrap">
    @for(String word : translationPair.sourceWords())
        <div
        class="flex gap-2">
            <input
            name="sourceWords"
            type="hidden"
            value="${word}">
            <span class="text-accent">${word}</span>
            @if(isUpdateForm)
                <form
                hx-delete="/translations/sourceWords/delete?sourceWord=${word}&targetWord=${translationPair.targetWord()}"
                hx-target="#source-words-list"
                hx-swap="innerHTML"
                hx-params="none">
                    <button class="cursor-pointer" type="submit">🗑️</button>
                </form>
            @endif
        </div>
    @endfor
</div>

