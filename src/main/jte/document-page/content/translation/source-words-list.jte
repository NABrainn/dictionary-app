@import java.util.List

@param boolean isUpdateForm
@param List<String> sourceWords
@param String targetWord
@param int selectedWordId

<div class="flex flex-col gap-2 text-wrap">
    @for(String word : sourceWords)
        <div
        class="flex gap-2">
            <input
            name="sourceWords"
            type="hidden"
            value="${word}">
            <span class="text-accent">${word}</span>
            @if(isUpdateForm)
                <form
                hx-delete="/translations/sourceWords/delete?sourceWord=${word}"
                hx-target="#translation-form-container-${selectedWordId}"
                hx-swap="innerHTML"
                hx-params="sourceWord,selectedWordId"
                hx-vals='{
                    "selectedWordId": "${selectedWordId}"
                }'>
                    <button
                    class="cursor-pointer"
                    type="submit">🗑️</button>
                </form>
            @endif
        </div>
    @endfor
</div>

