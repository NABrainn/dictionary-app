@import lule.dictionary.translations.data.attribute.WordCardAttribute

@param WordCardAttribute attribute

<form
class="
hover:bg-tertiary/80
bg-tertiary flex grow-1 justify-center items-center cursor-pointer p-8 rounded-t-md"
hx-get="/vocabulary/flashcard/flip-to-source-word"
hx-target="this"
hx-swap="outerHTML"
hx-trigger="click"
hx-vals='{
    "targetWord": "${attribute.targetWord()}"
}'>
    @for(var sw : attribute.sourceWord())
        <input name="sourceWord" value="${sw}" type="hidden">
    @endfor
    @if(attribute.sourceWord().isEmpty())
        <input name="sourceWord" value="" type="hidden">
    @endif
    <p class="block text-center grow-1 font-bold text-primary text-3xl select-none">${attribute.targetWord()}</p>
</form>
