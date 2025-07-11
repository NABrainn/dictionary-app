@import lule.dictionary.service.dto.result.ServiceResultImp
@import java.util.List

@param List<String> sourceWords
@param String targetWord
@param ServiceResultImp result

<form
    class="flex flex-col gap-2"
    hx-vals='{
        "targetWord": "${targetWord}"
    }'
    hx-put="/translations/sourceWords/update"
    hx-target="#source-word-form"
    hx-swap="innerHTML">
    <div id="source-words-list">
        @template.import-page.translation.source-words-list(sourceWords = sourceWords, targetWord = targetWord)
    </div>
    <div class="flex gap-2">
        @if(result != null)
            @if(result.hasError())
                <input
                id="input-source-word"
                class="
                outline-none border border-2 border-error rounded text-neutral text-sm
                focus:border-accent"
                type="text" placeholder="Custom translation"
                name="sourceWords">
                <button
                id="submit-source-word"
                class="text-2xl cursor-pointer"
                type="submit">+</button>
            @else
                <input
                id="input-source-word"
                class="
                outline-none border border-2 border-primary rounded text-neutral text-sm
                focus:border-accent"
                type="text" placeholder="Custom translation"
                name="sourceWords">
                <button
                id="submit-source-word"
                class="text-2xl cursor-pointer"
                type="submit">+</button>
            @endif
        @endif
    </div>
</form>