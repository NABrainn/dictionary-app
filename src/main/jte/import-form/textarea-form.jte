@import lule.dictionary.dto.application.result.ServiceResultImp

@param ServiceResultImp<?> result

<div class="flex flex-col" id="content">
    <input type="hidden" name="url">
    <div class="w-full">
        <label for="area"></label>
        <textarea
        class="
        md:min-h-200
        min-h-85 outline-none border border-2 border-primary rounded text-neutral resize-none
        focus:border-accent w-full"
        name="content"
        id="area"
        placeholder="Type in content here..."></textarea>
        @if(result != null)
            @if(result.hasError() && result.messages().get("content") != null)
                <span class="text-error font-semibold text-sm">${result.messages().get("content")}</span>
            @else
                <span class="invisible text-sm">fallback</span>
            @endif
        @else
            <span class="invisible text-sm">fallback</span>
        @endif
    </div>
</div>

