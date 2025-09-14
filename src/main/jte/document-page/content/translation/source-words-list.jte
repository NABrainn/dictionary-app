@import java.util.List

@param boolean isUpdateForm
@param List<String> sourceWords
@param String targetWord
@param int selectedWordId
@param String _csrf

<div class="flex flex-col gap-2 text-wrap">
    @for(String word : sourceWords)
        <div
        class="flex gap-2 items-center min-h-8">
            <input
            name="sourceWords"
            type="hidden"
            value="${word}">
            <span class="block text-center text-accent text-wrap">${word}</span>
            @if(isUpdateForm)
                <form
                class="flex items-center"
                hx-delete="/translations/sourceWords/delete"
                hx-target="#translation-form-container-${selectedWordId}"
                hx-swap="innerHTML"
                hx-params="sourceWord,selectedWordId,_csrf"
                hx-vals='{
                    "selectedWordId": "${selectedWordId}",
                    "sourceWord": "${word}",
                    "_csrf": "${_csrf}"
                }'>
                    <button
                    class="cursor-pointer text-md block"
                    type="submit">🗑️</button>
                </form>
            @endif
        </div>
    @endfor
</div>

