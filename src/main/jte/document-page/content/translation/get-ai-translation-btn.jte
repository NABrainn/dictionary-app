@param String targetWord

<div
id="btn-container"
class="flex items-center gap-1 rounded cursor-pointer">
    <button
    class="appearance-none px-1 py-0.5 font-bold cursor-pointer bg-tertiary w-full rounded text-md max-w-40 text-accent"
    type="button"
    hx-get="/ollama/translate?targetWord=${targetWord}"
    hx-target="#btn-container"
    hx-swap="outerHTML"
    hx-indicator="#loading-btn">💬 AI translation</button>
    <span
    id="loading-btn"
    class="animate-spin inline-block size-4 border-3 border-current border-t-transparent rounded-full text-accent justify-start htmx-indicator"></span>
</div>