@import lule.dictionary.translations.data.TranslationLocalizationKey
@import lule.dictionary.translations.data.attribute.FlashcardConfigAttribute
@import java.util.Map

@param FlashcardConfigAttribute attribute
@param Map<TranslationLocalizationKey, String> messages 

<div
id="flashcard-config-form"
class="
md:w-1/4
bg-tertiary min-h-140 rounded-md w-4/5  mx-auto flex flex-col justify-between p-6">
    <div class="flex flex-col gap-10">
        <p class="font-bold text-3xl text-primary">${messages.get(TranslationLocalizationKey.REVIEW_TRANSLATIONS)}</p>
        <div class="flex flex-col gap-4">
            <span class="font-bold text-xl text-secondary">${messages.get(TranslationLocalizationKey.FAMILIARITY)}</span>
            <div class="
            md:gap-8
            flex gap-4 select-none">
                @for(int i = 1; i <= 5; i++)
                    @if(attribute.familiarity() == i)
                        <div
                        class="size-8 rounded-xl border border-2 border-secondary text-center text-lg text-accent bg-secondary">${attribute.familiarity()}</div>
                    @else
                        <div
                        class="p-0.5 flex justify-center items-center size-8 rounded-xl border border-2 border-secondary text-center text-lg text-accent cursor-pointer"
                        hx-get="/vocabulary/config-form/"
                        hx-target="#flashcard-config-form"
                        hx-swap="outerHTML"
                        hx-params="familiarity,quantity,isPhrase"
                        hx-vals='{
                            "familiarity": "${i}",
                            "quantity": "${attribute.quantity()}",
                            "isPhrase":"${attribute.isPhrase()}"
                        }'>${i}</div>
                    @endif
                @endfor
                @if(attribute.familiarity() > 5 || attribute.familiarity() < 1)
                    <div
                    class="p-0.5 flex justify-center items-center  size-8 rounded-xl border border-2 border-secondary text-center text-lg text-accent bg-secondary">-</div>
                @else
                    <div
                    class="p-0.5 flex justify-center items-center  size-8 rounded-xl border border-2 border-secondary text-center text-lg text-accent cursor-pointer"
                    hx-get="/vocabulary/config-form/"
                    hx-target="#flashcard-config-form"
                    hx-swap="outerHTML"
                    hx-params="familiarity,quantity,isPhrase"
                    hx-vals='{
                        "familiarity": "0",
                        "quantity": "${attribute.quantity()}",
                        "isPhrase":"${attribute.isPhrase()}"
                    }'>-</div>
                @endif
            </div>
        </div>
        <div class="flex flex-col gap-4">
            <span class="font-bold text-xl text-secondary">${messages.get(TranslationLocalizationKey.HOW_MANY)}?</span>
            <div class="flex gap-8 select-none">
                @if(attribute.quantity() == 10)
                    <div
                    class="p-0.5 flex justify-center items-center  size-8 rounded-xl border border-2 border-secondary text-center text-lg text-accent bg-secondary">10</div>
                @else
                    <div
                    class="p-0.5 flex justify-center items-center  size-8 rounded-xl border border-2 border-secondary text-center text-lg text-accent cursor-pointer"
                    hx-get="/vocabulary/config-form/"
                    hx-target="#flashcard-config-form"
                    hx-swap="outerHTML"
                    hx-vals='{
                        "familiarity": "${attribute.familiarity()}",
                        "quantity": "10",
                        "isPhrase":"${attribute.isPhrase()}"
                    }'>10</div>
                @endif
                @if(attribute.quantity() == 50)
                    <div
                    class="p-0.5 flex justify-center items-center  size-8 rounded-xl border border-2 border-secondary text-center text-lg text-accent bg-secondary">50</div>
                @else
                    <div
                    class="p-0.5 flex justify-center items-center  size-8 rounded-xl border border-2 border-secondary text-center text-lg text-accent cursor-pointer"
                    hx-get="/vocabulary/config-form/"
                    hx-target="#flashcard-config-form"
                    hx-swap="outerHTML"
                    hx-vals='{
                        "familiarity": "${attribute.familiarity()}",
                        "quantity": "50",
                        "isPhrase":"${attribute.isPhrase()}"
                    }'>50</div>
                @endif
                @if(attribute.quantity() == 100)
                    <div
                    class="p-0.5 flex justify-center items-center  size-8 rounded-xl border border-2 border-secondary text-center text-lg text-accent bg-secondary">100</div>
                @else
                    <div
                    class="p-0.5 flex justify-center items-center  size-8 rounded-xl border border-2 border-secondary text-center text-lg text-accent cursor-pointer"
                    hx-get="/vocabulary/config-form/"
                    hx-target="#flashcard-config-form"
                    hx-swap="outerHTML"
                    hx-vals='{
                        "familiarity": "${attribute.familiarity()}",
                        "quantity": "100",
                        "isPhrase":"${attribute.isPhrase()}"
                    }'>100</div>
                @endif
            </div>
        </div>
        <div class="flex gap-8 select-none">
            @if(attribute.isPhrase())
                <div
                class="basis-1/2 rounded-xl border border-2 border-secondary text-center text-lg text-accent bg-secondary">${messages.get(TranslationLocalizationKey.PHRASES)}</div>
                <div
                class="basis-1/2  rounded-xl border border-2 border-secondary text-center text-lg text-accent cursor-pointer"
                hx-get="/vocabulary/config-form/"
                hx-target="#flashcard-config-form"
                hx-swap="outerHTML"
                hx-vals='{
                    "familiarity": "${attribute.familiarity()}",
                    "quantity": "${attribute.quantity()}",
                    "isPhrase": "false"
                }'>${messages.get(TranslationLocalizationKey.WORDS)}</div>
            @else
                <div
                class="basis-1/2  rounded-xl border border-2 border-secondary text-center text-lg text-accent cursor-pointer"
                hx-get="/vocabulary/config-form/"
                hx-target="#flashcard-config-form"
                hx-swap="outerHTML"
                hx-vals='{
                    "familiarity": "${attribute.familiarity()}",
                    "quantity": "${attribute.quantity()}",
                    "isPhrase": "true"
                }'>${messages.get(TranslationLocalizationKey.PHRASES)}</div>
                <div
                class="basis-1/2  rounded-xl border border-2 border-secondary text-center text-lg text-accent bg-secondary">${messages.get(TranslationLocalizationKey.WORDS)}</div>
            @endif
        </div>
    </div>
    <form
    class="flex flex-col gap-1"
    hx-get="/vocabulary/flashcard/"
    hx-target="#flashcard-config-form"
    hx-swap="outerHTML"
    hx-vals='{
        "familiarity": "${attribute.familiarity()}",
        "quantity": "${attribute.quantity()}",
        "isPhrase":"${attribute.isPhrase()}",
        "id": "1"
    }'>
        <button
        type="submit"
        class="font-bold text-accent cursor-pointer bg-primary py-4 flex justify-center rounded cursor-pointer"
        >Start</button>
    </form>
</div>