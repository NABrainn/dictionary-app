@param String translation
@param String targetWord

<div
id="ai-translation"
class="flex flex-col bg-tertiary rounded p-1.5  w-full">
    <div class="flex gap-1 justify-between items-center">
        <span
        id="loading-box"
        class="animate-spin inline-block size-4 border-3 border-current border-t-transparent rounded-full text-accent justify-start htmx-indicator"></span>
        <div class="flex items-center gap-1">
            <span
            class="text-neutral font-bold text-2xl cursor-pointer"
            hx-get="/ollama/translate?targetWord=${targetWord}"
            hx-target="#ai-translation"
            hx-swap="outerHTML"
            hx-indicator="#loading-box">⟲</span>
            <span
            class="text-red-400 font-bold text-sm cursor-pointer"
            hx-get="/ollama/closeAiTranslation?targetWord=${targetWord}"
            hx-target="#ai-translation"
            hx-swap="outerHTML">❌</span>
        </div>
    </div>
    <div class="text-wrap leading-6 text-neutral font-bold">
        ${translation}
    </div>
</div>