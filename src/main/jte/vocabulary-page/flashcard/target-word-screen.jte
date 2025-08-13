@param String targetWord
@param java.util.List<String> sourceWord

<form
class="
hover:bg-tertiary/80
bg-tertiary flex grow-1 justify-center items-center cursor-pointer p-8 rounded-t-md"
hx-get="/vocabulary/flashcard/flip-to-source-word"
hx-target="this"
hx-swap="outerHTML"
hx-trigger="click"
hx-vals='{
    "targetWord": "${targetWord}"
}'>
    @for(var sw : sourceWord)
        <input name="sourceWord" value="${sw}" type="hidden">
    @endfor
    <p class="block text-center grow-1 font-bold text-primary text-3xl select-none">${targetWord}</p>
</form>
