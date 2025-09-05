@import lule.dictionary.translations.data.attribute.BaseFlashcardAttribute
@import java.util.List

@param BaseFlashcardAttribute attribute

!{String targetWord;}
!{List<String> sourceWord;}
@if(!attribute.translations().isEmpty())
    !{targetWord = attribute.translations().get(attribute.id() - 1).targetWord();}
    !{sourceWord = attribute.translations().get(attribute.id() - 1).sourceWords();}
@else
    !{targetWord = "";}
    !{sourceWord = List.of();}
@endif

<form
id="flashcard-form"
class="min-h-140 rounded-md w-1/4 mx-auto flex flex-col">

    @template.vocabulary-page.flashcard.target-word-screen(
        targetWord = targetWord,
        sourceWord = sourceWord
    )
    <div class="rounded-b-md flex py-10 items-center font-bold bg-secondary text-accent text-xl select-none">
        @if(attribute.id() > 1)
            <form
            class="cursor-pointer visible basis-1/4 text-center"
            hx-get="/vocabulary/flashcard/flip"
            hx-target="#flashcard-form"
            hx-swap="outerHTML"
            hx-trigger="click"
            hx-vals='{
                "id": "${attribute.id() - 1}"
            }'>⬅️
                @for(var sw : sourceWord)
                    <input name="sourceWord" value="${sw}" type="hidden">
                @endfor
            </form>
        @else
            <div class="cursor-pointer invisible basis-1/4">⬅️</div>
        @endif
        <span class="basis-1/2 text-center"> ${attribute.id()}/${attribute.size()} </span>
        @if(attribute.id() < attribute.size())
            <form
            class="cursor-pointer visible basis-1/4 text-center"
            hx-get="/vocabulary/flashcard/flip"
            hx-swap="outerHTML"
            hx-target="#flashcard-form"
            hx-trigger="click"
            hx-vals='{
                "id": "${attribute.id() + 1}"
            }'>➡️
                @for(var sw : sourceWord)
                    <input name="sourceWord" value="${sw}" type="hidden">
                @endfor
            </form>
        @else
            <form
            class="cursor-pointer visible basis-1/4 text-center"
            hx-get="/vocabulary/flashcard/"
            hx-swap="outerHTML"
            hx-target="#flashcard-form"
            hx-trigger="click"
            hx-vals='{
                "familiarity": "${attribute.familiarity()}",
                "quantity": "${attribute.quantity()}",
                "isPhrase":"${attribute.isPhrase()}",
                "id": "1"
            }'>⟳
                @for(var sw : sourceWord)
                    <input name="sourceWord" value="${sw}" type="hidden">
                @endfor
            </form>
        @endif
    </div>
</form>


