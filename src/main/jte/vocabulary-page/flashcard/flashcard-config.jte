@param int familiarity = 0
@param int quantity = 10
@param boolean isPhrase = false
@param java.util.Map<String, String> messages = java.util.Map.of()

<div
id="flashcard-config-form"
class="bg-tertiary min-h-140 rounded-md w-1/4 mx-auto flex flex-col gap-12 p-8">
    <p class="font-bold text-3xl text-primary">Review translations</p>
    <div class="flex flex-col gap-4">
        <span class="font-bold text-xl text-secondary">Familiarity</span>
        <div class="flex gap-8 select-none">
            @for(int i = 1; i <= 5; i++)
                @if(familiarity == i)
                    <div
                    class="size-8 rounded-xl border border-2 border-secondary text-center text-lg text-accent bg-secondary">${familiarity}</div>
                @else
                    <div
                    class="p-0.5 flex justify-center items-center size-8 rounded-xl border border-2 border-secondary text-center text-lg text-accent cursor-pointer"
                    hx-get="/vocabulary/config-form/"
                    hx-target="#flashcard-config-form"
                    hx-swap="outerHTML"
                    hx-vals='{
                        "familiarity": "${i}",
                        "quantity": "${quantity}",
                        "isPhrase":"${isPhrase}"
                    }'>${i}</div>
                @endif
            @endfor
            @if(familiarity > 5 || familiarity < 1)
                <div
                class="p-0.5 flex justify-center items-center  size-8 rounded-xl border border-2 border-secondary text-center text-lg text-accent bg-secondary">-</div>
            @else
                <div
                class="p-0.5 flex justify-center items-center  size-8 rounded-xl border border-2 border-secondary text-center text-lg text-accent cursor-pointer"
                hx-get="/vocabulary/config-form/"
                hx-target="#flashcard-config-form"
                hx-swap="outerHTML"
                hx-vals='{
                    "familiarity": "0",
                    "quantity": "${quantity}",
                    "isPhrase":"${isPhrase}"
                }'>-</div>
            @endif
        </div>
    </div>
    <div class="flex flex-col gap-4">
        <span class="font-bold text-xl text-secondary">How many?</span>
        <div class="flex gap-8 select-none">
            @if(quantity == 10)
                <div
                class="p-0.5 flex justify-center items-center  size-8 rounded-xl border border-2 border-secondary text-center text-lg text-accent bg-secondary">10</div>
            @else
                <div
                class="p-0.5 flex justify-center items-center  size-8 rounded-xl border border-2 border-secondary text-center text-lg text-accent cursor-pointer"
                hx-get="/vocabulary/config-form/"
                hx-target="#flashcard-config-form"
                hx-swap="outerHTML"
                hx-vals='{
                    "familiarity": "${familiarity}",
                    "quantity": "10",
                    "isPhrase":"${isPhrase}"
                }'>10</div>
            @endif
            @if(quantity == 50)
                <div
                class="p-0.5 flex justify-center items-center  size-8 rounded-xl border border-2 border-secondary text-center text-lg text-accent bg-secondary">50</div>
            @else
                <div
                class="p-0.5 flex justify-center items-center  size-8 rounded-xl border border-2 border-secondary text-center text-lg text-accent cursor-pointer"
                hx-get="/vocabulary/config-form/"
                hx-target="#flashcard-config-form"
                hx-swap="outerHTML"
                hx-vals='{
                    "familiarity": "${familiarity}",
                    "quantity": "50",
                    "isPhrase":"${isPhrase}"
                }'>50</div>
            @endif
            @if(quantity == 100)
                <div
                class="p-0.5 flex justify-center items-center  size-8 rounded-xl border border-2 border-secondary text-center text-lg text-accent bg-secondary">100</div>
            @else
                <div
                class="p-0.5 flex justify-center items-center  size-8 rounded-xl border border-2 border-secondary text-center text-lg text-accent cursor-pointer"
                hx-get="/vocabulary/config-form/"
                hx-target="#flashcard-config-form"
                hx-swap="outerHTML"
                hx-vals='{
                    "familiarity": "${familiarity}",
                    "quantity": "100",
                    "isPhrase":"${isPhrase}"
                }'>100</div>
            @endif
        </div>
    </div>
    <div class="flex gap-8 select-none">
        @if(isPhrase)
            <div
            class="basis-1/2 rounded-xl border border-2 border-secondary text-center text-lg text-accent bg-secondary">Phrases</div>
            <div
            class="basis-1/2  rounded-xl border border-2 border-secondary text-center text-lg text-accent cursor-pointer"
            hx-get="/vocabulary/config-form/"
            hx-target="#flashcard-config-form"
            hx-swap="outerHTML"
            hx-vals='{
                "familiarity": "${familiarity}",
                "quantity": "${quantity}",
                "isPhrase": "false"
            }'>Words</div>
        @else
            <div
            class="basis-1/2  rounded-xl border border-2 border-secondary text-center text-lg text-accent cursor-pointer"
            hx-get="/vocabulary/config-form/"
            hx-target="#flashcard-config-form"
            hx-swap="outerHTML"
            hx-vals='{
                "familiarity": "${familiarity}",
                "quantity": "${quantity}",
                "isPhrase": "true"
            }'>Phrases</div>
            <div
            class="basis-1/2  rounded-xl border border-2 border-secondary text-center text-lg text-accent bg-secondary">Words</div>
        @endif
    </div>
    <form
    class="flex flex-col gap-2"
    hx-get="/vocabulary/flashcard/"
    hx-target="#flashcard-config-form"
    hx-swap="outerHTML"
    hx-vals='{
    "familiarity": "${familiarity}",
    "quantity": "${quantity}",
    "isPhrase":"${isPhrase}",
    "id": "1"
    }'>
        <button
        type="submit"
        class="font-bold text-accent cursor-pointer bg-primary py-2 flex justify-center rounded cursor-pointer"
        >Start</button>
        @if(messages.get("emptyList") != null)
            <span class="font-bold text-error/90">${messages.get("emptyList")}</span>
        @else
            <span class="font-bold text-error/80 invisible">placeholder</span>
        @endif
    </form>
</div>