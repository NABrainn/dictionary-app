@import java.util.List

@param List<String> sourceWords
@param String targetWord

<div class="flex flex-col gap-2">
    @for(String word : sourceWords)
        <div
        class="flex gap-2">
            <input
            name="sourceWords"
            type="hidden"
            value="${word}">
            <span class="text-accent">${word}</span>
            <form
            hx-delete="/translations/sourceWords/delete?sourceWord=${word}&targetWord=${targetWord}"
            hx-target="#source-words-list"
            hx-swap="innerHTML"
            hx-params="none">
                <button class="cursor-pointer" type="submit">🗑️</button>
            </form>
        </div>
    @endfor
</div>

